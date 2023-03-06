package com.sathyamlokare.serversideratelimiter.config;

import com.sathyamlokare.serversideratelimiter.interceptor.RateLimiterInterceptor;
import com.sathyamlokare.serversideratelimiter.limiter.RateLimiterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimitConfig rateLimitConfig;

    public WebConfig(RateLimiterFactory rateLimiterFactory, RateLimitConfig rateLimitConfig) {
        this.rateLimiterFactory = rateLimiterFactory;
        this.rateLimitConfig = rateLimitConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimiterInterceptor(rateLimiterFactory, rateLimitConfig));
    }
}