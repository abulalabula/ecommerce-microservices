//package com.ecommerce.order_service.kafka;
//
//import com.ecommerce.order_service.entity.OrderEvent;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OrderEventProducer {
//    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
//
//    @Value("${topic.name.producer}")
//    private String topicName;
//
//    OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void produce(OrderEvent event) {
//        kafkaTemplate.send(topicName, event);
//    }
//}
