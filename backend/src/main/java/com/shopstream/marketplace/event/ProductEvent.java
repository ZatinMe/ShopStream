package com.shopstream.marketplace.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Product domain events following events.mdc specification.
 */
public class ProductEvent extends BaseEvent {
    
    @JsonProperty("productId")
    private final String productId;
    
    @JsonProperty("title")
    private final String title;
    
    @JsonProperty("price")
    private final BigDecimal price;
    
    @JsonProperty("updatedAt")
    private final String updatedAt;
    
    public ProductEvent(String eventType, String productId, String title, BigDecimal price, Instant updatedAt) {
        super(eventType);
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.updatedAt = updatedAt.toString();
    }
    
    public String getProductId() {
        return productId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
}
