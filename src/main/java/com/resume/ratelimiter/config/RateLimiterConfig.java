package com.resume.ratelimiter.config;

import com.resume.ratelimiter.algorithm.*;
import com.resume.ratelimiter.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RateLimiterConfig {

    private final RateLimitProperties properties;
    private final StringRedisTemplate redisTemplate;

    @Bean
    public RateLimitStorage storage() {
        if (properties.getStorage().getType() ==
                RateLimitProperties.Storage.Type.REDIS) {

            return new RedisStorage(
                    redisTemplate,
                    properties.getStorage().getRedis()
            );
        }

        return new InMemoryStorage();
    }

    @Bean
    public RateLimitAlgorithm algorithm(RateLimitStorage storage) {

        if (properties.getAlgorithm().getType() ==
                RateLimitProperties.Algorithm.Type.FIXED_WINDOW) {

            return new FixedWindowAlgorithm(storage);
        }

        return new TokenBucketAlgorithm(storage);
    }
}