package net.gaven.enums;

/**
 * @author: lee
 * @create: 2021/8/17 8:03 下午
 **/
public enum ProductOrderStateEnum {
    /**
     * 未支付订单
     */
    NEW,

    /**
     * 已经支付订单
     */
    PAY,

    /**
     * 超时取消订单
     */
    CANCEL;
}
