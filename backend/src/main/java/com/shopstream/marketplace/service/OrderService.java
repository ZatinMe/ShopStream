package com.shopstream.marketplace.service;

import com.shopstream.marketplace.model.Order;
import com.shopstream.marketplace.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public Order createOrder(Order order) {
        return orderRepository.save(order);
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
