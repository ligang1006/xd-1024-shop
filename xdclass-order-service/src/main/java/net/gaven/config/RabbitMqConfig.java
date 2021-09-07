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
    @Value("${mqconfig.order_event_exchange}")
    private String eventExchange;


    /**
     * 第一个队列  延迟队列，
     */
    @Value("${mqconfig.order_close_delay_queue}")
    private String orderCloseDelayQueue;

    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    @Value("${mqconfig.order_close_delay_routing_key}")
    private String orderCloseDelayRoutingKey;


    /**
     * 第二个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.order_close_queue}")
    private String orderCloseQueue;

    /**
     * 第二个队列的路由key
     * <p>
     * 即进入死信队列的路由key
     */
    @Value("${mqconfig.order_close_routing_key}")
    private String orderCloseRoutingKey;

    /**
     * 过期时间
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;


    /**
     * 消息转换器
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 创建交换机 Topic类型，也可以用dirct路由
     * 一般一个微服务一个交换机
     *
     * @return
     */
    @Bean
    public Exchange couponEventExchange() {
        return new TopicExchange(eventExchange, true, false);
    }


    /**
     * 延迟队列
     */
    @Bean
    public Queue orderCloseDelayQueue() {

        Map<String, Object> args = new HashMap<>(3);
        args.put("x-message-ttl", ttl);
        args.put("x-dead-letter-routing-key", orderCloseRoutingKey);
        args.put("x-dead-letter-exchange", eventExchange);

        return new Queue(orderCloseDelayQueue, true, false, false, args);
    }


    /**
     * 死信队列，普通队列，用于被监听
     */
    @Bean
    public Queue couponReleaseQueue() {

        return new Queue(orderCloseQueue, true, false, false);

    }


    /**
     * 第一个队列，即延迟队列的绑定关系建立
     *
     * @return
     */
    @Bean
    public Binding couponReleaseDelayBinding() {

        return new Binding(orderCloseDelayQueue, Binding.DestinationType.QUEUE, eventExchange, orderCloseDelayRoutingKey, null);
    }

    /**
     * 死信队列绑定关系建立
     *
     * @return
     */
    @Bean
    public Binding couponReleaseBinding() {

        return new Binding(orderCloseQueue, Binding.DestinationType.QUEUE, eventExchange, orderCloseRoutingKey, null);
    }

}
