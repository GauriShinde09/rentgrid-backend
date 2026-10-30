
package com.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.VehicleReturnRequestDto;
import com.app.dto.VehicleReturnResponseDto;
import com.app.entity.Booking;
import com.app.entity.VehicleReturn;
import com.app.enums.BookingStatus;
import com.app.enums.Role;
import com.app.enums.VehicleStatus;
import com.app.repository.BookingRepository;
import com.app.repository.VehicleReturnRepository;
import com.app.repository.UserRepository;
import com.app.service.VehicleReturnService;

@Service
public class VehicleReturnServiceImpl implements VehicleReturnService {

    private final VehicleReturnRepository returnRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private static final BigDecimal LATE_FEE_PER_DAY =
            new BigDecimal("500.00");

    public VehicleReturnServiceImpl(
            VehicleReturnRepository returnRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository) {
        this.returnRepository = returnRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public VehicleReturnResponseDto recordReturn(
            Long bookingId, VehicleReturnRequestDto requestDto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only admin can record vehicle returns");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED
                && booking.getStatus() != BookingStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only confirmed or active bookings can be returned");
        }

        if (returnRepository.existsByBookingId(bookingId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Return already recorded for this booking");
        }

        LocalDate actualReturnDate = requestDto.getActualReturnDate();

        if (actualReturnDate == null
                || actualReturnDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Actual return date cannot be in the future");
        }

        if (actualReturnDate.isBefore(booking.getPickupDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Return date cannot be before pickup date");
        }

        long lateDays = Math.max(
                0,
                ChronoUnit.DAYS.between(
                        booking.getReturnDate(), actualReturnDate));

        BigDecimal lateCharge = LATE_FEE_PER_DAY
                .multiply(BigDecimal.valueOf(lateDays));

        // Create the entity that will be saved in the database.
        VehicleReturn vehicleReturn = new VehicleReturn();
        vehicleReturn.setBooking(booking);
        vehicleReturn.setActualReturnDate(actualReturnDate);
        vehicleReturn.setLateDays((int) lateDays);
        vehicleReturn.setLateCharge(lateCharge);

        // Complete the booking.
        booking.setStatus(BookingStatus.COMPLETED);

        // Make the vehicle available again.
        if (booking.getVehicle().getStatus() == VehicleStatus.BOOKED) {
            booking.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        }

        bookingRepository.save(booking);

        // Save the entity first.
        VehicleReturn savedReturn = returnRepository.save(vehicleReturn);

        // Return a DTO instead of the entity to avoid lazy-loading JSON errors.
        return convertToDto(savedReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleReturnResponseDto getReturnByBookingId(Long bookingId) {

        VehicleReturn vehicleReturn = returnRepository
                .findByBookingId(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Return record not found"));

        return convertToDto(vehicleReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleReturnResponseDto> getAllReturns() {

        return returnRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private VehicleReturnResponseDto convertToDto(
            VehicleReturn vehicleReturn) {

        VehicleReturnResponseDto dto = new VehicleReturnResponseDto();

        dto.setId(vehicleReturn.getId());
        dto.setBookingId(vehicleReturn.getBooking().getId());
        dto.setActualReturnDate(vehicleReturn.getActualReturnDate());
        dto.setLateDays(vehicleReturn.getLateDays());
        dto.setLateCharge(vehicleReturn.getLateCharge());
        dto.setCreatedAt(vehicleReturn.getCreatedAt());

        return dto;
    }
}
