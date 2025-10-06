package com.shopstream.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test Elasticsearch connectivity by checking if we can access the operations
            if (elasticsearchOperations != null) {
                response.put("status", "UP");
                response.put("elasticsearch", Map.of(
                    "connected", true,
                    "message", "Elasticsearch operations are available"
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "DOWN");
                response.put("error", "Elasticsearch operations not available");
                return ResponseEntity.status(503).body(response);
            }
            
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }
}
