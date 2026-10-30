
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.DateModificationRequest;
import com.app.enums.DateModificationStatus;

public interface DateModificationRequestRepository
        extends JpaRepository<DateModificationRequest, Long> {

    List<DateModificationRequest> findByBookingId(Long bookingId);

    boolean existsByBookingIdAndStatus(
            Long bookingId,
            DateModificationStatus status);
}
