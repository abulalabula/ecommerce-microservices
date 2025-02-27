package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.PaymentEvent;
import com.ecommerce.order_service.payload.OrderRequestDTO;

public interface OrderService {
    Order createOrder(OrderRequestDTO orderRequestDTO);
    Order cancelOrder(String userId, String orderId);
    Order updateOrder(String orderId, OrderRequestDTO orderRequestDTO);
    Order getOrderById(String userId, String orderId);

    void processPaymentResponse(PaymentEvent response);

}