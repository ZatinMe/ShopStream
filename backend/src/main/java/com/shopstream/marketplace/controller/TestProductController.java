package com.shopstream.marketplace.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/test-products")
@CrossOrigin(origins = "*")
public class TestProductController {
    
    private static final List<Map<String, Object>> PRODUCTS = new ArrayList<>();
    
    static {
        // Add some sample products
        Map<String, Object> product1 = new HashMap<>();
        product1.put("id", "1");
        product1.put("name", "MacBook Pro 16-inch");
        product1.put("description", "Apple MacBook Pro with M2 Pro chip, 16GB RAM, 512GB SSD");
        product1.put("price", 2499.99);
        product1.put("stock", 5);
        product1.put("category", "Electronics");
        product1.put("tags", Arrays.asList("laptop", "apple", "macbook", "computer"));
        product1.put("sellerId", 1);
        PRODUCTS.add(product1);
        
        Map<String, Object> product2 = new HashMap<>();
        product2.put("id", "2");
        product2.put("name", "iPhone 15 Pro");
        product2.put("description", "Latest iPhone with A17 Pro chip, 256GB storage");
        product2.put("price", 999.99);
        product2.put("stock", 10);
        product2.put("category", "Electronics");
        product2.put("tags", Arrays.asList("phone", "apple", "iphone", "mobile"));
        product2.put("sellerId", 1);
        PRODUCTS.add(product2);
        
        Map<String, Object> product3 = new HashMap<>();
        product3.put("id", "3");
        product3.put("name", "Sony WH-1000XM5 Headphones");
        product3.put("description", "Premium noise-canceling wireless headphones");
        product3.put("price", 399.99);
        product3.put("stock", 8);
        product3.put("category", "Electronics");
        product3.put("tags", Arrays.asList("headphones", "sony", "wireless", "audio"));
        product3.put("sellerId", 1);
        PRODUCTS.add(product3);
        
        Map<String, Object> product4 = new HashMap<>();
        product4.put("id", "4");
        product4.put("name", "Nike Air Max 270");
        product4.put("description", "Comfortable running shoes with Air Max technology");
        product4.put("price", 150.00);
        product4.put("stock", 15);
        product4.put("category", "Fashion");
        product4.put("tags", Arrays.asList("shoes", "nike", "running", "sports"));
        product4.put("sellerId", 1);
        PRODUCTS.add(product4);
        
        Map<String, Object> product5 = new HashMap<>();
        product5.put("id", "5");
        product5.put("name", "Coffee Maker Deluxe");
        product5.put("description", "Programmable coffee maker with 12-cup capacity");
        product5.put("price", 89.99);
        product5.put("stock", 12);
        product5.put("category", "Home & Kitchen");
        product5.put("tags", Arrays.asList("coffee", "kitchen", "appliance", "home"));
        product5.put("sellerId", 1);
        PRODUCTS.add(product5);
    }
    
    @GetMapping
    public List<Map<String, Object>> getAllProducts() {
        return PRODUCTS;
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable String id) {
        return PRODUCTS.stream()
                .filter(product -> product.get("id").equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @PostMapping
    public Map<String, Object> createProduct(@RequestBody Map<String, Object> product) {
        String newId = String.valueOf(PRODUCTS.size() + 1);
        product.put("id", newId);
        PRODUCTS.add(product);
        return product;
    }
}
