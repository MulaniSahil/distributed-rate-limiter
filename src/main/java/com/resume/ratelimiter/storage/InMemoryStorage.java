package com.resume.ratelimiter.storage;

import org.springframework.stereotype.Component;
import java.util.concurrent.*;


public class InMemoryStorage implements RateLimitStorage {

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor =
            Executors.newSingleThreadScheduledExecutor();

    public InMemoryStorage() {
        cleanupExecutor.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public CompletableFuture<Long> get(String key) {
        Entry entry = store.get(key);
        if (entry == null || entry.expiresAt < System.currentTimeMillis()) {
            store.remove(key);
            return CompletableFuture.completedFuture(0L);
        }
        return CompletableFuture.completedFuture(entry.value);
    }

    @Override
    public CompletableFuture<Void> set(String key, long value, long ttlMs) {
        long expiresAt = System.currentTimeMillis() + ttlMs;
        store.put(key, new Entry(value, expiresAt));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Long> increment(String key) {
        return get(key).thenApply(current -> {
            long newValue = current + 1;
            store.computeIfPresent(key, (k, v) -> {
                v.value = newValue;
                return v;
            });
            return newValue;
        });
    }

    @Override
    public CompletableFuture<Void> reset(String key) {
        store.remove(key);
        return CompletableFuture.completedFuture(null);
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(e -> e.getValue().expiresAt < now);
    }

    private static class Entry {
        long value;
        long expiresAt;

        Entry(long value, long expiresAt) {
            this.value = value;
            this.expiresAt = expiresAt;
        }
    }
}