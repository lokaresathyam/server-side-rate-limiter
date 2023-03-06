package com.sathyamlokare.serversideratelimiter.interceptor;

import com.sathyamlokare.serversideratelimiter.config.RateLimitConfig;
import com.sathyamlokare.serversideratelimiter.limiter.RateLimiterFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimitConfig rateLimitConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ipAddress = request.getRemoteAddr();
        var rateLimiter = rateLimiterFactory.getRateLimiter(rateLimitConfig.getRateLimiterType());

        if (!rateLimiter.allowRequest(ipAddress)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }

        // TODO you can choose to do this
//        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(rateLimiter.getRemainingLimit(ipAddress)));
//        response.setHeader("X-Rate-Limit-Reset", String.valueOf(rateLimiter.getResetTime(ipAddress)));
        return true;
    }

}