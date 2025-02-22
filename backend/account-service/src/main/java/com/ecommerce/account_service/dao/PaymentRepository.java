package com.ecommerce.account_service.dao;

import com.ecommerce.account_service.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findById(Long id);
    boolean existsByCardNumber(String cardNumber);
}
