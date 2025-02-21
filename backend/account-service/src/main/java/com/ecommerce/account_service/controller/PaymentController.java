package com.ecommerce.account_service.controller;

import com.ecommerce.account_service.exception.ResourceNotFoundException;
import com.ecommerce.account_service.payload.PaymentMethodDTO;
import com.ecommerce.account_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@Valid @RequestBody PaymentMethodDTO paymentMethodDTO) {
        return ResponseEntity.ok(paymentService.createPaymentMethod(paymentMethodDTO));
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {
        return ResponseEntity.ok(paymentService.getAllPaymentMethods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodDTO paymentMethod = paymentService.getPaymentMethodById(id);
        if (paymentMethod == null) {
            throw new ResourceNotFoundException("Payment method with ID: " + id + " not found.");
        }
        return ResponseEntity.ok(paymentMethod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(@PathVariable Long id, @Valid @RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethodDTO updatedPaymentMethod = paymentService.updatePaymentMethod(id, paymentMethodDTO);
        if (updatedPaymentMethod == null) {
            throw new ResourceNotFoundException("Payment method with ID: " + id + " not found.");
        }
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
