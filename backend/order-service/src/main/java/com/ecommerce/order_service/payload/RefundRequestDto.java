package com.ecommerce.order_service.payload;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RefundRequestDto {

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Original Payment ID is required")
    private Long originalPaymentId;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than zero")
    private BigDecimal refundAmount;

    // Constructor
    public RefundRequestDto() {}

    public RefundRequestDto(String transactionId, Long originalPaymentId, BigDecimal refundAmount) {
        this.transactionId = transactionId;
        this.originalPaymentId = originalPaymentId;
        this.refundAmount = refundAmount;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Long getOriginalPaymentId() { return originalPaymentId; }
    public void setOriginalPaymentId(Long originalPaymentId) { this.originalPaymentId = originalPaymentId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
}