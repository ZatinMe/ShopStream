package com.shopstream.marketplace.controller;

import com.shopstream.marketplace.service.KafkaTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for Kafka validation.
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class KafkaTestController {
    
    @Autowired
    private KafkaTestService kafkaTestService;
    
    /**
     * Simple test endpoint without authentication
     */
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Kafka test controller is working");
        response.put("timestamp", Instant.now().toString());
        return response;
    }
    
    /**
     * Send a test message to Kafka to validate the setup.
     */
    @PostMapping("/kafka/hello")
    public Map<String, Object> sendHelloKafka(@RequestParam(defaultValue = "Hello from ShopStream!") String message) {
        try {
            kafkaTestService.sendTestMessage(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test message sent to Kafka");
            response.put("timestamp", Instant.now().toString());
            response.put("topic", "hello-kafka");
            
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", Instant.now().toString());
            
            return response;
        }
    }
}
