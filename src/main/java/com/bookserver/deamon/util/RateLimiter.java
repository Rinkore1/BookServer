package com.bookserver.deamon.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param key     限流的 key（通常是用户 IP 或接口 ID）
     * @param limit   在指定时间内的请求上限
     * @param seconds 时间窗口，单位为秒
     * @return 是否允许请求
     */
    public boolean isAllowed(String key, int limit, int seconds) {
        // Redis 中的 Key 使用 "rate:limiter:{key}" 模式
        String redisKey = "rate:limiter:" + key;
        Long count = redisTemplate.opsForValue().increment(redisKey); // 计数加 1

        // 第一次请求设置过期时间
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(seconds));
        }

        // 判断是否超过限制
        return count != null && count <= limit;
    }
}
