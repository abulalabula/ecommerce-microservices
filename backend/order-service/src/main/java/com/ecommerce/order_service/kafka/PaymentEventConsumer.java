package com.ecommerce.order_service.kafka;

import com.ecommerce.order_service.entity.PaymentEvent;
import com.ecommerce.order_service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public PaymentEventConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-group",
            containerFactory = "paymentResponseKafkaListenerContainerFactory")
    public void processPaymentResponse(ConsumerRecord<String, String> record) {
        if (record.value() == null || record.value().isEmpty()) {
            LOGGER.error("Error: Received empty or null payment response. Ignoring...");
            return;
        }

        try {
            PaymentEvent event = objectMapper.readValue(record.value(), PaymentEvent.class);
            LOGGER.info("Received payment response: {}", event);
            orderService.processPaymentResponse(event);
            LOGGER.info("Payment processed successfully for Order ID: {}", event.getOrderId());
        } catch (Exception e) {
            LOGGER.error("Error deserializing or processing payment response: {} - {}", record.value(), e.getMessage(), e);
        }
    }
}
