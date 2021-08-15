package net.gaven.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lee
 * @create: 2021/8/15 11:28 上午
 **/
@Configuration
public class RedissionConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPwd;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //集群模式
        //config.useClusterServers()
        //.setScanInterval(2000)
        //.addNodeAddress("redis://10.0.29.30:6379", "redis://10.0.29.95:6379")
        // .addNodeAddress("redis://127.0.0.1:6379");
        config.useSingleServer()
                .setPassword(redisPwd)
                .setAddress("redis://" + redisHost + ":" + redisPort);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
