package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.gaven.config.RabbitMqConfig;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.ProductOrderStateEnum;
import net.gaven.enums.ProductTaskStatusEnum;
import net.gaven.enums.RequestStatusEnum;
import net.gaven.exception.BizException;
import net.gaven.feign.ProductOrderFeignService;
import net.gaven.mapper.ProductMapper;
import net.gaven.mapper.ProductTaskMapper;
import net.gaven.model.ProductDO;
import net.gaven.model.ProductMessage;
import net.gaven.model.ProductTaskDO;
import net.gaven.request.LockProductRequest;
import net.gaven.request.OrderItemRequest;
import net.gaven.service.IProductService;
import net.gaven.util.JsonData;
import net.gaven.vo.ProductVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/16 9:38 上午
 **/
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductTaskMapper productTaskMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;
    @Autowired
    private ProductOrderFeignService productOrderFeignService;

    /**
     * @return
     */
    @Override
    public Map<String, Object> getList(Integer page, Integer size) {
        Page<ProductDO> productDOPage = new Page<>(page, size);

        Page<ProductDO> pageProducts = productMapper.selectPage(productDOPage, new QueryWrapper<ProductDO>()
                .orderByDesc("create_time"));
        Map<String, Object> result = new HashMap<>(4);
        result.put("total_page", pageProducts.getPages());
        result.put("total_record", pageProducts.getTotal());
        if (!CollectionUtils.isEmpty(pageProducts.getRecords())) {
            result.put("record", pageProducts.getRecords().stream().map(obj -> getProductVO(obj)).collect(Collectors.toList()));
        }
        return result;
    }

    private ProductVO getProductVO(ProductDO obj) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(obj, productVO);
        return productVO;
    }

    @Override
    public ProductVO productDetail(Long productId) {
        ProductDO productDO = productMapper.selectOne(
                new QueryWrapper<ProductDO>().eq("id", productId));
        if (productDO != null) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productDO, productVO);
            return productVO;
        }
        return null;
    }

    /**
     * 根据Id查询商品信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<ProductVO> getAllProductById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

//        productMapper.selectList(new QueryWrapper<ProductDO>().in("id", ids));
        List<ProductDO> productDOS = productMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(productDOS)) {
            return null;
        }
        return productDOS.stream().map(obj -> getProductVO(obj)).collect(Collectors.toList());
    }

    /**
     * 下完单之后操作
     * 锁定商品的同时，也要增加task表
     * 根据商品发送mq信息
     * <p>
     * 锁定商品，数量
     * 1、查询出要锁定的商品
     * 2、校验是否有足够的库存
     * 3、锁定task表
     *
     * @param lockProductRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData lockProductStock(LockProductRequest lockProductRequest) {
        //批量查询商品
        List<OrderItemRequest> orderItemList = lockProductRequest.getOrderItemList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            return null;
        }
        List<Long> productIds = orderItemList.stream()
                .map(OrderItemRequest::getProductId).collect(Collectors.toList());

        List<ProductVO> productVOS = this.getAllProductById(productIds);
        Map<Long, ProductVO> productsInfo =
                productVOS.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));
        String orderOutTradeNo = lockProductRequest.getOrderOutTradeNo();

        //更新商品状态
        updateProductTask(productsInfo, orderOutTradeNo, orderItemList);


        return JsonData.buildSuccess();
    }

    private void updateProductTask(
            Map<Long, ProductVO> productsInfo, String orderOutTradeNo, List<OrderItemRequest> orderItems) {

        for (OrderItemRequest item : orderItems) {
            //锁定商品记录
            int rows = productMapper.lockProductStock(item.getProductId(), item.getBuyNum());
            if (rows != 1) {
                throw new BizException(BizCodeEnum.PRODUCT_LOCK_FAIL);
            }
            //存储task表
            ProductTaskDO productTaskDO = new ProductTaskDO();
            productTaskDO.setProductId(item.getProductId());
            productTaskDO.setBuyNum(item.getBuyNum());
            productTaskDO.setLockState(ProductTaskStatusEnum.LOCK.name());
            productTaskDO.setOutTradeNo(orderOutTradeNo);
            productTaskDO.setProductName(productsInfo.get(item.getProductId()).getTitle());
            productTaskDO.setCreateTime(new Date());
            productTaskMapper.insert(productTaskDO);
            log.info("商品库存锁定-插入商品product_task成功:{}", productTaskDO);

            // 发送MQ延迟消息，介绍商品库存
            sendProductMessage(productTaskDO);
        }
    }

    private void sendProductMessage(ProductTaskDO productTaskDO) {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setOutTradeNo(productTaskDO.getOutTradeNo());
        productMessage.setTaskId(productTaskDO.getId());
        rabbitTemplate.convertAndSend(rabbitMqConfig.getEventExchange(), rabbitMqConfig.getStockReleaseDelayRoutingKey(), productMessage);
        log.info("send product delay success product message:{}" + productMessage);
    }

    /**
     * 释放商品
     * 1、查询task表
     * 2、查询订单情况
     * case1：订单好在下单中---->false 重新入队
     * case2：订单支付成功了---->true  不再进行入队
     * case3：订单已经不存在---->true  不入队（可能是重复消费）
     * 3、根据不同的状态，设置商品
     *
     * @param productMessage
     * @return
     */
    @Override
    public boolean releaseProductStockRecord(ProductMessage productMessage) {

        ProductTaskDO taskDO = productTaskMapper
                .selectOne(new QueryWrapper<ProductTaskDO>()
                        .eq("id", productMessage.getTaskId()));

        if (taskDO == null) {
            return true;
        }
        //task状态是否是LOCK,如果是的话查询订单的状态 status.getCode().equals(0)
        if (ProductTaskStatusEnum.LOCK.name().equals(taskDO.getLockState())) {
            JsonData status = productOrderFeignService.getOrderProductStatus(productMessage.getOutTradeNo());
            //查询数据成功
            //NEW 未支付订单,PAY已经支付订单,CANCEL超时取消订单
            if (RequestStatusEnum.OK.getCode().equals(status.getCode())) {
                if (ProductOrderStateEnum.PAY.name().equals(status.getData().toString())) {
                    //修改状态
                    log.info("订单状态已经支付,消费成功:{}", productMessage);
                    taskDO.setLockState(ProductTaskStatusEnum.FINISH.name());
                    productTaskMapper.update(taskDO, new QueryWrapper<ProductTaskDO>().eq("id", taskDO.getId()));
                    return true;
                }
                if (ProductOrderStateEnum.NEW.name().equals(status.getData().toString())) {
                    //状态是NEW新建状态，则返回给消息队，列重新投递
                    log.warn("订单状态是NEW,返回给消息队列，重新投递:{}", productMessage);
                    return false;
                }
                //cancel状态
                taskDO.setLockState(ProductTaskStatusEnum.CANCEL.name());
                productTaskMapper.update(taskDO, new QueryWrapper<ProductTaskDO>().eq("id", taskDO.getId()));
                //未知的状态类型
                log.warn("订单状态取消或者订单不存在,投递信息为:{}", productMessage);
                // 恢复商品数量
                //恢复商品库存，集锁定库存的值减去当前购买的值
                /*这里有问题？
                 * 是否存在并发问题，和幂等性问题
                 * 行锁
                 * TODO 注意：这里消费一般不会并发，一个一个消费的 当然可能存在并发的情况
                 * */
                productMapper.unlockProductStock(taskDO.getProductId(), taskDO.getBuyNum());
                return true;
            } else {
                log.warn("查询订单情况失败，请重新查询{}", productMessage);
                return true;
            }

        } else {
            log.warn("工作单状态不是LOCK,state={},消息体={}", taskDO.getLockState(), taskDO);
            return true;
        }
    }
}
