
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.AvailabilityNotification;

public interface AvailabilityNotificationRepository
        extends JpaRepository<AvailabilityNotification, Long> {

    boolean existsByUserIdAndVehicleIdAndPickupDateAndReturnDateAndStatus(
            Long userId,
            Long vehicleId,
            java.time.LocalDate pickupDate,
            java.time.LocalDate returnDate,
            String status);

    List<AvailabilityNotification> findByStatus(String status);
}
