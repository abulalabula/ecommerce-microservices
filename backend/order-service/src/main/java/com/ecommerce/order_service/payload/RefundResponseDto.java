package com.ecommerce.order_service.payload;

import com.ecommerce.order_service.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponseDto {

    private String transactionId;
    private Long originalPaymentId;
    private BigDecimal refundAmount;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    // Constructor
    public RefundResponseDto() {}

    public RefundResponseDto(String transactionId, Long originalPaymentId, BigDecimal refundAmount,
                             PaymentStatus status, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.originalPaymentId = originalPaymentId;
        this.refundAmount = refundAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Long getOriginalPaymentId() { return originalPaymentId; }
    public void setOriginalPaymentId(Long originalPaymentId) { this.originalPaymentId = originalPaymentId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
