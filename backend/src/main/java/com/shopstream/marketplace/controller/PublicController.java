package com.shopstream.marketplace.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public")
public class PublicController {
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "ShopStream Marketplace API");
        response.put("version", "1.0.0");
        return response;
    }
    
    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> response = new HashMap<>();
        response.put("name", "ShopStream Marketplace");
        response.put("description", "AI-powered marketplace backend");
        response.put("phase", "Phase 1 - Core Services");
        return response;
    }
}
