-- Drop tables if they exist
DROP TABLE IF EXISTS account_roles;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS payment_methods;

-- Create roles table
CREATE TABLE roles (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- Changed INT to BIGINT
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Create accounts table
CREATE TABLE accounts (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_email VARCHAR(100) NOT NULL UNIQUE,
                          user_name VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          shipping_address_id BIGINT DEFAULT NULL,
                          billing_address_id BIGINT DEFAULT NULL,
                          payment_method_id BIGINT DEFAULT NULL,
                          create_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create addresses table
CREATE TABLE addresses (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           street VARCHAR(255) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(50) NOT NULL,
                           zip_code VARCHAR(20) NOT NULL
);

-- Create payment methods table
CREATE TABLE payment_methods (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 card_number VARCHAR(19) NOT NULL,
                                 card_type ENUM('VISA', 'MASTERCARD', 'AMEX', 'DISCOVER') NOT NULL,
                                 expiry_date VARCHAR(5) NOT NULL
);

-- Create mapping table for account roles
CREATE TABLE account_roles (
                               account_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL,  -- Changed INT to BIGINT
                               PRIMARY KEY (account_id, role_id),
                               FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
                               FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert roles
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_ADMIN'),
                                 (2, 'ROLE_USER')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert test accounts (passwords are bcrypt encoded)
INSERT INTO accounts (id, user_email, user_name, password, create_date_time, update_date_time) VALUES
                                                                                                   (4, 'admin1@example.com', 'adminUser1', '$2a$10$ZJkc216r6YYtMB9gTfc5Z.saBUuZzGCNwd1LWX6lbmnk5J04fl8YC', NOW(), NOW()),
                                                                                                   (5, 'user1@example.com', 'normalUser1', '$2a$10$UuV7RhN3xR1FphKZ5OIwL.jlbKgHX0SjrRbF1T8gDJQmpcujOhfYC', NOW(), NOW()),
                                                                                                   (6, 'user2@example.com', 'normalUser2', 'user2password', NOW(), NOW())
ON DUPLICATE KEY UPDATE user_email = VALUES(user_email);

-- Assign roles to accounts
INSERT INTO account_roles (account_id, role_id) VALUES
                                                    (4, 1),  -- Admin user
                                                    (5, 2)   -- Normal user
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- Insert addresses
INSERT INTO addresses (id, street, city, state, zip_code) VALUES
                                                              (4, '123 Admin St', 'Admin City', 'AD', '12345'),
                                                              (5, '456 User St', 'User City', 'US', '67890')
ON DUPLICATE KEY UPDATE street = VALUES(street);

-- Link addresses to accounts
UPDATE accounts SET shipping_address_id = 4, billing_address_id = 4 WHERE id = 4;
UPDATE accounts SET shipping_address_id = 5, billing_address_id = 5 WHERE id = 5;

-- Insert payment methods
INSERT INTO payment_methods (id, card_number, card_type, expiry_date) VALUES
                                                                          (4, '4111111111111111', 'VISA', '12/27'),
                                                                          (5, '5555555555554444', 'MASTERCARD', '11/29')
ON DUPLICATE KEY UPDATE card_number = VALUES(card_number);

-- Link payment methods to accounts
UPDATE accounts SET payment_method_id = 4 WHERE id = 4;
UPDATE accounts SET payment_method_id = 5 WHERE id = 5;
