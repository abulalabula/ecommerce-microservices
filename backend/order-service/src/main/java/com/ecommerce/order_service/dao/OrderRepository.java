package com.ecommerce.order_service.dao;

import com.ecommerce.order_service.entity.Order;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface OrderRepository extends CassandraRepository<Order, UUID> {
}
