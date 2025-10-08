package com.shopstream.marketplace.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ProductEvent {
    
    @JsonProperty("eventType")
    private String eventType; // CREATED, UPDATED, DELETED
    
    @JsonProperty("productId")
    private String productId;
    
    @JsonProperty("productName")
    private String productName;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("tags")
    private String tags;
    
    @JsonProperty("sellerId")
    private String sellerId;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonProperty("version")
    private Integer version;

    // Default constructor
    public ProductEvent() {
        this.timestamp = LocalDateTime.now();
        this.version = 1;
    }

    // Constructor for product creation
    public ProductEvent(String eventType, String productId, String productName, String description, 
                       Double price, String category, String sellerId) {
        this();
        this.eventType = eventType;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerId = sellerId;
    }

    // Constructor for product update
    public ProductEvent(String eventType, String productId, String productName, String description, 
                       Double price, String category, String tags, String sellerId) {
        this(eventType, productId, productName, description, price, category, sellerId);
        this.tags = tags;
    }

    // Constructor for product deletion
    public ProductEvent(String eventType, String productId) {
        this();
        this.eventType = eventType;
        this.productId = productId;
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ProductEvent{" +
                "eventType='" + eventType + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", tags='" + tags + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", timestamp=" + timestamp +
                ", version=" + version +
                '}';
    }
}