package com.ecommerce.payment_service.event;

import java.time.Instant;

public class PaymentEvent {
    private String eventType;
    private Long userId;
    private String itemId;
    private String orderId;
    private String paymentCardLast4;
    private Instant paymentTimestamp;

    // Constructors
    public PaymentEvent() {}

    public PaymentEvent(String eventType, Long userId, String itemId, String orderId,
                        String paymentCardLast4, Instant paymentTimestamp) {
        this.eventType = eventType;
        this.userId = userId;
        this.itemId = itemId;
        this.orderId = orderId;
        this.paymentCardLast4 = paymentCardLast4;
        this.paymentTimestamp = paymentTimestamp;
    }

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaymentCardLast4() { return paymentCardLast4; }
    public void setPaymentCardLast4(String paymentCardLast4) { this.paymentCardLast4 = paymentCardLast4; }

    public Instant getPaymentTimestamp() { return paymentTimestamp; }
    public void setPaymentTimestamp(Instant paymentTimestamp) { this.paymentTimestamp = paymentTimestamp; }
}
