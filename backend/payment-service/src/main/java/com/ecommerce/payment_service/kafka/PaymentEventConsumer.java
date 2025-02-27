package com.ecommerce.payment_service.kafka;

import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public PaymentEventConsumer(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-payment-requests", groupId = "payment-group",
            containerFactory = "paymentRequestKafkaListenerContainerFactory")
    public void processPaymentRequest(ConsumerRecord<String, String> record) {
        if (record.value() == null || record.value().isEmpty()) {
            LOGGER.error("Error: Received empty or null payment request. Ignoring...");
            return;
        }

        try {
            PaymentRequestDto paymentRequest = objectMapper.readValue(record.value(), PaymentRequestDto.class);
            LOGGER.info("Received payment request: {}", paymentRequest);
            paymentService.processPayment(paymentRequest);
            LOGGER.info("Payment processed successfully for Order ID: {}", paymentRequest.getOrderId());
        } catch (Exception e) {
            LOGGER.error("Error deserializing or processing payment request: {} - {}", record.value(), e.getMessage(), e);
        }
    }
}
