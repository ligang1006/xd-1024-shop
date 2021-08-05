package net.gaven.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redisTemplate工具
 *
 * @author: lee
 * @create: 2021/8/5 8:42 上午
 **/
@Component
public class MyRedisTemplate {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存入普通对象
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     */
    public void set(String key, String value, long timeOut, TimeUnit timeUnit) {

        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);

    }


}
