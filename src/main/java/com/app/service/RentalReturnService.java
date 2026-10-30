
package com.app.service;

import java.util.Optional;
import com.app.entity.RentalReturn;

public interface RentalReturnService {
    RentalReturn recordReturn(RentalReturn rentalReturn);
    Optional<RentalReturn> getReturnByBookingId(Long bookingId);
}
