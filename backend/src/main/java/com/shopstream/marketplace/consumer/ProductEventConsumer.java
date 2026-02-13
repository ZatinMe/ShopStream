package com.shopstream.marketplace.consumer;

import com.shopstream.marketplace.event.ProductEvent;
import com.shopstream.marketplace.model.Product;
import com.shopstream.marketplace.service.ProductIndexService;
import com.shopstream.marketplace.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductEventConsumer.class);
    
    @Autowired
    private ProductIndexService productIndexService;
    
    @Autowired
    private ProductService productService;

    /**
     * Consume product events from Kafka and index them in Elasticsearch
     */
    @KafkaListener(topics = "product-events", groupId = "shopstream-elasticsearch-group")
    public void handleProductEvent(
            @Payload ProductEvent productEvent,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        logger.info("Received product event: {} from topic: {}, partition: {}, offset: {}", 
                   productEvent, topic, partition, offset);
        
        try {
            switch (productEvent.getEventType().toUpperCase()) {
                case "CREATED":
                    handleProductCreated(productEvent);
                    break;
                case "UPDATED":
                    handleProductUpdated(productEvent);
                    break;
                case "DELETED":
                    handleProductDeleted(productEvent);
                    break;
                default:
                    logger.warn("Unknown event type: {}", productEvent.getEventType());
            }
        } catch (Exception e) {
            logger.error("Failed to process product event: {}", productEvent, e);
            // In production, you might want to implement retry logic or dead letter queue
        }
    }

    /**
     * Handle product creation event
     */
    private void handleProductCreated(ProductEvent productEvent) {
        logger.info("Processing product creation event for product: {}", productEvent.getProductId());
        
        try {
            // Get the full product from database
            Optional<Product> productOpt = productService.getProductById(Long.parseLong(productEvent.getProductId()));
            
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                productIndexService.indexProduct(product);
                logger.info("Successfully indexed new product: {}", product.getId());
            } else {
                logger.warn("Product not found in database for event: {}", productEvent.getProductId());
            }
        } catch (Exception e) {
            logger.error("Failed to handle product creation event: {}", productEvent.getProductId(), e);
            throw e;
        }
    }

    /**
     * Handle product update event
     */
    private void handleProductUpdated(ProductEvent productEvent) {
        logger.info("Processing product update event for product: {}", productEvent.getProductId());
        
        try {
            // Get the updated product from database
            Optional<Product> productOpt = productService.getProductById(Long.parseLong(productEvent.getProductId()));
            
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                productIndexService.updateProduct(product);
                logger.info("Successfully updated product in index: {}", product.getId());
            } else {
                logger.warn("Product not found in database for update event: {}", productEvent.getProductId());
            }
        } catch (Exception e) {
            logger.error("Failed to handle product update event: {}", productEvent.getProductId(), e);
            throw e;
        }
    }

    /**
     * Handle product deletion event
     */
    private void handleProductDeleted(ProductEvent productEvent) {
        logger.info("Processing product deletion event for product: {}", productEvent.getProductId());
        
        try {
            productIndexService.deleteProduct(productEvent.getProductId());
            logger.info("Successfully deleted product from index: {}", productEvent.getProductId());
        } catch (Exception e) {
            logger.error("Failed to handle product deletion event: {}", productEvent.getProductId(), e);
            throw e;
        }
    }

    /**
     * Health check method to verify consumer is working
     */
    @KafkaListener(topics = "product-events-health", groupId = "shopstream-elasticsearch-group")
    public void handleHealthCheck(@Payload String message) {
        logger.info("Received health check message: {}", message);
    }
}
