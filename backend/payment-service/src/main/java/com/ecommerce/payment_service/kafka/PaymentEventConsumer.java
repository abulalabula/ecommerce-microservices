package com.ecommerce.payment_service.kafka;

import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final PaymentService paymentService;

    public PaymentEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "order-payment-requests", groupId = "payment-group")
    public void processPaymentRequest(PaymentRequestDto paymentRequest) {
        LOGGER.info("Received payment request: {}", paymentRequest);
        paymentService.processPayment(paymentRequest);
    }
}
