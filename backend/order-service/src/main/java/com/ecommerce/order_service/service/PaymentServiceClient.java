package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderPrimaryKey;
import com.ecommerce.order_service.payload.PaymentRequestDto;
import com.ecommerce.order_service.payload.RefundRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentServiceClient {

    private final WebClient webClient;

    public PaymentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
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
                .doOnSuccess(v -> System.out.println("Payment request sent successfully"))
                .doOnError(error -> System.err.println("Payment request failed: " + error.getMessage()))
                .subscribe(); // Fire-and-forget (non-blocking)
    }

    public void initiateRefund(Order order, BigDecimal refundAmount) {
        System.out.println("initiating refund");
        OrderPrimaryKey key = order.getKey();

        RefundRequestDto requestDTO = new RefundRequestDto(
                "3019-2025-02-28T13:37:27.441787-a55e9b57-aca8-4e1b-803d-7524ebe86703",
                12L,
                refundAmount);

        webClient.post()
                .uri("/api/payments/refund")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(Void.class) // No response expected
                .doOnSuccess(v -> System.out.println("Refund request sent successfully"))
                .doOnError(error -> System.err.println("Refund request failed: " + error.getMessage()))

                .subscribe(); // Fire-and-forget (non-blocking)
    }
}
