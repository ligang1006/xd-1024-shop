package net.gaven.service;

import net.gaven.model.message.OrderMessage;
import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;

import java.util.Map;

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
     * 查询订单状态
     *
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);

    /**
     * 定时关闭订单
     *
     * @param orderMessage
     * @return
     */
    Boolean closeProductOrder(OrderMessage orderMessage);

    /**
     * 处理阿里支付回调的结果 支付通知结果更新订单状态
     *
     * @param payType
     * @param requestParams
     * @return
     */
    JsonData handlerOrderCallbackMsg(String payType, Map<String, String> requestParams);

    /**
     * 获取订单列表
     *
     * @param page
     * @param size
     * @param state
     * @return
     */
    Map<String, Object> page(int page, int size, String state);
}
