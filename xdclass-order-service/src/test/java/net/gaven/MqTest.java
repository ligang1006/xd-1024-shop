package net.gaven;

import lombok.extern.slf4j.Slf4j;
import net.gaven.config.RabbitMqConfig;
import net.gaven.model.message.OrderMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: lee
 * @create: 2021/8/24 7:04 下午
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
public class MqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Test
    public void mqTest() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("order.event.exchange", "order.close.delay.routing.key", "RssabbitMq test" + i);

        }

    }
    @Test
    public void orderMqTest() {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOutTradeNo("12321");
        rabbitTemplate.convertAndSend(rabbitMqConfig.getEventExchange(),
                rabbitMqConfig.getOrderCloseDelayRoutingKey(),orderMessage);

    }
}
