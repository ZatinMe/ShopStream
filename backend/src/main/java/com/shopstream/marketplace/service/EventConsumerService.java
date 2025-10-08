package com.shopstream.marketplace.service;

import com.shopstream.marketplace.event.OrderEvent;
import com.shopstream.marketplace.event.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service to consume and log domain events from Kafka topics.
 */
@Service
public class EventConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventConsumerService.class);
    
    /**
     * Consumer for product events.
     */
    @KafkaListener(topics = "product-events", groupId = "shopstream-group")
    public void consumeProductEvent(ProductEvent productEvent) {
        logger.info("Received Product Event: {} - Product ID: {}, Name: {}, Price: {}, Timestamp: {}", 
            productEvent.getEventType(), 
            productEvent.getProductId(), 
            productEvent.getProductName(), 
            productEvent.getPrice(), 
            productEvent.getTimestamp());
    }
    
    /**
     * Consumer for order events.
     */
    @KafkaListener(topics = "order-events", groupId = "shopstream-group")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        logger.info("Received Order Event: {} - Order ID: {}, User ID: {}, Product ID: {}, Quantity: {}, Created At: {}", 
            orderEvent.getEventType(), 
            orderEvent.getOrderId(), 
            orderEvent.getUserId(), 
            orderEvent.getProductId(), 
            orderEvent.getQuantity(), 
            orderEvent.getCreatedAt());
    }
}
