CREATE KEYSPACE IF NOT EXISTS ecommerce_microservices_order_service
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE ecommerce_microservices_order_service;
CREATE TABLE orders (
                        user_id TEXT,
                        item_id TEXT,
                        created_at TIMESTAMP,
                        id UUID,
                        status TEXT,
                        quantity INT,
                        details TEXT,
                        updated_at TIMESTAMP,
                        PRIMARY KEY ((user_id, item_id, created_at), id)
);

-- ALTER TABLE ecommerce_microservices_order_service.orders ADD quantity int;