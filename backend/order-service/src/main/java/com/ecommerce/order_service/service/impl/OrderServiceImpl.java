package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dao.OrderRepository;
import com.ecommerce.order_service.entity.*;
import com.ecommerce.order_service.kafka.OrderEventProducer;
import com.ecommerce.order_service.payload.OrderRequestDTO;
import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.service.PaymentServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentServiceClient paymentServiceClient;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentServiceClient paymentServiceClient) {
        this.orderRepository = orderRepository;
        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        LocalDateTime now = LocalDateTime.now();
        OrderPrimaryKey key = new OrderPrimaryKey(orderRequestDTO.getUserId(), now, UUID.randomUUID().toString());

        Order order = new Order(
                key,
                OrderStatus.CREATED.toString(),
                PaymentStatus.PENDING.toString(),
                orderRequestDTO.getItems().size(),
                BigDecimal.ZERO.doubleValue(),
                orderRequestDTO.getDetails(),
                orderRequestDTO.getItems(),
                now
        );

        orderRepository.save(order);

        // Validate and charge payment
        paymentServiceClient.initiatePayment(order, new BigDecimal(order.getTotalAmount()));

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order cancelOrder(String userId, String orderId) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        Order order = orderOpt.get();
        if (order.getOrderStatus().equals(OrderStatus.PAID.toString())) {
            order.setOrderStatus(OrderStatus.CANCELED.toString());
            order.setPaymentStatus(PaymentStatus.PENDING.toString());
            paymentServiceClient.initiateRefund(order, BigDecimal.valueOf(order.getTotalAmount()));
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order cannot be canceled");
        }
        return order;
    }

    @Override
    public Order updateOrder(String orderId, OrderRequestDTO orderRequestDTO) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(orderRequestDTO.getUserId(), orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        Order order = orderOpt.get();
        order.setOrderStatus(OrderStatus.CREATED.toString());
        order.setPaymentStatus(PaymentStatus.PENDING.toString());
        order.setTotalQuantity(orderRequestDTO.getItems().size());
        order.setTotalAmount(0);
        order.setDetails(orderRequestDTO.getDetails());
        order.setItems(orderRequestDTO.getItems());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(String userId, String orderId) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");
        return orderOpt.get();
    }

    @Override
    public void processPaymentResponse(PaymentEvent event) {
        String orderId = event.getOrderId();
        String userId = event.getUserId().toString();
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) return;

        Order order = orderOpt.get();
        if (event.getEventType().equals("payment_successed")) {
            order.setOrderStatus(OrderStatus.PAID.toString());
            order.setPaymentStatus(PaymentStatus.COMPLETED.toString());
        } else if (event.getEventType().equals("payment_authorization_failed")) {
            order.setOrderStatus(OrderStatus.FAILED.toString());
            order.setPaymentStatus(PaymentStatus.FAILED.toString());
        } else if (event.getEventType().equals("payment_refunded")) {
            order.setOrderStatus(OrderStatus.CANCELCOMPLETED.toString());
            order.setPaymentStatus(PaymentStatus.REFUNDED.toString());
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}