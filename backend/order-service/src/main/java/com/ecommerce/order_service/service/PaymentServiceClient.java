package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderPrimaryKey;
import com.ecommerce.order_service.payload.PaymentRequestDto;
import com.ecommerce.order_service.payload.RefundRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentServiceClient {

    private final WebClient webClient;

    public PaymentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://payment-service").build();
    }

    public void initiatePayment(Order order, BigDecimal totalAmount) {
        OrderPrimaryKey key = order.getKey();
        PaymentRequestDto requestDTO = new PaymentRequestDto(
                key.getUserId() + "-" + key.getCreatedAt() + "-" + key.getOrderId(),
                UUID.fromString(key.getOrderId()),
                Long.parseLong(key.getUserId()),
                key.getOrderId(),
                totalAmount,
                "USD",
                "CARD",
                String.format("%04d", new Random().nextInt(10000)),
                new Random().nextBoolean());

        webClient.post()
                .uri("/api/payments/process")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class) // No response expected
                .subscribe(); // Fire-and-forget (non-blocking)
    }

    public void initiateRefund(Order order, BigDecimal refundAmount) {
        OrderPrimaryKey key = order.getKey();

        RefundRequestDto requestDTO = new RefundRequestDto(
                key.getUserId() + "-" + key.getCreatedAt() + "-" + key.getOrderId(),                1L,
                refundAmount);

        webClient.post()
                .uri("/api/payments/refund")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class) // No response expected
                .subscribe(); // Fire-and-forget (non-blocking)
    }
}