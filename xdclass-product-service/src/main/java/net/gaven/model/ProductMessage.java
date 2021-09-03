package net.gaven.model;

import lombok.Data;

/**
 * 发送消息协议
 *
 * @author: lee
 * @create: 2021/9/2 11:10 上午
 **/
@Data
public class ProductMessage {
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
