package net.gaven.service;

import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;

/**
 * @author: lee
 * @create: 2021/8/17 7:57 下午
 **/
public interface IOrderService {
    /**
     * 提交订单
     *
     * @param orderRequest
     */
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);

    /**
     *
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);
}
