package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.payload.RefundRequestDto;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments();
    void processPayment(PaymentRequestDto request);
    void processRefund(RefundRequestDto request);
}
