
package com.app.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.entity.RentalReturn;
import com.app.repository.RentalReturnRepository;
import com.app.service.RentalReturnService;

@Service
public class RentalReturnServiceImpl implements RentalReturnService {

    private final RentalReturnRepository rentalReturnRepository;

    public RentalReturnServiceImpl(
            RentalReturnRepository rentalReturnRepository) {
        this.rentalReturnRepository = rentalReturnRepository;
    }

    @Override
    public RentalReturn recordReturn(RentalReturn rentalReturn) {
        return rentalReturnRepository.save(rentalReturn);
    }

    @Override
    public Optional<RentalReturn> getReturnByBookingId(Long bookingId) {
        return rentalReturnRepository.findByBookingId(bookingId);
    }
}
