package net.gaven.model;

import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/25 9:26 上午
 **/
@Data
public class CouponRecordMessage {
    /**
     * 消息队列id
     */
    private Long messageId;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 库存锁定⼯作单id
     */
    private Long taskId;
}
