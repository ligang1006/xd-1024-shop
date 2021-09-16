package net.gaven.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lee
 * @create: 2021/9/16 7:09 下午
 **/
//@Data
//@Component
//@ConfigurationProperties(prefix = "user")

@Configuration
public class UserConfig {
    /**
     * 2.2 之前版本,必须使用 @Component 或者 @Configuration 声明成Spring Bean
     */
    private Map<String, String> user = new HashMap<>();

    @Bean
    @ConfigurationProperties(prefix = "contract")
    public Map<String, String> getUserConfig() {
        return user;
    }

}
