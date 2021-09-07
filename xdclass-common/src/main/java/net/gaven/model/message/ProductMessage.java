package net.gaven.model.message;

import lombok.Data;

/**
 * @author: lee
 * @create: 2021/9/7 3:20 下午
 **/
@Data
public class ProductMessage {
    /**
     * 消息队列id
     */
    private long messageId;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 库存锁定taskId
     */
    private long taskId;
}
