package net.gaven.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: lee
 * @create: 2021/8/15 11:56 上午
 **/
@Component
public class MyRedissionClient {
    @Autowired
    private RedissonClient redissioClient;

    public RLock getLock(String lockName) {
        RLock lock = redissioClient.getLock(lockName);
        return lock;
    }


}
