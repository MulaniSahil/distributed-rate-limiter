package com.resume.ratelimiter.storage;

import java.util.concurrent.CompletableFuture;

public interface RateLimitStorage {

    CompletableFuture<Long> get(String key);

    CompletableFuture<Void> set(String key, long value, long ttlMs);

    CompletableFuture<Long> increment(String key);

    CompletableFuture<Void> reset(String key);
}