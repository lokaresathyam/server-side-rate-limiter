package com.sathyamlokare.serversideratelimiter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RateLimitConfig {
    @Value("${rateLimiter.type}")
    private String rateLimiterType;

    @Value("${rateLimiter.capacity}")
    private long capacity;

    @Value("${rateLimiter.refillRate}")
    private long refillRate;

    @Value("${rateLimiter.timeInterval}")
    private long timeInterval;
}
