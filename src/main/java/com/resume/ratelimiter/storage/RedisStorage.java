package com.resume.ratelimiter.storage;

import com.resume.ratelimiter.config.RateLimitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;


@RequiredArgsConstructor
public class RedisStorage implements RateLimitStorage {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties.Storage.Redis config;

    private String key(String k) {
        return config.getPrefix() + k;
    }

    @Override
    public CompletableFuture<Long> get(String key) {
        return CompletableFuture.supplyAsync(() -> {
            String value = redisTemplate.opsForValue().get(key(key));
            return value != null ? Long.parseLong(value) : 0L;
        });
    }

    @Override
    public CompletableFuture<Void> set(String key, long value, long ttlMs) {
        return CompletableFuture.runAsync(() ->
                redisTemplate.opsForValue().set(key(key),
                        String.valueOf(value),
                        ttlMs,
                        java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    @Override
    public CompletableFuture<Long> increment(String key) {
        return CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForValue().increment(key(key)));
    }

    @Override
    public CompletableFuture<Void> reset(String key) {
        return CompletableFuture.runAsync(() ->
                redisTemplate.delete(key(key)));
    }
}