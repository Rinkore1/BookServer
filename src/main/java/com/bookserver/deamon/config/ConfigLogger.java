package com.bookserver.deamon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigLogger {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.timeout}")
    private String redisTimeout;

    @Value("${resilience4j.circuitbreaker.instances.bookServiceCircuitBreaker.failureRateThreshold}")
    private float failureRateThreshold;

    @Value("${resilience4j.circuitbreaker.instances.bookServiceCircuitBreaker.minimumNumberOfCalls}")
    private int minimumNumberOfCalls;

    @Value("${resilience4j.circuitbreaker.instances.bookServiceCircuitBreaker.waitDurationInOpenState}")
    private String waitDurationInOpenState;

    @Value("${resilience4j.circuitbreaker.instances.bookServiceCircuitBreaker.permittedNumberOfCallsInHalfOpenState}")
    private int permittedNumberOfCallsInHalfOpenState;

    @Value("${resilience4j.circuitbreaker.instances.bookServiceCircuitBreaker.slidingWindowSize}")
    private int slidingWindowSize;

    @PostConstruct
    public void logConfig() {
        System.out.println("MongoDB URI: " + mongoUri);
        System.out.println("Redis Host: " + redisHost);
        System.out.println("Redis Port: " + redisPort);
        System.out.println("Redis Timeout: " + redisTimeout);
        System.out.println("Circuit Breaker Failure Rate Threshold: " + failureRateThreshold);
        System.out.println("Circuit Breaker Minimum Number of Calls: " + minimumNumberOfCalls);
        System.out.println("Circuit Breaker Wait Duration in Open State: " + waitDurationInOpenState);
        System.out.println("Circuit Breaker Permitted Number of Calls in Half Open State: "
                + permittedNumberOfCallsInHalfOpenState);
        System.out.println("Circuit Breaker Sliding Window Size: " + slidingWindowSize);
    }
}
