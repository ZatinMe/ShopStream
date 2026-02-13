package com.shopstream.marketplace.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Base event structure that all domain events must follow.
 * Based on events.mdc specification.
 */
public abstract class BaseEvent {
    
    @JsonProperty("eventType")
    private final String eventType;
    
    @JsonProperty("timestamp")
    private final String timestamp;
    
    public BaseEvent(String eventType) {
        this.eventType = eventType;
        this.timestamp = Instant.now().toString();
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}
