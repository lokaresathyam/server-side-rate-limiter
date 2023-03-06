package com.sathyamlokare.serversideratelimiter.limiter;

import com.sathyamlokare.serversideratelimiter.config.RateLimitConfig;
import com.sathyamlokare.serversideratelimiter.constant.LimiterType;
import com.sathyamlokare.serversideratelimiter.interceptor.RateLimiterInterceptor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConfigurationService implements WebMvcConfigurer {

    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimitConfig rateLimitConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RateLimiterInterceptor rateLimiterInterceptor =
                new RateLimiterInterceptor(rateLimiterFactory, rateLimitConfig);

        registry.addInterceptor(rateLimiterInterceptor);
    }
}



