package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.gaven.mapper.ProductOrderMapper;
import net.gaven.model.ProductOrderDO;
import net.gaven.service.IOrderService;
import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Override
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        return null;
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
