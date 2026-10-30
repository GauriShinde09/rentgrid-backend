

package com.app.service;

import java.math.BigDecimal;

import com.app.dto.InvoiceResponseDto;

public interface InvoiceService {

    InvoiceResponseDto generateInvoice(Long bookingId);

    InvoiceResponseDto getInvoiceByBookingId(Long bookingId);

    InvoiceResponseDto approveDamageCharge(
            Long bookingId, BigDecimal damageCharge);
}
