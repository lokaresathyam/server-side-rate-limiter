package com.sathyamlokare.serversideratelimiter.limiter;

import org.springframework.data.redis.core.RedisTemplate;

// TODO not used just an example
public class RedisTokenBucketRateLimiterAlternative implements RateLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final String key;
    private final long capacity;
    private final long refillRate;
    private final long timeInterval;

    public RedisTokenBucketRateLimiterAlternative(RedisTemplate<String, String> redisTemplate, String key,
                                       long capacity, long refillRate, long timeInterval) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.timeInterval = timeInterval;
    }

    @Override
    public boolean allowRequest(String key) {
        // NA
        return false;
    }

    //    @Override
    public boolean tryAcquire(String key, long limit, long interval) {
        long tokens = refillTokens();
        if (tokens < limit) {
            return false;
        }

        redisTemplate.opsForValue().decrement(this.key, limit);

        return true;
    }

//    @Override
    public void acquire(String key, long limit, long interval) throws InterruptedException {
        while (true) {
            long tokens = refillTokens();
            if (tokens < limit) {
                Thread.sleep(100);
            } else {
                redisTemplate.opsForValue().decrement(this.key, limit);
                break;
            }
        }
    }

//    @Override
    public long getRemainingLimit(String key, long limit, long interval) {
        long tokens = refillTokens();
        return Math.max(limit - tokens, 0);
    }

//    @Override
    public long getResetTime(String key, long limit, long interval) {
        return (System.currentTimeMillis() / 1000) + (timeInterval - (System.currentTimeMillis() / 1000) % timeInterval);
    }

    private long refillTokens() {
        long currentTokens = Long.parseLong(redisTemplate.opsForValue().get(key) != null ?
                redisTemplate.opsForValue().get(key) : String.valueOf(capacity));

        long now = System.currentTimeMillis();
        long refillAmount = (now - (Long.parseLong(redisTemplate.opsForValue().get(key + ":ts") != null ?
                redisTemplate.opsForValue().get(key + ":ts") : String.valueOf(now))) / 1000) / refillRate;

        long newTokens = Math.min(currentTokens + refillAmount, capacity);

        redisTemplate.opsForValue().set(key, String.valueOf(newTokens));
        redisTemplate.opsForValue().set(key + ":ts", String.valueOf(now));

        return newTokens;
    }
}
