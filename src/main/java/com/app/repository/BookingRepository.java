
package com.app.repository;

import java.time.LocalDate;import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Booking;
import com.app.enums.BookingStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByVehicleIdAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
            Long vehicleId,
            List<BookingStatus> statuses,
            LocalDate returnDate,
            LocalDate pickupDate);

    // Get completed bookings for a customer
    List<Booking> findByUserIdAndStatus(
            Long userId, BookingStatus status);

    // Get all completed bookings
    List<Booking> findByStatus(BookingStatus status);

    long countByStatusIn(List<BookingStatus> statuses);

    List<Booking> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findByStatusAndCreatedAtBetween(
            BookingStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
    long countByStatus(BookingStatus status);
    

long count();


@Query("""
    SELECT b.vehicle.id, b.vehicle.name, COUNT(b.id),
           SUM(CASE WHEN b.status = :completed THEN 1 ELSE 0 END),
           SUM(CASE WHEN b.status = :completed THEN b.totalAmount ELSE 0 END)
    FROM Booking b
    GROUP BY b.vehicle.id, b.vehicle.name
    ORDER BY COUNT(b.id) DESC
    """)
List<Object[]> getVehiclePerformance(
        @Param("completed") BookingStatus completed);




}
