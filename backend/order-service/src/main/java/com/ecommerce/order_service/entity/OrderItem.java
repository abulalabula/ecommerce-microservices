package com.ecommerce.order_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Column("item_id")
    private String itemId;
    private String name;
    private String upc;
    private int quantity;
}