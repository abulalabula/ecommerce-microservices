package com.ecommerce.order_service.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@PrimaryKeyClass
public class OrderPrimaryKey implements Serializable {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "item_id", type = PrimaryKeyType.PARTITIONED)
    private String itemId;

    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.PARTITIONED)
    private LocalDateTime createdAt;

}
