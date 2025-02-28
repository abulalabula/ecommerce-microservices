package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.payload.RefundRequestDto;

public interface PaymentService {
    void processPayment(PaymentRequestDto request);
    void processRefund(RefundRequestDto request);
}
