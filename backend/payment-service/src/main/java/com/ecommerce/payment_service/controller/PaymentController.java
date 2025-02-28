package com.ecommerce.payment_service.controller;

import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.payload.RefundRequestDto;
import com.ecommerce.payment_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processPayment(@RequestBody PaymentRequestDto request) {
        paymentService.processPayment(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateCard(@RequestBody PaymentRequestDto request) {
        request.setAmount(BigDecimal.ONE); // Set $1 pre-charge for validation
        paymentService.processPayment(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> processRefund(@RequestBody RefundRequestDto request) {
        paymentService.processRefund(request);
        return ResponseEntity.ok().build();
    }
}
