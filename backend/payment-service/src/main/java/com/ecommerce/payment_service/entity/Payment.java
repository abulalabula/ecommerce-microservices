package com.ecommerce.payment_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
//import lombok.*;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = "transaction_id"))
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId; // Ensures idempotency

    @Column(nullable = false)
    private UUID orderId;  // Matches OrderService's UUID type

    @Column(nullable = false)
    private Long userId;  // Matches AccountService's user ID type

    @Column(nullable = false)
    private String itemId;  // Matches OrderService Item ID

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;  // PENDING, COMPLETED, FAILED, REFUNDED

    @Column(nullable = false, length = 50)
    private String paymentMethod;  // E.g., "VISA", "PayPal"

    @Column(nullable = true, length = 4)
    private String cardLast4; // Last 4 digits of card from `AccountService.PaymentMethod`

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_payment_id", referencedColumnName = "id", nullable = true)
    private Payment originalPayment; // Links to the original charge for refunds

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Payment() {}

    public Payment(String transactionId, UUID orderId, Long userId, String itemId,
                   BigDecimal amount, String currency, PaymentStatus status,
                   String paymentMethod, String cardLast4, Payment originalPayment) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.userId = userId;
        this.itemId = itemId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.cardLast4 = cardLast4;
        this.originalPayment = originalPayment;
    }

    // run before entity insert to set default val
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    // run before entity update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCardLast4() { return cardLast4; }
    public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }

    public Payment getOriginalPayment() { return originalPayment; }
    public void setOriginalPayment(Payment originalPayment) { this.originalPayment = originalPayment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
