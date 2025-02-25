package com.ecommerce.order_service.payload;

import lombok.Data;

@Data
public class OrderDTO {
    private String userId;
    private String itemId;
    private int quantity;
    private String status;  // CREATED, PAID, COMPLETED, CANCELED
    private String details;
}
