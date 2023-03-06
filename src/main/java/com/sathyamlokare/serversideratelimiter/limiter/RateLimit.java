package com.sathyamlokare.serversideratelimiter.limiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

// TODO unused just an example, not tested
// Usage example @RateLimit(key = "getUserById", value = 10, timeUnit = MINS) - key is not present, but you get the idea ?
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    long value();
    TimeUnit timeUnit();
}