package com.resume.ratelimiter.algorithm;

import com.resume.ratelimiter.storage.RateLimitStorage;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class FixedWindowAlgorithm extends RateLimitAlgorithm {

    public FixedWindowAlgorithm(RateLimitStorage storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Boolean> isAllowed(String key, long windowMs, int maxRequests) {

        String windowKey = "window:" + key;

        return storage.get(windowKey).thenCompose(current -> {

            if (current == 0) {
                return storage.set(windowKey, 1L, windowMs)
                        .thenApply(v -> true);
            }

            if (current < maxRequests) {
                return storage.increment(windowKey)
                        .thenApply(v -> true);
            }

            return CompletableFuture.completedFuture(false);
        });
    }

    @Override
    public CompletableFuture<Integer> getRemaining(String key, long windowMs, int maxRequests) {
        return storage.get("window:" + key)
                .thenApply(current -> Math.max(0, maxRequests - current.intValue()));
    }

    @Override
    public CompletableFuture<Void> reset(String key) {
        return storage.reset("window:" + key);
    }
}