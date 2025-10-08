package com.shopstream.marketplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Test service to validate Kafka setup with hello-kafka messages.
 */
@Service
public class KafkaTestService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaTestService.class);
    private static final String TEST_TOPIC = "hello-kafka";
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Send a test message to validate Kafka producer setup.
     */
    public void sendTestMessage(String message) {
        logger.info("Sending test message to topic '{}': {}", TEST_TOPIC, message);
        
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TEST_TOPIC, message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Successfully sent message=[{}] with offset=[{}]", 
                    message, result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send message=[{}] due to: {}", message, ex.getMessage());
            }
        });
    }
    
    /**
     * Consumer for test messages to validate Kafka consumer setup.
     */
    @KafkaListener(topics = TEST_TOPIC, groupId = "test-group")
    public void consumeTestMessage(String message) {
        logger.info("Received test message: {}", message);
    }
}
