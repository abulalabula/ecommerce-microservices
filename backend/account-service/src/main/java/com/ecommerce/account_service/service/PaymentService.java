package com.ecommerce.account_service.service;

import com.ecommerce.account_service.payload.PaymentMethodDTO;
import java.util.List;

public interface PaymentService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    List<PaymentMethodDTO> getAllPaymentMethods();
    PaymentMethodDTO getPaymentMethodById(Long id);
    PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO paymentMethodDTO);
    void deletePaymentMethod(Long id);
}
