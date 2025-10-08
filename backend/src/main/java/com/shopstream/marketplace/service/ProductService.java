package com.shopstream.marketplace.service;

import com.shopstream.marketplace.event.ProductEvent;
import com.shopstream.marketplace.model.Product;
import com.shopstream.marketplace.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String PRODUCT_EVENTS_TOPIC = "product-events";
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        publishProductEvent("PRODUCT_CREATED", savedProduct);
        return savedProduct;
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id.toString());
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }
    
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerIdAndIsActiveTrue(sellerId);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }
    
    public List<Product> searchProductsByTag(String tag) {
        return productRepository.findByTagsContainingAndIsActiveTrue(tag);
    }
    
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
    }
    
    public Product updateProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        publishProductEvent("PRODUCT_UPDATED", savedProduct);
        return savedProduct;
    }
    
    public void deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product productToDelete = product.get();
            productToDelete.setIsActive(false);
            Product savedProduct = productRepository.save(productToDelete);
            publishProductEvent("PRODUCT_DELETED", savedProduct);
        }
    }
    
    public void updateProductStock(String productId, Integer newStock) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setStock(newStock);
            productRepository.save(product.get());
        }
    }
    
    /**
     * Publish product events to Kafka topic.
     */
    private void publishProductEvent(String eventType, Product product) {
        try {
            ProductEvent event = new ProductEvent(
                eventType,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().doubleValue(),
                product.getCategory(),
                product.getTags() != null ? String.join(",", product.getTags()) : "",
                product.getSellerId() != null ? product.getSellerId().toString() : "unknown"
            );
            
            kafkaTemplate.send(PRODUCT_EVENTS_TOPIC, product.getId(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Successfully published {} event for product {} with offset {}", 
                            eventType, product.getId(), result.getRecordMetadata().offset());
                    } else {
                        logger.error("Failed to publish {} event for product {}: {}", 
                            eventType, product.getId(), ex.getMessage());
                    }
                });
        } catch (Exception e) {
            logger.error("Error publishing {} event for product {}: {}", eventType, product.getId(), e.getMessage());
        }
    }
}
