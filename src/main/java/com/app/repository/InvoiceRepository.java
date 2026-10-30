
package com.app.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByBookingId(Long bookingId);

    // Total revenue from all invoices
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i")
    BigDecimal getTotalRevenue();

    // Total rental income
    @Query("SELECT COALESCE(SUM(i.rentalAmount), 0) FROM Invoice i")
    BigDecimal getTotalRentalRevenue();

    // Total extra kilometre charges
    @Query("SELECT COALESCE(SUM(i.extraKmCharge), 0) FROM Invoice i")
    BigDecimal getTotalExtraKmCharges();

    // Total late return charges
    @Query("SELECT COALESCE(SUM(i.lateReturnCharge), 0) FROM Invoice i")
    BigDecimal getTotalLateReturnCharges();

    // Total damage charges
    @Query("SELECT COALESCE(SUM(i.damageCharge), 0) FROM Invoice i")
    BigDecimal getTotalDamageCharges();
}
