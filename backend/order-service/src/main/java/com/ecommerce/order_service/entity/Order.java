package com.ecommerce.order_service.entity;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("orders")
public class Order {

    @PrimaryKey
    private OrderPrimaryKey primaryKey;

    @Column("id")
    private UUID id;

    @Column("status")
    private String status;  // CREATED, PAID, COMPLETED, CANCELED

    @Column("quantity")
    private int quantity;

    @Column("details")
    private String details;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}