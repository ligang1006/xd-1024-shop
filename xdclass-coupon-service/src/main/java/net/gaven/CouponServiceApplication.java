package net.gaven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: lee
 * @create: 2021/8/12 8:56 上午
 **/
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("net.gaven.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class CouponServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }
}
