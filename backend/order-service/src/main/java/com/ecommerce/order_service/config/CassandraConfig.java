package com.ecommerce.order_service.config;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;


//@Configuration
//@EnableCassandraRepositories
//public class CassandraConfig extends AbstractCassandraConfiguration {
//
//    @Override
//    protected String getKeyspaceName() {
//        return "ecommerce-microservices-order-service";
//    }
//}

@Configuration
@EnableCassandraRepositories(basePackages = "com.ecommerce.order_service.dao")
public class CassandraConfig {

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(CassandraProperties properties) {
        return builder -> builder.withKeyspace(properties.getKeyspaceName());
    }
}