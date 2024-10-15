package com.bookserver.deamon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

import com.bookserver.deamon.interceptor.RateLimiterInterceptor;

/**
 * WebConfig 类是一个 Spring 配置类，用于配置 Web MVC 的拦截器。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 自动注入的 RateLimiterInterceptor 实例，用于限制请求速率。
     */
    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;

    /**
     * 添加拦截器到拦截器注册表中。
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor).addPathPatterns("/**");
    }
}