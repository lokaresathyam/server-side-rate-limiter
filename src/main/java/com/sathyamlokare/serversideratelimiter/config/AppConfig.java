package com.sathyamlokare.serversideratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // TODO
//        redisStandaloneConfiguration.setHostName(environment.getProperty("spring.redis.host"));
//        redisStandaloneConfiguration.setPort(Integer.parseInt(environment.getProperty("spring.redis.port")));
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(Integer.parseInt("6379"));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    @Bean(name = "redisLeakyBucketRateLimiter")
//    public RedisLeakyBucketRateLimiter redisLeakyBucketRateLimiter(RedisTemplate<String, String> redisTemplate) {
//        return new RedisLeakyBucketRateLimiter(redisTemplate);
//    }
//
//    @Bean(name = "redisTokenBucketRateLimiter")
//    public RedisTokenBucketRateLimiter redisTokenBucketRateLimiter() {
//        return new RedisTokenBucketRateLimiter(redisTemplate(),
//                rateLimitConfig.getCapacity(),
//                rateLimitConfig.getRefillRate(),
//                rateLimitConfig.getTimeInterval());
//    }

    // TODO remove this I think this is useless
//    @Bean
//    public WebMvcConfigurer webMvcConfigurer(RateLimiterInterceptor rateLimiterInterceptor) {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(rateLimiterInterceptor);
//            }
//        };
//    }
}