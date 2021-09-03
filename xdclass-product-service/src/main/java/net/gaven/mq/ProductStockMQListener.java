package net.gaven.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.gaven.model.ProductMessage;
import net.gaven.service.IProductService;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: lee
 * @create: 2021/9/2 12:33 下午
 **/
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.stock_release_queue}")
public class ProductStockMQListener {

    @Autowired
    private IProductService productService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 重复消费-幂等性
     * <p>
     * 消费失败，重新入队后最大重试次数：
     * 如果消费失败，不重新入队，可以记录日志，然后插到数据库人工排查
     * <p>
     * 消费者这块还有啥问题，大家可以先想下，然后给出解决方案
     *
     * @param productMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void releaseProductStockRecord(ProductMessage productMessage,
                                          Message message, Channel channel) throws IOException {

        log.info("监听到消息：releaseProductStockRecord：{}", productMessage);
        //消息标签
        long msgTag = message.getMessageProperties().getDeliveryTag();
        //释放商品(record表设置状态)
        boolean flag = productService.releaseProductStockRecord(productMessage);
        try {
            //释放优惠卷成功
            if (flag) {
                //确认消息消费成功，不进行入队
                channel.basicAck(msgTag, false);
            } else {
                log.error("释放商品库存失败 flag=false,{}", productMessage);
                //重新入队，
                channel.basicReject(msgTag, true);
            }
        } catch (IOException e) {
            log.error("释放商品库存异常:{},msg:{}", e, productMessage);
            channel.basicReject(msgTag, true);
        }

    }

}
