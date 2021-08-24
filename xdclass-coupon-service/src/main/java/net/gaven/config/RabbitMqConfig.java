package net.gaven.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lee
 * @create: 2021/8/24 12:49 下午
 **/
@Data
@Configuration
public class RabbitMqConfig {
    /**
     * 交换机
     */
    @Value("${mqconfig.coupon_event_exchange}")
    private String eventExchange;
    /**
     * 第⼀个队列延迟队列，
     */
    @Value("${mqconfig.coupon_release_delay_queue}"
    )
    private String couponReleaseDelayQueue;
    /**
     * 第⼀个队列的路由key
     * 进⼊队列的路由key
     */
    @Value("${mqconfig.coupon_release_delay_routing_key}")
    private String couponReleaseDelayRoutingKey;
    /**
     * 第⼆个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.coupon_release_queue}")
    private String couponReleaseQueue;
    /**
     * 第⼆个队列的路由key
     * <p>
     * 即进⼊死信队列的路由key
     */
    @Value("${mqconfig.coupon_release_routing_key}"
    )
    private String couponReleaseRoutingKey;
    /**
     * 过期时间
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;

    /**
     * 以jackson序列化转化
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 交换机，也可以用direct交换机
     * 一般一个微服务一个
     *
     * @return
     */
    @Bean
    public Exchange convertEvenExchange() {
        //持久化 true 自动删除false
        return new TopicExchange(eventExchange, true, false);
    }

    @Bean
    public Queue couponReleaseDelayQueue() {

        Map<String, Object> args = new HashMap<>(3);
        args.put("x-message-ttl", ttl);
        args.put("x-dead-letter-routing-key", couponReleaseRoutingKey);
        args.put("x-dead-letter-exchange", eventExchange);

        return new Queue(couponReleaseDelayQueue, true, false, false, args);
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue couponReleaseDelQueue() {
        return new Queue(couponReleaseDelayQueue, true, false, false);
    }

    /**
     * 绑定延迟队列
     */
    @Bean
    public Binding couponReleaseDelayBinding() {
        return new Binding(couponReleaseDelayQueue,
                Binding.DestinationType.QUEUE, eventExchange,
                couponReleaseDelayRoutingKey, null);
    }

    /**
     * 死信队列绑定关系建立
     *
     * @return
     */
    @Bean
    public Binding couponReleaseBinding() {

        return new Binding(couponReleaseQueue, Binding.DestinationType.QUEUE, eventExchange, couponReleaseRoutingKey, null);
    }


}
