package com.ecommerce.order_service.kafka;

import com.ecommerce.order_service.dao.OrderRepository;
import com.ecommerce.order_service.entity.OrderEvent;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final OrderService orderService;

    OrderEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

//    @KafkaListener(topics = "order-events", groupId = "order-group")
//    public void consume(OrderEvent event) {
//
//        if (event.getOrderStatus().equals(OrderStatus.PAID.toString())) {
//            orderService.updateOrder(event.getOrderId().toString());
//        }
//    }
}
