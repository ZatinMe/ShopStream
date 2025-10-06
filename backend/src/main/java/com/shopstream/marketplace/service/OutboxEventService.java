package com.shopstream.marketplace.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstream.marketplace.event.OrderEvent;
import com.shopstream.marketplace.model.OutboxEvent;
import com.shopstream.marketplace.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for handling outbox pattern - stores events in database first, then publishes to Kafka.
 */
@Service
public class OutboxEventService {
    
    private static final Logger logger = LoggerFactory.getLogger(OutboxEventService.class);
    private static final String ORDER_EVENTS_TOPIC = "order-events";
    
    @Autowired
    private OutboxEventRepository outboxEventRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Store an order event in the outbox table.
     */
    @Transactional
    public void storeOrderEvent(OrderEvent orderEvent) {
        try {
            String payload = objectMapper.writeValueAsString(orderEvent);
            OutboxEvent outboxEvent = new OutboxEvent(
                orderEvent.getEventType(),
                orderEvent.getOrderId(),
                "Order",
                payload
            );
            outboxEventRepository.save(outboxEvent);
            logger.info("Stored outbox event for order {}: {}", orderEvent.getOrderId(), orderEvent.getEventType());
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize order event: {}", e.getMessage());
        }
    }
    
    /**
     * Background process to publish unpublished events to Kafka.
     * Runs every 5 seconds.
     */
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void publishUnpublishedEvents() {
        List<OutboxEvent> unpublishedEvents = outboxEventRepository.findUnpublishedEvents();
        
        for (OutboxEvent event : unpublishedEvents) {
            try {
                // Publish to appropriate topic based on aggregate type
                String topic = getTopicForAggregateType(event.getAggregateType());
                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            event.setPublished(true);
                            outboxEventRepository.save(event);
                            logger.info("Successfully published outbox event {} with offset {}", 
                                event.getId(), result.getRecordMetadata().offset());
                        } else {
                            logger.error("Failed to publish outbox event {}: {}", event.getId(), ex.getMessage());
                        }
                    });
            } catch (Exception e) {
                logger.error("Error processing outbox event {}: {}", event.getId(), e.getMessage());
            }
        }
    }
    
    private String getTopicForAggregateType(String aggregateType) {
        switch (aggregateType) {
            case "Order":
                return ORDER_EVENTS_TOPIC;
            default:
                return "unknown-events";
        }
    }
}
