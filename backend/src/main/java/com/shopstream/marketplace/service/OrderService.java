package com.shopstream.marketplace.service;

import com.shopstream.marketplace.event.OrderEvent;
import com.shopstream.marketplace.model.Order;
import com.shopstream.marketplace.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OutboxEventService outboxEventService;
    
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        
        // For simplicity, we'll create an event for the first order item
        // In a real scenario, you might want to create separate events for each item
        if (savedOrder.getOrderItems() != null && !savedOrder.getOrderItems().isEmpty()) {
            var firstItem = savedOrder.getOrderItems().get(0);
            OrderEvent orderEvent = new OrderEvent(
                "ORDER_CREATED",
                savedOrder.getId().toString(),
                savedOrder.getUserId().toString(),
                firstItem.getProductId(),
                firstItem.getQuantity(),
                savedOrder.getCreatedAt() != null ? savedOrder.getCreatedAt().atZone(java.time.ZoneOffset.UTC).toInstant() : Instant.now()
            );
            outboxEventService.storeOrderEvent(orderEvent);
        }
        
        return savedOrder;
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Order> getOrdersByUserIdAndStatus(Long userId, Order.OrderStatus status) {
        return orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setStatus(status);
            orderRepository.save(order.get());
        }
    }
    
    public void updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setPaymentStatus(paymentStatus);
            orderRepository.save(order.get());
        }
    }
    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
