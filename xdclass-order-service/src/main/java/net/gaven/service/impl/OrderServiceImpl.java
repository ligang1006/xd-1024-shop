package net.gaven.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alibaba.fastjson.TypeReference;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.gaven.component.PayFactory;
import net.gaven.config.RabbitMqConfig;
import net.gaven.constant.CacheKey;
import net.gaven.constant.TimeConstant;
import net.gaven.enums.*;
import net.gaven.exception.BizException;
import net.gaven.feign.CouponFeignService;
import net.gaven.feign.ProductFeignService;
import net.gaven.feign.UserFeignService;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.ProductOrderItemMapper;
import net.gaven.mapper.ProductOrderMapper;
import net.gaven.model.LoginUser;
import net.gaven.model.ProductOrderDO;
import net.gaven.model.ProductOrderItemDO;
import net.gaven.model.message.OrderMessage;
import net.gaven.request.LockCouponRecordRequest;
import net.gaven.request.LockProductRequest;
import net.gaven.request.OrderItemRequest;
import net.gaven.service.IOrderService;
import net.gaven.util.CommonUtil;
import net.gaven.util.JsonData;
import net.gaven.util.RandomUtil;
import net.gaven.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 1、分两步实现
 * <p>
 * 前端---》后端（生成一个订单支付页面，可以选支付方式）---->保存数据到数据库（生成订单）
 * <p>
 * 前端<---后端（后端返回数据给前端，可以选支付方式等）
 * <p>
 * 前端---->选择支付方式之后-->后端(请求后端数据)
 * <p>
 * <p>
 * 一步实现
 * 前端-----》后端(后端生成订单+支付)
 * 前端《-----后端（返回支付）
 * <p>
 * <p>
 * <p>
 * 1 防重提交
 * 2用户微服务-确认收货地址
 * 3商品微服务-获取最新购物项和价格
 * 4订单验价
 * *优惠券微服务-获取优惠券
 * *验证价格
 * 5锁定优惠券
 * 6锁定商品库存
 * 7创建订单对象
 * 8创建子订单对象
 * 9发送延迟消息-用于自动关单
 * 10创建支付信息-对接三方支付
 *
 * @author: lee
 * @create: 2021/8/17 7:57 下午
 **/
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Resource
    private ProductOrderMapper productOrderMapper;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;
    @Autowired
    private ProductOrderItemMapper orderItemMapper;
    @Autowired
    private PayFactory payFactory;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 确认订单
     *
     * @param orderRequest
     * @return
     */
    @Override
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        //获取用户
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
//        checkToken(orderRequest.getToken(), loginUser.getId());
        //订单号
        String orderOutTradeNo = RandomUtil.getRandomString();
        List<OrderItemVO> cartItemVOList = null;
        //
        //获取收货地址详情
        ProductOrderAddressVO addressVO = this.getUserAddress(orderRequest.getAddressId());
        log.info("get user address detail {}", addressVO);
        //商品微服务-获取最新购物项和价格(购物车，需要删除商品)
        List<Long> productIdList = orderRequest.getProductIdList();
        JsonData jsonData = productFeignService.confirmProductItems(productIdList);
        if (RequestStatusEnum.OK.getCode().equals(jsonData.getCode())) {
            cartItemVOList = jsonData.getData(new TypeReference<List<OrderItemVO>>() {
            });
            log.info(" confirm product from cart info {}", cartItemVOList);
            if (cartItemVOList == null) {
                //购物车商品不存在
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_CART_ITEM_NOT_EXIST);
            }
        }
        /*订单验价*/
        this.checkPrice(cartItemVOList, orderRequest);
        //锁定优惠券
        this.lockCouponRecords(orderRequest, orderOutTradeNo);
        //锁定库存
        this.lockProductStocks(cartItemVOList, orderOutTradeNo);

        //创建订单
        ProductOrderDO productOrderDO = this.saveProductOrder(orderRequest, loginUser, orderOutTradeNo, addressVO);

        //创建订单项
        this.saveProductOrderItems(orderOutTradeNo, productOrderDO.getId(), cartItemVOList);

        //定时关单
        sendOrderMessage(orderOutTradeNo);
        //支付
        //创建支付
        return createPay(orderOutTradeNo, productOrderDO, orderRequest, cartItemVOList);

    }

    private void checkToken(String token, Long userId) {
        if (StringUtils.isBlank(token)) {
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_NOT_EXIST);
        }
        //原子操作 校验令牌，删除令牌
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(String.format(CacheKey.SUBMIT_ORDER_TOKEN_KEY, userId)), token);
        if (result == 0L) {
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
        }
    }

    private JsonData createPay(String orderOutTradeNo, ProductOrderDO productOrderDO, ConfirmOrderRequest orderRequest, List<OrderItemVO> cartItemVOList) {
        PayInfoVO payInfoVO = new PayInfoVO(orderOutTradeNo,
                productOrderDO.getPayAmount(), orderRequest.getPayType(),
                orderRequest.getClientType(), cartItemVOList.get(0).getProductTitle(), "", TimeConstant.ORDER_PAY_TIMEOUT_MILLS);

        String payResult = payFactory.pay(payInfoVO);
        if (StringUtils.isNotBlank(payResult)) {
            log.info("创建支付订单成功:payInfoVO={},payResult={}", payInfoVO, payResult);
            return JsonData.buildSuccess(payResult);
        } else {
            log.error("创建支付订单失败:payInfoVO={},payResult={}", payInfoVO, payResult);
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
        }
    }

    private void saveProductOrderItems(String orderOutTradeNo, Long orderId, List<OrderItemVO> orderItemList) {

        List<ProductOrderItemDO> list = orderItemList.stream().map(
                obj -> {
                    ProductOrderItemDO itemDO = new ProductOrderItemDO();
                    itemDO.setBuyNum(obj.getBuyNum());
                    itemDO.setProductId(obj.getProductId());
                    itemDO.setProductImg(obj.getProductImg());
                    itemDO.setProductName(obj.getProductTitle());

                    itemDO.setOutTradeNo(orderOutTradeNo);
                    itemDO.setCreateTime(new Date());

                    //单价
                    itemDO.setAmount(obj.getAmount());
                    //总价
                    itemDO.setTotalAmount(obj.getTotalAmount());
                    itemDO.setProductOrderId(orderId);
                    return itemDO;
                }
        ).collect(Collectors.toList());
        orderItemMapper.insertBatch(list);

    }

    private ProductOrderDO saveProductOrder(
            ConfirmOrderRequest orderRequest, LoginUser loginUser,
            String orderOutTradeNo, ProductOrderAddressVO addressVO) {

        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setUserId(loginUser.getId());
        productOrderDO.setHeadImg(loginUser.getHeadImg());
        productOrderDO.setNickname(loginUser.getName());

        productOrderDO.setOutTradeNo(orderOutTradeNo);
        productOrderDO.setCreateTime(new Date());
        productOrderDO.setDel(0);
        productOrderDO.setOrderType(ProductOrderTypeEnum.DAILY.name());

        //实际支付的价格
        productOrderDO.setPayAmount(orderRequest.getRealPayAmount());

        //总价，未使用优惠券的价格
        productOrderDO.setTotalAmount(orderRequest.getTotalAmount());
        productOrderDO.setState(ProductOrderStateEnum.NEW.name());
//        ProductOrderTypeEnum.valueOf(orderRequest.getPayType()).name();
        productOrderDO.setPayType(ProductOrderPayTypeEnum.valueOf(orderRequest.getPayType()).name());

        productOrderDO.setReceiverAddress(JSON.toJSONString(addressVO));

        productOrderMapper.insert(productOrderDO);

        return productOrderDO;
    }

    private void sendOrderMessage(String orderOutTradeNo) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOutTradeNo(orderOutTradeNo);
        rabbitTemplate.convertAndSend(rabbitMqConfig.getEventExchange(),
                rabbitMqConfig.getOrderCloseDelayRoutingKey(), orderMessage);
    }

    private void lockProductStocks(List<OrderItemVO> productIdList, String orderOutTradeNo) {
        List<OrderItemRequest> products = productIdList.stream().map(product -> {
            OrderItemRequest item = new OrderItemRequest();
            item.setProductId(product.getProductId());
            item.setBuyNum(product.getBuyNum());
            return item;
        }).collect(Collectors.toList());
        LockProductRequest productRequest = new LockProductRequest();
        productRequest.setOrderOutTradeNo(orderOutTradeNo);
        productRequest.setOrderItemList(products);
        JsonData jsonData = productFeignService.lockProductStack(productRequest);
        if (!jsonData.getCode().equals(RequestStatusEnum.OK.getCode())) {
            log.error("锁定商品库存失败：{}", productIdList);
            throw new BizException(BizCodeEnum.PRODUCT_LOCK_FAIL);
        }
    }

    private void lockCouponRecords(ConfirmOrderRequest orderRequest, String orderOutTradeNo) {
        if (orderRequest.getCouponRecordId() > 0) {
            List<Long> couponRecordIds = new ArrayList<>();
            couponRecordIds.add(orderRequest.getCouponRecordId());
            LockCouponRecordRequest lockCouponRecordRequest = new LockCouponRecordRequest();
            lockCouponRecordRequest.setOrderOutTradeNo(orderOutTradeNo);
            lockCouponRecordRequest.setLockCouponRecordIds(couponRecordIds);
            //锁定优惠卷
            JsonData lockCouponRecords = couponFeignService.lockCouponRecords(lockCouponRecordRequest);
            if (!RequestStatusEnum.OK.getCode().equals(lockCouponRecords.getCode())) {
                throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
            }
//        //mq发送优惠卷的锁定
//        rabbitTemplate.convertAndSend(rabbitMqConfig.getEventExchange(), rabbitMqConfig.getCouponReleaseDelayRoutingKey(), lockCouponRecordRequest);
        }
    }

    /**
     * 验证价格 计算购物车价格，是否满足优惠券满减条件
     * * 1）统计全部商品的价格
     * * 2) 获取优惠券(判断是否满足优惠券的条件)，总价再减去优惠券的价格 就是 最终的价格
     *
     * @param orderItemList
     * @param orderRequest
     */
    private void checkPrice(List<OrderItemVO> orderItemList, ConfirmOrderRequest orderRequest) {
        //统计商品总价格
        BigDecimal realPayAmount = new BigDecimal("0");
        if (orderItemList != null) {
            for (OrderItemVO orderItemVO : orderItemList) {
                BigDecimal itemRealPayAmount = orderItemVO.getTotalAmount();
                realPayAmount = realPayAmount.add(itemRealPayAmount);
            }
        }

        //获取优惠券，判断是否可以使用
        CouponRecordVO couponRecordVO = getCartCouponRecord(orderRequest.getCouponRecordId());

        //计算购物车价格，是否满足优惠券满减条件
        if (couponRecordVO != null) {

            //计算是否满足满减
            if (realPayAmount.compareTo(couponRecordVO.getConditionPrice()) < 0) {
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_COUPON_FAIL);
            }
            if (couponRecordVO.getPrice().compareTo(realPayAmount) > 0) {
                realPayAmount = BigDecimal.ZERO;

            } else {
                realPayAmount = realPayAmount.subtract(couponRecordVO.getPrice());
            }

        }

        if (realPayAmount.compareTo(orderRequest.getRealPayAmount()) != 0) {
            log.error("订单验价失败：{}", orderRequest);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }
//        //统计商品总价格
//        BigDecimal realPayAmount = new BigDecimal("0");
//
//        Long couponRecordId = orderRequest.getCouponRecordId();
//        //获取优惠卷
//        CouponRecordVO cartCouponRecord = getCartCouponRecord(couponRecordId);
//        /*计算实际的价格，
//        Case1：使用优惠卷
//        Case2:没有使用优惠卷*/
//        for (OrderItemVO itemVO : cartItemVOList) {
//            realPayAmount.add(itemVO.getTotalAmount());
//        }
//        if (cartCouponRecord == null) {
//            //优惠卷>商品值
//        } else if (cartCouponRecord.getPrice().compareTo(realPayAmount) > 0) {
//            realPayAmount = BigDecimal.ZERO;
//        } else {
//            //商品-优惠卷价格
//            realPayAmount.divide(cartCouponRecord.getPrice());
//        }
//
//        //把最新获取的价格-前端传的总价=0 ？：
//        if (orderRequest.getRealPayAmount().compareTo(realPayAmount) != 0) {
//            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
//        }
    }

    private CouponRecordVO getCartCouponRecord(Long couponRecordId) {
        if (couponRecordId == null || couponRecordId < 0) {
            return null;
        }
        JsonData detail = couponFeignService.detail(couponRecordId);

        if (detail.getCode() != 0) {
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_COUPON_FAIL);
        }

        if (RequestStatusEnum.OK.getCode().equals(detail.getCode())) {
            CouponRecordVO couponRecordVO = detail.getData(new TypeReference<CouponRecordVO>() {
            });
            if (couponRecordVO != null
                    && couponAvailable(couponRecordVO)) {
                return couponRecordVO;
            } else {
                log.warn("当前优惠卷不可用");
                return null;
            }

        } else {
            log.warn("未获取到获取优惠卷信息{}", couponRecordId);
            return null;
        }
    }

    /**
     * 判断优惠券是否可用
     *
     * @param couponRecordVO
     * @return
     */
    private boolean couponAvailable(CouponRecordVO couponRecordVO) {

        if (couponRecordVO.getUseState().equalsIgnoreCase(CouponStateEnum.NEW.name())) {
            long currentTimestamp = CommonUtil.getCurrentTimestamp();
            long end = couponRecordVO.getEndTime().getTime();
            long start = couponRecordVO.getStartTime().getTime();
            if (currentTimestamp >= start && currentTimestamp <= end) {
                return true;
            }
        }
        return false;
    }

    private ProductOrderAddressVO getUserAddress(long addressId) {
        JsonData addressData = userFeignService.addressDetail(addressId);
        if (addressData.getCode() != 0) {
            log.error("获取收获地址失败,msg:{}", addressData);
            throw new BizException(BizCodeEnum.ADDRESS_NO_EXITS);
        }
        ProductOrderAddressVO addressVO = addressData.getData(new TypeReference<ProductOrderAddressVO>() {
        });
        return addressVO;
    }


    @Override
    public String queryProductOrderState(String outTradeNo) {

        ProductOrderDO productOrderDO = productOrderMapper
                .selectOne(new QueryWrapper<ProductOrderDO>()
                        .eq("out_trade_no", outTradeNo));
        if (productOrderDO == null) {
            return null;
        }
        return productOrderDO.getState();
    }

    /**
     * 关单服务
     * 查询是否能关闭订单
     * <p>
     * 在关闭订单之前，需要到第三方查询当前的订单状态
     * 先到本地数据库查询订单的状态
     *
     * @param orderMessage
     * @return
     */
    @Override
    public Boolean closeProductOrder(OrderMessage orderMessage) {
        log.info("start close order the orderNo is{}", orderMessage.getOutTradeNo());
        String outTradeNo = orderMessage.getOutTradeNo();
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>().eq("out_trade_no", outTradeNo));
        //订单不存在，直接关单
        if (productOrderDO == null) {
            log.warn("this order not exist {}", outTradeNo);
            return true;
        }
        //查询订单状态
        String state = productOrderDO.getState();
        if (ProductOrderStateEnum.PAY.name().equalsIgnoreCase(state)) {
            log.info("this order is pay{}", orderMessage.getOutTradeNo());
            return true;
        }


        //向第三方支付查询订单是否真的未支付
        //向第三方支付查询订单是否真的未支付
        PayInfoVO payInfoVO = new PayInfoVO();
        payInfoVO.setPayType(productOrderDO.getPayType());
        payInfoVO.setOutTradeNo(orderMessage.getOutTradeNo());
        String payResult = payFactory.queryPaySuccess(payInfoVO);

        //支付，return true 关单 修改订单状态
        // 未支付，到第三方查询订单状态
        //第三方 未支付--->true

        //未支付成功
        if (StringUtils.isBlank(payResult)) {
            productOrderMapper.updateOrderPayState(ProductOrderStateEnum.CANCEL.name(), ProductOrderStateEnum.NEW.name(), outTradeNo);
            log.info("结果为空，则未支付成功，本地取消订单:{}", orderMessage);
            return true;
        } else {
            //支付成功，主动的把订单状态改成UI就支付，造成该原因的情况可能是支付通道回调有问题
            productOrderMapper.updateOrderPayState(ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name(), outTradeNo);
            log.info("支付成功，主动的把订单状态改成UI就支付，造成该原因的情况可能是支付通道回调有问题:{}", orderMessage);
            return true;
        }

    }

    /**
     * 处理第三方回调的结果
     * 保存订单库
     *
     * @param payType
     * @param requestParams
     * @return
     */
    @Override
    public JsonData handlerOrderCallbackMsg(String payType, Map<String, String> requestParams) {
        if (ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payType)) {
            String outTradeNo = requestParams.get("out_trade_no");
            String tradeStatus = requestParams.get("trade_status");

            if ("TRADE_SUCCESS".equalsIgnoreCase(tradeStatus) || "TRADE_FINISH".equalsIgnoreCase(tradeStatus)) {
                //更新订单状态
                int i = productOrderMapper.updateOrderPayState(ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name(), outTradeNo);
                if (i == 1) {
                    return JsonData.buildSuccess();
                } else {
                    return JsonData.buildResult(BizCodeEnum.PAY_ORDER_UPDATE_FAIL);
                }
            } else if (ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payType)) {
                ///TODO 微信支付
            }

        }
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_CALLBACK_NOT_SUCCESS);
    }

    @Override
    public Map<String, Object> page(int page, int size, String state) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        Page<ProductOrderDO> pageInfo = new Page<>(page, size);
        IPage<ProductOrderDO> orderDOPage = null;
        if (StringUtils.isBlank(state)) {
            orderDOPage = productOrderMapper
                    .selectPage(pageInfo, new QueryWrapper<ProductOrderDO>()
                            .eq("user_id", loginUser.getId()));
        } else {
            orderDOPage = productOrderMapper
                    .selectPage(pageInfo, new QueryWrapper<ProductOrderDO>()
                            .eq("user_id", loginUser.getId())
                            .eq("state", state));
        }
        //获取订单列表
        List<ProductOrderDO> orderList = orderDOPage.getRecords();
        if (CollectionUtils.isEmpty(orderList)) {
            return null;
        }
        List<ProductOrderVO> productOrderVOSList = orderList.stream().map(order -> {
            List<ProductOrderItemDO> productOrderItemDOS = orderItemMapper.selectList(new QueryWrapper<ProductOrderItemDO>()
                    .eq("product_order_id", order.getId()));
            List<OrderItemVO> itemVOS = productOrderItemDOS.stream().map(itemDos -> {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setProductId(itemDos.getProductId());
                orderItemVO.setAmount(order.getTotalAmount());
                orderItemVO.setProductImg(itemDos.getProductImg());
                orderItemVO.setBuyNum(itemDos.getBuyNum());
                orderItemVO.setProductTitle(itemDos.getProductName());
                return orderItemVO;
            }).collect(Collectors.toList());
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(order, productOrderVO);
            productOrderVO.setOrderItemList(itemVOS);
            return productOrderVO;
        }).collect(Collectors.toList());
        Map<String, Object> pageMap = new HashMap<>(1 << 2);
        pageMap.put("total_record", orderDOPage.getTotal());
        pageMap.put("total_page", orderDOPage.getPages());
        pageMap.put("current_data", productOrderVOSList);
        return pageMap;
    }
}
