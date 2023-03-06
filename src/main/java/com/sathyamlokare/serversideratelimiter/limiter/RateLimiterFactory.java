package com.sathyamlokare.serversideratelimiter.limiter;

import com.sathyamlokare.serversideratelimiter.constant.LimiterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterFactory {

    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    private final RedisLeakyBucketRateLimiter redisLeakyBucketRateLimiter;

    private final RedisTokenBucketRateLimiter redisTokenBucketRateLimiter;

    @Autowired
    public RateLimiterFactory(RedisLeakyBucketRateLimiter redisLeakyBucketRateLimiter, RedisTokenBucketRateLimiter redisTokenBucketRateLimiter) {
        this.redisLeakyBucketRateLimiter = redisLeakyBucketRateLimiter;
        this.redisTokenBucketRateLimiter = redisTokenBucketRateLimiter;
    }

    public RateLimiter getRateLimiter(String limiterType) {
        return rateLimiters.computeIfAbsent(limiterType, type -> {
            switch (LimiterType.valueOf(type)) {
                case LEAKY_BUCKET:
                    return redisLeakyBucketRateLimiter;
                case TOKEN_BUCKET:
                    return redisTokenBucketRateLimiter;
                default:
                    throw new IllegalArgumentException("Invalid limiter type: " + type);
            }
        });
    }
}