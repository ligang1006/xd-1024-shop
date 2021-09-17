package net.gaven.constant;

/**
 * @author: lee
 * @create: 2021/9/17 9:29 上午
 **/
public class TimeConstant {
    /**
     *
     * 支付订单的有效时长，超过未支付则关闭订单
     *
     * 订单超时，毫秒，默认30分钟
     */
    public static final long ORDER_PAY_TIMEOUT_MILLS = 30*60*1000;
//    public static final long ORDER_PAY_TIMEOUT_MILLS = 5*60*1000;
}
