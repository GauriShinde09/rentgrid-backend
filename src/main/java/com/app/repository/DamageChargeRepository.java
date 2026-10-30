
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.DamageCharge;

public interface DamageChargeRepository
        extends JpaRepository<DamageCharge, Long> {

    List<DamageCharge> findByBookingId(Long bookingId);

@org.springframework.data.jpa.repository.Query(
    "SELECT COALESCE(SUM(d.chargeAmount), 0) " +
    "FROM DamageCharge d WHERE d.approved = true"
)
java.math.BigDecimal getTotalApprovedDamageCharges();


}
