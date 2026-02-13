package com.shopstream.marketplace.controller;

import com.shopstream.marketplace.model.Order;
import com.shopstream.marketplace.service.OrderService;
import com.shopstream.marketplace.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrders(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Order> orders = orderService.getOrdersByUserId(userPrincipal.getId());
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Optional<Order> order = orderService.getOrderById(id);
        
        if (order.isPresent() && (order.get().getUserId().equals(userPrincipal.getId()) || 
            userPrincipal.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))) {
            return ResponseEntity.ok(order.get());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        order.setUserId(userPrincipal.getId());
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order, 
                                           Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Optional<Order> existingOrder = orderService.getOrderById(id);
        
        if (existingOrder.isPresent() && (existingOrder.get().getUserId().equals(userPrincipal.getId()) || 
            userPrincipal.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))) {
            order.setId(id);
            Order updatedOrder = orderService.updateOrder(order);
            return ResponseEntity.ok(updatedOrder);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long id, @RequestParam Order.PaymentStatus paymentStatus) {
        orderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
