package com.ecommerce.payment_service.payload;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
public class PaymentRequestDto {

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Item ID is required")
    private String itemId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Card last 4 digits are required")
    @Size(min = 4, max = 4, message = "Card number must be last 4 digits")
    private String cardLast4;

    private boolean isNewCard;

    // Constructor
    public PaymentRequestDto() {}

    public PaymentRequestDto(String transactionId, UUID orderId, Long userId, String itemId,
                             BigDecimal amount, String currency, String paymentMethod,
                             String cardLast4, boolean isNewCard) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.userId = userId;
        this.itemId = itemId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.cardLast4 = cardLast4;
        this.isNewCard = isNewCard;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCardLast4() { return cardLast4; }
    public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }

    public boolean isNewCard() { return isNewCard; }
    public void setNewCard(boolean newCard) { isNewCard = newCard; }
}