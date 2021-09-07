package net.gaven.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.gaven.model.message.OrderMessage;
import net.gaven.model.message.ProductMessage;
import net.gaven.service.IOrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: lee
 * @create: 2021/9/7 3:17 下午
 **/
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.order_close_queue}")
public class ProductOrderMqListener {
    @Autowired
    private IOrderService orderService;

    /**
     * 消息重复消费，幂等性校验
     * 并发情况下怎么操作
     *
     * @param orderMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void closeProductOrder(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
        log.info("get product info from mq {}", orderMessage);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            Boolean flag = orderService.closeProductOrder(orderMessage);
            if (flag) {
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, true);
            }
        } catch (Exception e) {
            log.error("close product fail begin repost to mq{}", orderMessage);
            channel.basicReject(deliveryTag, true);
        }
    }
}