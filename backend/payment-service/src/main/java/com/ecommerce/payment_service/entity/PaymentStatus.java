package com.ecommerce.payment_service.entity;

public enum PaymentStatus {
    PENDING,    // Payment initiated, waiting for confirmation
    COMPLETED,  // Payment Successful
    FAILED,     // Payment authorization failed
    REFUNDED    // Payment refunded
}
