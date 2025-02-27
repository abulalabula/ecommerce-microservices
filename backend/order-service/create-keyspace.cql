CREATE KEYSPACE IF NOT EXISTS ecommerce_microservices_order_service WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE ecommerce_microservices_order_service;

CREATE TABLE IF NOT EXISTS orders (
                                                user_id TEXT,
                                                created_at TIMESTAMP,
                                                order_id TEXT,
                                                order_status TEXT,
                                                payment_status TEXT,
                                                total_quantity INT,
                                                total_amount DOUBLE,
                                                details TEXT,
                                                items LIST<FROZEN<order_item>>,
                                                updated_at TIMESTAMP,
                                                PRIMARY KEY ((user_id), created_at, order_id)
) WITH CLUSTERING ORDER BY (created_at DESC, order_id ASC);

CREATE TYPE IF NOT EXISTS order_item (
                                                   item_id TEXT,
                                                   name TEXT,
                                                   upc TEXT,
                                                   quantity INT
                                               );


-- CREATE KEYSPACE IF NOT EXISTS ecommerce_microservices_order_service
--     WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
-- USE ecommerce_microservices_order_service;
-- CREATE TABLE orders (
--                         user_id TEXT,
--                         item_id TEXT,
--                         created_at TIMESTAMP,
--                         id UUID,
--                         status TEXT,
--                         quantity INT,
--                         details TEXT,
--                         updated_at TIMESTAMP,
--                         PRIMARY KEY ((user_id, item_id, created_at), id)
-- );
--
-- -- ALTER TABLE ecommerce_microservices_order_service.orders ADD quantity int;