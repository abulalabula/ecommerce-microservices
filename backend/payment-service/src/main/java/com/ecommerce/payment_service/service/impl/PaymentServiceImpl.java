package com.ecommerce.payment_service.service.impl;

import com.ecommerce.payment_service.dao.PaymentRepository;
import com.ecommerce.payment_service.payload.PaymentRequestDto;
import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.entity.PaymentStatus;
import com.ecommerce.payment_service.event.PaymentEvent;
import com.ecommerce.payment_service.kafka.PaymentEventProducer;
import com.ecommerce.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    @Override
    @Transactional
    public void processPayment(PaymentRequestDto request) {
        // Ensure idempotency (check if payment already exists)
        Optional<Payment> existingPayment = paymentRepository.findByTransactionId(request.getTransactionId());
        if (existingPayment.isPresent()) {
            LOGGER.warn("Duplicate payment request detected, ignoring.");
            return;
        }

        // Determine charge amount (new card requires $1 pre-charge)
        BigDecimal chargeAmount = request.isNewCard() ? BigDecimal.ONE : request.getAmount();

        // Create a new Payment entity
        Payment payment = new Payment();
        payment.setTransactionId(request.getTransactionId());
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setItemId(request.getItemId());
        payment.setAmount(chargeAmount);
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setCardLast4(request.getCardLast4());

        paymentRepository.save(payment);

        // Simulate Payment Processing (80% success rate)
        boolean isSuccessful = Math.random() > 0.2;

        PaymentEvent event;
        if (isSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
            event = new PaymentEvent("payment_successed", request.getUserId(), request.getItemId(),
                    request.getOrderId().toString(), request.getCardLast4(), Instant.now());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            event = new PaymentEvent("payment_authorization_failed", request.getUserId(), request.getItemId(),
                    request.getOrderId().toString(), request.getCardLast4(), Instant.now());
        }

        paymentRepository.save(payment);
        paymentEventProducer.publishEvent(event);
    }
}



