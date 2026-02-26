package com.resume.ratelimiter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimitProperties {

    private Storage storage = new Storage();
    private Algorithm algorithm = new Algorithm();

    @Data
    public static class Storage {
        private Type type = Type.MEMORY;
        private Redis redis = new Redis();

        public enum Type { MEMORY, REDIS }

        @Data
        public static class Redis {
            private String host = "localhost";
            private int port = 6379;
            private String prefix = "rate_limit:";
        }
    }

    @Data
    public static class Algorithm {
        private Type type = Type.TOKEN_BUCKET;
        private long windowMs = 60000;
        private int maxRequests = 100;

        public enum Type { TOKEN_BUCKET, FIXED_WINDOW }
    }
}