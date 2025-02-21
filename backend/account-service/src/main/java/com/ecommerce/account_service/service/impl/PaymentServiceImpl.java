package com.ecommerce.account_service.service.impl;

import com.ecommerce.account_service.dao.PaymentRepository;
import com.ecommerce.account_service.entity.PaymentMethod;
import com.ecommerce.account_service.exception.DuplicateResourceException;
import com.ecommerce.account_service.exception.ResourceNotFoundException;
import com.ecommerce.account_service.payload.PaymentMethodDTO;
import com.ecommerce.account_service.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {

        if (paymentRepository.existsByCardNumber(paymentMethodDTO.getCardNumber())) {
            throw new DuplicateResourceException("Payment method with card number already exists.");
        }

        PaymentMethod paymentMethod = new PaymentMethod(
                null, // auto generated id
                paymentMethodDTO.getCardNumber(),
                paymentMethodDTO.getCardType(),
                paymentMethodDTO.getExpiryDate()
        );

        PaymentMethod savedPaymentMethod = paymentRepository.save(paymentMethod);
        return convertToDTO(savedPaymentMethod);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO getPaymentMethodById(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentRepository.findById(id);
        return paymentMethod.map(this::convertToDTO).orElse(null);
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO paymentMethodDTO) {
        Optional<PaymentMethod> existingPaymentMethod = paymentRepository.findById(id);
        if (existingPaymentMethod.isPresent()) {
            PaymentMethod paymentMethod = existingPaymentMethod.get();
            paymentMethod.setCardNumber(paymentMethodDTO.getCardNumber());
            paymentMethod.setCardType(paymentMethodDTO.getCardType());
            paymentMethod.setExpiryDate(paymentMethodDTO.getExpiryDate());
            PaymentMethod updatedPaymentMethod = paymentRepository.save(paymentMethod);
            return convertToDTO(updatedPaymentMethod);
        }
        return null;
    }

    @Override
    public void deletePaymentMethod(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment method with ID: " + id + " not found.");
        }

        paymentRepository.deleteById(id);
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod paymentMethod) {
        return new PaymentMethodDTO(
                paymentMethod.getId(),
                paymentMethod.getCardType(),
                paymentMethod.getCardNumber(),
                paymentMethod.getExpiryDate()
        );
    }
}
