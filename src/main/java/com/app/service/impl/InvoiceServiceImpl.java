
package com.app.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.InvoiceResponseDto;
import com.app.entity.Booking;
import com.app.entity.Invoice;
import com.app.enums.BookingStatus;
import com.app.repository.BookingRepository;
import com.app.repository.InvoiceRepository;
import com.app.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(
            BookingRepository bookingRepository,
            InvoiceRepository invoiceRepository) {

        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // Generate invoice for a completed booking
    @Override
    @Transactional
    public InvoiceResponseDto generateInvoice(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Invoice can only be generated for completed bookings");
        }

        if (invoiceRepository.findByBookingId(bookingId).isPresent()) {
            throw new IllegalStateException(
                    "Invoice already exists for this booking");
        }

        Invoice invoice = new Invoice();

        invoice.setBooking(booking);
        invoice.setRentalAmount(booking.getTotalAmount());
        invoice.setExtraKmCharge(BigDecimal.ZERO);
        invoice.setLateReturnCharge(BigDecimal.ZERO);
        invoice.setDamageCharge(BigDecimal.ZERO);

        BigDecimal total = invoice.getRentalAmount()
                .add(invoice.getExtraKmCharge())
                .add(invoice.getLateReturnCharge())
                .add(invoice.getDamageCharge());

        invoice.setTotalAmount(total);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return convertToDto(savedInvoice);
    }

    // Get invoice by booking ID
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceByBookingId(Long bookingId) {

        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found for this booking"));

        return convertToDto(invoice);
    }

    // Convert Invoice entity into response DTO
    private InvoiceResponseDto convertToDto(Invoice invoice) {

        InvoiceResponseDto dto = new InvoiceResponseDto();

        dto.setId(invoice.getId());
        dto.setBookingId(invoice.getBooking().getId());
        dto.setRentalAmount(invoice.getRentalAmount());
        dto.setExtraKmCharge(invoice.getExtraKmCharge());
        dto.setLateReturnCharge(
                invoice.getLateReturnCharge() == null
                        ? BigDecimal.ZERO
                        : invoice.getLateReturnCharge());
        dto.setDamageCharge(invoice.getDamageCharge());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setCreatedAt(invoice.getCreatedAt());

        return dto;
    }

    // Admin approves or updates damage charge
    @Override
    @Transactional
    public InvoiceResponseDto approveDamageCharge(
            Long bookingId, BigDecimal damageCharge) {

        if (damageCharge == null
                || damageCharge.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Damage charge cannot be negative");
        }

        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found for this booking"));

        // Preserve existing charges; initialize missing values
        if (invoice.getExtraKmCharge() == null) {
            invoice.setExtraKmCharge(BigDecimal.ZERO);
        }

        if (invoice.getLateReturnCharge() == null) {
            invoice.setLateReturnCharge(BigDecimal.ZERO);
        }

        if (invoice.getRentalAmount() == null) {
            invoice.setRentalAmount(BigDecimal.ZERO);
        }

        // Update only the damage charge
        invoice.setDamageCharge(damageCharge);

        // Recalculate invoice total
        BigDecimal total = invoice.getRentalAmount()
                .add(invoice.getExtraKmCharge())
                .add(invoice.getLateReturnCharge())
                .add(invoice.getDamageCharge());

        invoice.setTotalAmount(total);

        Invoice updatedInvoice = invoiceRepository.save(invoice);

        return convertToDto(updatedInvoice);
    }
}
