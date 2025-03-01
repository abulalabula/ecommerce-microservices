package com.ecommerce.order_service.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table("orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @PrimaryKey
    private OrderPrimaryKey key;

    @Column("order_status")
    private String orderStatus;

    @Column("payment_status")
    private String paymentStatus;

    @Column("total_quantity")
    private int totalQuantity;

    @Column("total_amount")
    private double totalAmount;

    @Column("details")
    private String details;

    @Column("items")
    private List<@Frozen OrderItem> items;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}


//@Table("orders")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Order {
//
//    @PrimaryKey
//    private UUID id;
//
//    @Column("order_id")
//    private String orderId;
//
//    @Column("user_id")
//    private String userId;
//
//    @Column("order_status")
//    private String orderStatus;  // CREATED, PAID, COMPLETED, CANCELED
//
//    @Column("payment_status")
//    private String paymentStatus;  // PENDING, COMPLETED, FAILED, REFUNDED
//
//    @Column("total_quantity")
//    private int totalQuantity;
//
//    @Column("total_amount")
//    private double totalAmount;
//
//    @Column("details")
//    private String details;
//
//    @Column("items")
//    private List<@Frozen OrderItem> items;  // Frozen List
//
//    @Column("created_at")
//    private LocalDateTime createdAt;
//
//    @Column("updated_at")
//    private LocalDateTime updatedAt;
//}
