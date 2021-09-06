package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.CouponStateEnum;
import net.gaven.enums.RequestStatusEnum;
import net.gaven.exception.BizException;
import net.gaven.feign.CouponFeignService;
import net.gaven.feign.ProductFeignService;
import net.gaven.feign.UserFeignService;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.ProductOrderMapper;
import net.gaven.model.LoginUser;
import net.gaven.model.ProductOrderDO;
import net.gaven.service.IOrderService;
import net.gaven.util.CommonUtil;
import net.gaven.util.JsonData;
import net.gaven.util.RandomUtil;
import net.gaven.vo.CouponRecordVO;
import net.gaven.vo.OrderItemVO;
import net.gaven.vo.ConfirmOrderRequest;
import net.gaven.vo.ProductOrderAddressVO;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
        //订单号
        String orderOutTranceNo = RandomUtil.getRandomString();
        //
        //获取收货地址详情
        ProductOrderAddressVO addressVO = this.getUserAddress(orderRequest.getAddressId());
        log.info("get user address detail {}", addressVO);
        //商品微服务-获取最新购物项和价格(购物车，需要删除商品)
        List<Long> productIdList = orderRequest.getProductIdList();
        JsonData jsonData = productFeignService.confirmProductItems(productIdList);
        if (RequestStatusEnum.OK.getCode().equals(jsonData.getCode())) {
            List<OrderItemVO> cartItemVOList = jsonData.getData(new TypeReference<List<OrderItemVO>>() {
            });
            log.info(" confirm product from cart info {}", cartItemVOList);
            if (cartItemVOList == null) {
                //购物车商品不存在
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_CART_ITEM_NOT_EXIST);
            }
        }
        /*订单验价*/
        Long couponRecordId = orderRequest.getCouponRecordId();
        //获取优惠卷
        CouponRecordVO cartCouponRecord = getCartCouponRecord(couponRecordId);


        //获取总价格


        return null;
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
}
