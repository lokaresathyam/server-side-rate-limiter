package com.sathyamlokare.serversideratelimiter.limiter;

import com.sathyamlokare.serversideratelimiter.config.RateLimitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTokenBucketRateLimiter implements RateLimiter {
    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitConfig rateLimitConfig;

    @Override
    public boolean allowRequest(String ipAddress) {
        String redisKeyPrefix = "ip:";
        String redisKey = redisKeyPrefix + ipAddress;
        long currentTime = System.currentTimeMillis();
        String lastRefillTimeStr = redisTemplate.opsForValue().get(redisKey + ":lastRefillTime");
        long lastRefillTime = (lastRefillTimeStr == null) ? 0 : Long.parseLong(lastRefillTimeStr);
        String tokensStr = redisTemplate.opsForValue().get(redisKey + ":tokens");
        long tokens = (tokensStr == null) ? 0 : Long.parseLong(tokensStr);

        // Calculate the number of tokens to refill since last refill time
        long elapsedTime = currentTime - lastRefillTime;
        long tokensToRefill = Math.min(rateLimitConfig.getCapacity(),
                tokens + (elapsedTime * rateLimitConfig.getRefillRate() / rateLimitConfig.getTimeInterval())) - tokens;

        // Refill the bucket with new tokens if needed
        if (tokensToRefill > 0) {
            redisTemplate.opsForValue().increment(redisKey + ":tokens", tokensToRefill);
            redisTemplate.opsForValue().set(redisKey + ":lastRefillTime", String.valueOf(currentTime));
        }

        // Check if there are enough tokens to allow the request
        if (tokens < 1) {
            return false;
        }

        // Consume one token and update the bucket in Redis
        redisTemplate.opsForValue().decrement(redisKey + ":tokens");

        return true;
    }
}
