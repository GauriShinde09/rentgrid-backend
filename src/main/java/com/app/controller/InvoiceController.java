
package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApproveDamageChargeDto;
import com.app.dto.InvoiceResponseDto;
import com.app.entity.Invoice;
import com.app.service.InvoiceService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/rentgrid/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/generate/{bookingId}")
    public ResponseEntity<InvoiceResponseDto> generateInvoice(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                invoiceService.generateInvoice(bookingId));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByBookingId(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceByBookingId(bookingId));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/booking/{bookingId}/damage-charge")
    public ResponseEntity<InvoiceResponseDto> approveDamageCharge(
            @PathVariable Long bookingId,
            @Valid @RequestBody ApproveDamageChargeDto request) {

        InvoiceResponseDto invoice =
                invoiceService.approveDamageCharge(
                        bookingId, request.getDamageCharge());

        return ResponseEntity.ok(invoice);
    }


}
