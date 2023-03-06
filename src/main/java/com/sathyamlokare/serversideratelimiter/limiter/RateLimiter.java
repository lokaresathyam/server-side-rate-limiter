package com.sathyamlokare.serversideratelimiter.limiter;

public interface RateLimiter {
    boolean allowRequest(String key);
}
