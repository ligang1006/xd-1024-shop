package net.gaven.mq;

import lombok.extern.slf4j.Slf4j;
import net.gaven.CouponServiceApplication;
import net.gaven.model.CouponRecordMessage;
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
    public void mqTest() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("coupon.event.exchange", "coupon.release.delay.routing.key", "RssabbitMq test" + i);

        }

    }

    @Test
    public void couponRecordReleaseTest() {
        CouponRecordMessage recordMessage = new CouponRecordMessage();
        recordMessage.setOutTradeNo("123456abc");
        recordMessage.setTaskId(1L);
        rabbitTemplate.convertAndSend("coupon.event.exchange", "coupon.release.delay.routing.key", recordMessage);
    }
}
