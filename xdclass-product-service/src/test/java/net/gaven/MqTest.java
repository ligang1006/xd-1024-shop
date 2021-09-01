package net.gaven;

import lombok.extern.slf4j.Slf4j;
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
@SpringBootTest(classes = ProductApplication.class)
@Slf4j
public class MqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void mqTest() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("stock.event.exchange", "stock.release.delay.routing.key", "RssabbitMq test" + i);

        }

    }

}
