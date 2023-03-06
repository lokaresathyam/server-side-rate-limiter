package com.sathyamlokare.serversideratelimiter.interceptor;

import com.sathyamlokare.serversideratelimiter.config.RateLimitConfig;
import com.sathyamlokare.serversideratelimiter.limiter.RateLimit;
import com.sathyamlokare.serversideratelimiter.limiter.RateLimiter;
import com.sathyamlokare.serversideratelimiter.limiter.RateLimiterFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

// TODO not used just an example
@Component
@Aspect
@RequiredArgsConstructor
public class AnnotatedRateLimiterInterceptor {

    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimitConfig rateLimitConfig;

    @Around("@annotation(rateLimit)")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint, RateLimit rateLimit) throws Throwable {
        RateLimiter rateLimiter = rateLimiterFactory.getRateLimiter(rateLimitConfig.getRateLimiterType());

        long limit = (long) getValue(rateLimit, "value");
        long interval = ((TimeUnit) getValue(rateLimit, "timeUnit")).toMillis(1);

//        boolean allowed = rateLimiter.tryAcquire(limit, interval);
//        if (!allowed) {
//            throw error
//        }

        return proceedingJoinPoint.proceed();
    }

    private Object getValue(RateLimit rateLimit, String name) throws Exception {
        Method method = rateLimit.annotationType().getDeclaredMethod(name);
        return method.invoke(rateLimit);
    }
}