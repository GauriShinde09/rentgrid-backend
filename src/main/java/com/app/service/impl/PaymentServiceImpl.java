
package com.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Payment;
import com.app.repository.PaymentRepository;
import com.app.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
