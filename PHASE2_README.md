# ShopStream Phase 2 - Events & Kafka

This document outlines the Phase 2 implementation of ShopStream, which introduces Kafka as the backbone for event-driven architecture.

## What's New in Phase 2

### Infrastructure
- **Kafka + Zookeeper**: Added to docker-compose.yml for event streaming
- **Kafdrop UI**: Web interface for monitoring Kafka topics (accessible at http://localhost:9000)
- **Spring Kafka**: Added dependency and configuration for event publishing/consuming

### Event Architecture
- **Event Models**: Created `BaseEvent`, `ProductEvent`, and `OrderEvent` following the events.mdc specification
- **Product Events**: Product create/update/delete operations now publish events to `product-events` topic
- **Order Events**: Order creation uses outbox pattern for reliable event publishing to `order-events` topic
- **Event Consumers**: Simple consumers that log all received events

### Outbox Pattern Implementation
- **OutboxEvent Entity**: Database table to store events before publishing to Kafka
- **OutboxEventService**: Background service that publishes stored events every 5 seconds
- **Reliable Publishing**: Ensures events are not lost even if Kafka is temporarily unavailable

## API Endpoints

### New Test Endpoints
- `POST /api/test/kafka/hello` - Send test message to validate Kafka setup

### Existing Endpoints (Now with Events)
- Product CRUD operations now publish events
- Order creation now uses outbox pattern for event publishing

## Event Structure

All events follow the common structure defined in events.mdc:

```json
{
  "eventType": "PRODUCT_CREATED|PRODUCT_UPDATED|PRODUCT_DELETED|ORDER_CREATED",
  "timestamp": "2025-01-27T10:00:00Z",
  "payload": { ... }
}
```

### Product Events
```json
{
  "eventType": "PRODUCT_CREATED",
  "timestamp": "2025-01-27T10:00:00Z",
  "productId": "string",
  "title": "string", 
  "price": "number",
  "updatedAt": "ISO8601"
}
```

### Order Events
```json
{
  "eventType": "ORDER_CREATED",
  "timestamp": "2025-01-27T10:00:00Z",
  "orderId": "string",
  "userId": "string",
  "productId": "string",
  "quantity": "number",
  "createdAt": "ISO8601"
}
```

## Running the Application

1. **Start Infrastructure**:
   ```bash
   docker-compose up -d
   ```

2. **Access Services**:
   - Backend API: http://localhost:8080
   - Frontend: http://localhost:3000
   - Kafdrop (Kafka UI): http://localhost:9000

3. **Test Kafka Setup**:
   ```bash
   curl -X POST "http://localhost:8080/api/test/kafka/hello?message=Hello%20Kafka"
   ```

## Monitoring

- **Kafdrop UI**: Visit http://localhost:9000 to monitor Kafka topics and messages
- **Application Logs**: Check backend logs for event publishing/consuming activity
- **Topics**: 
  - `hello-kafka` - Test messages
  - `product-events` - Product domain events
  - `order-events` - Order domain events

## Next Steps (Phase 3)

- Elasticsearch integration for product search
- Caching layer with Redis
- Rate limiting implementation
- Search result caching
