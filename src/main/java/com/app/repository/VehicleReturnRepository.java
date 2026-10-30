
package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.VehicleReturn;

public interface VehicleReturnRepository
        extends JpaRepository<VehicleReturn, Long> {

    Optional<VehicleReturn> findByBookingId(Long bookingId);

    boolean existsByBookingId(Long bookingId);
}
