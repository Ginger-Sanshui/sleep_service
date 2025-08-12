package com.example.demo.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenAuth())
                .addPathPatterns("/**")          // 拦截所有路径
                .excludePathPatterns("/Token","/Music/**","/addSensor","/addSensor32");  // 排除登录接口
    }
}