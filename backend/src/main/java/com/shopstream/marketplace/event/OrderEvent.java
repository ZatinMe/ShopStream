package com.shopstream.marketplace.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Order domain events following events.mdc specification.
 */
public class OrderEvent extends BaseEvent {
    
    @JsonProperty("orderId")
    private final String orderId;
    
    @JsonProperty("userId")
    private final String userId;
    
    @JsonProperty("productId")
    private final String productId;
    
    @JsonProperty("quantity")
    private final Integer quantity;
    
    @JsonProperty("createdAt")
    private final String createdAt;
    
    public OrderEvent(String eventType, String orderId, String userId, String productId, Integer quantity, Instant createdAt) {
        super(eventType);
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt.toString();
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
}
