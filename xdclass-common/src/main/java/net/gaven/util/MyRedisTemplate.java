package net.gaven.util;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate工具
 *
 * @author: lee
 * @create: 2021/8/5 8:42 上午
 **/
@Component
public class MyRedisTemplate<K, V, T> {
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

    /**
     * 如果不存在则设置成功
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     * @return
     */
    public Boolean setIfAbsent(String key, String value, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);
    }

    /**
     * 获取对应的Value
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @return
     */
    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除对应的值
     *
     * @param key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 泛型的使用
     *
     * @param script
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T luaKey(RedisScript<T> script, List<K> keys, Object... args) {
        return (T) redisTemplate.execute(script, keys, args);
    }
}
