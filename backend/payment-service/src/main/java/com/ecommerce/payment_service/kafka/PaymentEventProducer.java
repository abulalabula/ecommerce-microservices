package com.ecommerce.payment_service.kafka;

import com.ecommerce.payment_service.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventProducer.class);

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String PAYMENT_TOPIC = "payment-events";

    public void publishEvent(PaymentEvent event) {
        System.out.println("in payment event producer");
        LOGGER.info("Publishing Payment Event: {}", event);
        kafkaTemplate.send(PAYMENT_TOPIC, event);
    }
}

