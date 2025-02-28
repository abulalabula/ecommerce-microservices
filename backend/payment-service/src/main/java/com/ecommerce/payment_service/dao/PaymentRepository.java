package com.ecommerce.payment_service.dao;

import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Ensure idempotency: Find payment by unique transactionId
    Optional<Payment> findByTransactionId(String transactionId);

    // Retrieve payments for a specific order
    List<Payment> findByOrderId(UUID orderId);

    // Retrieve payments for a specific user
    List<Payment> findByUserId(Long userId);

    // Retrieve payments by status (e.g., all REFUNDED payments)
    List<Payment> findByStatus(PaymentStatus status);

    // Retrieve payments linked to a specific original payment (for refunds)
    List<Payment> findByOriginalPaymentId(Long originalPaymentId);
}
