package net.gaven.config;

import net.gaven.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: lee
 * @create: 2021/8/13 8:36 上午
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/coupon/**")
                .addPathPatterns("/api/coupon_record/*/**");

//                .excludePathPatterns("/api/coupon/v1/page_coupon");
    }
}
