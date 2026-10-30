
package com.app.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.DamageChargeRequestDto;
import com.app.dto.DamageChargeResponseDto;
import com.app.entity.Booking;
import com.app.entity.DamageCharge;
import com.app.repository.BookingRepository;
import com.app.repository.DamageChargeRepository;
import com.app.service.DamageChargeService;

@Service
public class DamageChargeServiceImpl implements DamageChargeService {

    private final DamageChargeRepository damageChargeRepository;
    private final BookingRepository bookingRepository;

    public DamageChargeServiceImpl(
            DamageChargeRepository damageChargeRepository,
            BookingRepository bookingRepository) {
        this.damageChargeRepository = damageChargeRepository;
        this.bookingRepository = bookingRepository;
    }

    // Add a damage charge to a completed booking
    @Override
    @Transactional
    public DamageChargeResponseDto addDamageCharge(
            Long bookingId, DamageChargeRequestDto requestDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != com.app.enums.BookingStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Damage charges can only be added to completed bookings");
        }

        DamageCharge damageCharge = new DamageCharge();
        damageCharge.setBooking(booking);
        damageCharge.setDamageDescription(
                requestDto.getDamageDescription());
        damageCharge.setChargeAmount(requestDto.getChargeAmount());
        damageCharge.setApproved(false);

        DamageCharge savedCharge =
                damageChargeRepository.save(damageCharge);

        return convertToDto(savedCharge);
    }

    // Get all damage charges for a booking
    @Override
    @Transactional(readOnly = true)
    public List<DamageChargeResponseDto> getChargesByBookingId(
            Long bookingId) {

        if (!bookingRepository.existsById(bookingId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Booking not found");
        }

        return damageChargeRepository.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Approve a damage charge
    @Override
    @Transactional
    public DamageChargeResponseDto approveDamageCharge(Long chargeId) {

        DamageCharge damageCharge = damageChargeRepository.findById(chargeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Damage charge not found"));

        if (Boolean.TRUE.equals(damageCharge.getApproved())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Damage charge is already approved");
        }

        damageCharge.setApproved(true);

        DamageCharge savedCharge =
                damageChargeRepository.save(damageCharge);

        return convertToDto(savedCharge);
    }

    // Convert entity to response DTO
    private DamageChargeResponseDto convertToDto(
            DamageCharge damageCharge) {

        DamageChargeResponseDto dto = new DamageChargeResponseDto();

        dto.setId(damageCharge.getId());
        dto.setBookingId(damageCharge.getBooking().getId());
        dto.setDamageDescription(
                damageCharge.getDamageDescription());
        dto.setChargeAmount(damageCharge.getChargeAmount());
        dto.setApproved(damageCharge.getApproved());

        return dto;
    }
}
