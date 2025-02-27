package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.payload.OrderRequestDTO;
import com.ecommerce.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.createOrder(orderRequestDTO);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.internalServerError().build();
    }

    @PutMapping("cancel/{userId}/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable String userId, @PathVariable String orderId) {
        Order canceledOrder = orderService.cancelOrder(userId, orderId);
        return canceledOrder != null ? ResponseEntity.ok(canceledOrder) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId,  @RequestBody OrderRequestDTO orderRequestDTO) {
        Order updatedOrder = orderService.updateOrder(orderId, orderRequestDTO);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }


    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String userId, @PathVariable String orderId) {
        Order order = orderService.getOrderById(userId, orderId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }
}
