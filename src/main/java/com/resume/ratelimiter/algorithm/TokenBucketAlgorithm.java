package com.resume.ratelimiter.algorithm;

import com.resume.ratelimiter.storage.RateLimitStorage;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class TokenBucketAlgorithm extends RateLimitAlgorithm {

    public TokenBucketAlgorithm(RateLimitStorage storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Boolean> isAllowed(String key, long windowMs, int maxRequests) {

        String bucketKey = "bucket:" + key;
        long now = System.currentTimeMillis();
        double refillRate = (double) maxRequests / windowMs;

        return storage.get(bucketKey).thenCompose(tokens -> {

            if (tokens == 0) {
                tokens = (long) maxRequests;
            }

            long newTokens = Math.min(maxRequests, tokens + 1);

            if (newTokens > 0) {
                long finalTokens = newTokens - 1;
                return storage.set(bucketKey, finalTokens, windowMs)
                        .thenApply(v -> true);
            }

            return CompletableFuture.completedFuture(false);
        });
    }

    @Override
    public CompletableFuture<Integer> getRemaining(String key, long windowMs, int maxRequests) {
        return storage.get("bucket:" + key)
                .thenApply(tokens -> tokens.intValue());
    }

    @Override
    public CompletableFuture<Void> reset(String key) {
        return storage.reset("bucket:" + key);
    }
}