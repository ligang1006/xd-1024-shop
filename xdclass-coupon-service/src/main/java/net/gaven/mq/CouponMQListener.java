package net.gaven.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.gaven.model.CouponRecordMessage;
import net.gaven.service.ICouponRecordService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: lee
 * @create: 2021/8/26 9:09 上午
 **/
@Slf4j
@Component
//监听的队列，用于消费的队列key
@RabbitListener(queues = "${mqconfig.coupon_release_queue}")
public class CouponMQListener {

    @Autowired
    private ICouponRecordService couponRecordService;

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
     * @param recordMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void releaseCouponRecord(CouponRecordMessage recordMessage,
                                    Message message, Channel channel) throws IOException {

        log.info("监听到消息：releaseCouponRecord消息内容：{}", recordMessage);
        //消息标签
        long msgTag = message.getMessageProperties().getDeliveryTag();
        //释放优惠卷(record表设置状态)
        boolean flag = couponRecordService.releaseCouponRecord(recordMessage);
        //当前记录
//        RLock lock = redissonClient.getLock("lock:coupon_record_release:" + recordMessage.getTaskId());
//        lock.lock();
        //释放优惠卷成功
        try {
            if (flag) {
                //确认消息消费成功，不进行入队
                channel.basicAck(msgTag, false);
            } else {
                log.error("释放优惠券失败 flag=false,{}", recordMessage);
                //重新入队，
                channel.basicReject(msgTag, true);
            }
        } catch (IOException e) {
            log.error("释放优惠券记录异常:{},msg:{}", e, recordMessage);
            channel.basicReject(msgTag, true);
        }
//        finally {
//            lock.unlock();
//        }
        //   //防止同个解锁任务并发进入；如果是串行消费不用加锁；加锁有利也有弊，看项目业务逻辑而定
        //        //Lock lock = redissonClient.getLock("lock:coupon_record_release:"+recordMessage.getTaskId());
        //        //lock.lock();
        //        try {
        //            if (flag) {
        //                //确认消息消费成功
        //                channel.basicAck(msgTag, false);
        //            }else {
        //                log.error("释放优惠券失败 flag=false,{}",recordMessage);
        //                channel.basicReject(msgTag,true);
        //            }
        //
        //        } catch (IOException e) {
        //            log.error("释放优惠券记录异常:{},msg:{}",e,recordMessage);
        //            channel.basicReject(msgTag,true);
        //        }
        ////        finally {
        ////            lock.unlock();
        ////        }

    }
}
