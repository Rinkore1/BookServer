package com.bookserver.deamon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

import com.bookserver.deamon.interceptor.RateLimiterInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor).addPathPatterns("/**");
    }
}
