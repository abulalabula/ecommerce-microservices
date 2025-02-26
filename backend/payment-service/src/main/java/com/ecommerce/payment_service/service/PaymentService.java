package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.payload.PaymentRequestDto;

public interface PaymentService {
    void processPayment(PaymentRequestDto request);
}
