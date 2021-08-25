package net.gaven.mq;

import lombok.extern.slf4j.Slf4j;
import net.gaven.CouponServiceApplication;
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
@SpringBootTest(classes = CouponServiceApplication.class)
@Slf4j
public class MqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void mqTest(){
        rabbitTemplate.convertAndSend("coupon.event.exchange","coupon.release.delay.routing.key","RabbitMq test");


    }
}
