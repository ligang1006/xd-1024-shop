package net.gaven.model.message;

import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/25 9:26 上午
 **/
@Data
public class OrderMessage {
    /**
     * 消息队列id
     */
    private Long messageId;
    /**
     * 订单号
     */
    private String outTradeNo;
}
