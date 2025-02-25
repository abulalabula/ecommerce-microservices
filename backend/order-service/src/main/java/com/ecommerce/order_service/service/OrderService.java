package com.ecommerce.order_service.service;

import com.ecommerce.order_service.payload.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrdersByCustomerId(String id);
    OrderDTO getOrderById(String id);
    long cancelOrderById(String id);
    void updateOrder(String id, OrderDTO orderDTO);
}
