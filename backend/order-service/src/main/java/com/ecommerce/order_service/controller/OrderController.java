package com.ecommerce.order_service.controller;


import com.ecommerce.order_service.payload.OrderDTO;
import com.ecommerce.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

//    @PostMapping
//    public ResponseEntity<OrderDTO> confirmOrder(@RequestBody OrderDTO orderDTO) {
//        return ResponseEntity.ok(orderService.confirmOrder(orderDTO));
//    }


    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByCustomerId(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getAllOrdersByCustomerId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id) {
        OrderDTO order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable String id,  @RequestBody OrderDTO orderDTO) {
        orderService.updateOrder(id, orderDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id) {
        orderService.cancelOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
