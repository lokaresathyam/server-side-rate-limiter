package com.sathyamlokare.serversideratelimiter.limiter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLeakyBucketRateLimiter implements RateLimiter {
    private final RedisTemplate<String, String> redisTemplate;
    private final String redisKeyPrefix = "ip:";

    public RedisLeakyBucketRateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean allowRequest(String ipAddress) {
        String redisKey = redisKeyPrefix + ipAddress;
        long currentTime = System.currentTimeMillis();
        String counter = redisTemplate.opsForValue().get(redisKey);
        long currentCount = (counter == null) ? 0 : Long.parseLong(counter);
        long expirationTime = currentTime + 60000; // 1 minute from now
        long newCount = currentCount + 1;

        // Check if the current count exceeds the rate limit
        if (newCount > 3) {
            return false;
        }

        // Update the counter in Redis and set an expiration time
        redisTemplate.opsForValue().set(redisKey, String.valueOf(newCount), expirationTime, TimeUnit.MINUTES);

        return true;
    }
}
