package com.ecommerce.order_service.dao;

import com.ecommerce.order_service.entity.Order;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CassandraRepository<Order, UUID> {
    @Query("SELECT * FROM orders WHERE user_id = ?0 AND order_id = ?1 ORDER BY created_at DESC LIMIT 1")
    Optional<Order> findLatestOrderByUserIdAndOrderId(String userId, String orderId);


    //    Optional<Order> findOrderByOrderId(String id);
    //    Optional<Order> findFirstByOrderIdOrderByUpdatedAtDesc(String orderId);
}
