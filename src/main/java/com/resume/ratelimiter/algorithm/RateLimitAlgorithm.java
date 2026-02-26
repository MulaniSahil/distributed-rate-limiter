package com.resume.ratelimiter.algorithm;

import com.resume.ratelimiter.storage.RateLimitStorage;
import lombok.RequiredArgsConstructor;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class RateLimitAlgorithm {

    protected final RateLimitStorage storage;

    public abstract CompletableFuture<Boolean> isAllowed(String key, long windowMs, int maxRequests);

    public abstract CompletableFuture<Integer> getRemaining(String key, long windowMs, int maxRequests);

    public abstract CompletableFuture<Void> reset(String key);
}