package com.ecommerce.order_service.payload;

import com.ecommerce.order_service.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private String userId;
    private List<OrderItem> items;
    private String details;
}
