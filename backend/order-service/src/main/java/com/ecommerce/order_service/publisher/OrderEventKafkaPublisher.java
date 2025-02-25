package com.ecommerce.order_service.publisher;

import com.ecommerce.order_service.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventKafkaPublisher {
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Value("${order.event.topicName}")
    private String topicName;

    public void sendOrderEvent(Order order) {
        kafkaTemplate.send("order_events", order);
    }
}

