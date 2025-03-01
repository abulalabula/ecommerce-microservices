package com.ecommerce.order_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private UUID id;
    private String orderId;
    private String userId;
    private String orderStatus; // CREATED, PENDING, CONFIRMED, CANCELED, SHIPPED, DELIVERED
    private LocalDateTime createdAt;
}
