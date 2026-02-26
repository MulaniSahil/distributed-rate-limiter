package com.resume.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }
    
    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "This is a rate-limited endpoint";
    }
    
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin endpoint with different limits";
    }
}
