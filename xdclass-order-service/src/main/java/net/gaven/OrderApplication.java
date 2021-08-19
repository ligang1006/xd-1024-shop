package net.gaven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: lee
 * @create: 2021/8/19 9:06 上午
 **/
@SpringBootApplication
@MapperScan("net.gaven.mapper")
@EnableTransactionManagement
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
