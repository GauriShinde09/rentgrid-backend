
package com.app.service;

import java.util.List;
import com.app.entity.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
    List<Payment> getPaymentsByBookingId(Long bookingId);
}
