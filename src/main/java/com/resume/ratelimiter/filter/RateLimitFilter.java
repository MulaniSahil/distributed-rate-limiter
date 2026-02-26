package com.resume.ratelimiter.filter;

import com.resume.ratelimiter.algorithm.RateLimitAlgorithm;
import com.resume.ratelimiter.config.RateLimitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimitAlgorithm algorithm;
    private final RateLimitProperties properties;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        if (request.getRequestURI().contains("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String key = generateKey(request);
        
        try {
            boolean allowed = algorithm.isAllowed(key, 
                properties.getAlgorithm().getWindowMs(), 
                properties.getAlgorithm().getMaxRequests())
                .get();
            
            int remaining = algorithm.getRemaining(key, 
                properties.getAlgorithm().getWindowMs(), 
                properties.getAlgorithm().getMaxRequests())
                .get();
            
            response.setHeader("X-RateLimit-Limit", 
                String.valueOf(properties.getAlgorithm().getMaxRequests()));
            response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
            
            if (!allowed) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setHeader("Retry-After", 
                    String.valueOf(properties.getAlgorithm().getWindowMs() / 1000));
                return;
            }
            
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("Rate limiting error", e);
            filterChain.doFilter(request, response);
        }
    }
    
    private String generateKey(HttpServletRequest request) {
        String path = request.getRequestURI();
        String ip = getClientIp(request);
        return path + ":" + ip;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0];
    }
}
