package com.shopstream.marketplace.controller;

import com.shopstream.marketplace.document.ProductDocument;
import com.shopstream.marketplace.service.ProductIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class ProductIndexController {

    @Autowired
    private ProductIndexService productIndexService;

    /**
     * Health check for Elasticsearch connectivity
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isAvailable = productIndexService.isElasticsearchAvailable();
            response.put("status", isAvailable ? "UP" : "DOWN");
            response.put("elasticsearch", Map.of(
                "connected", isAvailable,
                "message", isAvailable ? "Elasticsearch is available" : "Elasticsearch is not available"
            ));
            
            if (isAvailable) {
                // Try to create index if it doesn't exist
                productIndexService.createIndexIfNotExists();
                response.put("index", Map.of(
                    "created", true,
                    "message", "Products index is ready"
                ));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * Get all indexed products
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDocument>> getAllProducts() {
        try {
            List<ProductDocument> products = productIndexService.getAllActiveProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * Search products by text query
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(@RequestParam String q) {
        try {
            List<ProductDocument> products = productIndexService.searchProducts(q);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDocument>> getProductsByCategory(@PathVariable String category) {
        try {
            List<ProductDocument> products = productIndexService.getProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * Get products by seller
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDocument>> getProductsBySeller(@PathVariable String sellerId) {
        try {
            List<ProductDocument> products = productIndexService.getProductsBySeller(sellerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * Get a specific product document by ID
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDocument> getProductDocument(@PathVariable String productId) {
        try {
            return productIndexService.getProductDocument(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
