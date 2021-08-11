package net.gaven.config;

import net.gaven.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: lee
 * @create: 2021/8/10 8:38 下午
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(getLoginInterceptor())
                //拦截路径
                .addPathPatterns("/api/v1/address/*/**")
                .addPathPatterns("/api/v1/user/detail");

    }
}
