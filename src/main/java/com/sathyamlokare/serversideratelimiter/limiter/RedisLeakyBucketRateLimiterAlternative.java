package com.sathyamlokare.serversideratelimiter.limiter;

import org.springframework.data.redis.core.RedisTemplate;

// TODO not used just an example
public class RedisLeakyBucketRateLimiterAlternative implements RateLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final String key;
    private final double leakRate;
    private final int limit;
    private final int timeInterval;

    public RedisLeakyBucketRateLimiterAlternative(RedisTemplate<String, String> redisTemplate, String key, double leakRate, int limit, int timeInterval) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.leakRate = leakRate;
        this.limit = limit;
        this.timeInterval = timeInterval;
    }

    @Override
    public boolean allowRequest(String key) {
        // NA
        return false;
    }

    //    @Override
    public boolean tryAcquire(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            value = "0";
        }
        double currentValue = Double.parseDouble(value);
        double newValue = currentValue + leakRate * ((System.currentTimeMillis() - timeInterval * 1000) / 1000.0 - Double.parseDouble(redisTemplate.opsForValue().get(key)));
        if (newValue > limit) {
            return false;
        }
        redisTemplate.opsForValue().set(key, String.valueOf(newValue));
        return true;
    }

//    @Override
    public void acquire(String key) {
        while (!tryAcquire(key)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    @Override
    public int getRemainingLimit(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return limit;
        }
        double currentValue = Double.parseDouble(value);
        return Math.max(0, (int) (limit - currentValue));
    }

//    @Override
    public long getResetTime(String key) {
        long lastLeakTime = Long.parseLong(redisTemplate.opsForValue().get(key + ":last_leak_time"));
        long resetTime = lastLeakTime + timeInterval * 1000L;
        return Math.max(0, resetTime - System.currentTimeMillis());
    }
}