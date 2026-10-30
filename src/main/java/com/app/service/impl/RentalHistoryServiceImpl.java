
package com.app.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.RentalHistoryResponseDto;
import com.app.entity.Booking;
import com.app.entity.User;
import com.app.entity.VehicleReturn;
import com.app.enums.BookingStatus;
import com.app.enums.Role;
import com.app.repository.BookingRepository;
import com.app.repository.UserRepository;
import com.app.repository.VehicleReturnRepository;
import com.app.service.RentalHistoryService;

@Service
public class RentalHistoryServiceImpl implements RentalHistoryService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleReturnRepository returnRepository;
    private final ModelMapper modelMapper;
    public RentalHistoryServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            VehicleReturnRepository returnRepository,
            ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.returnRepository = returnRepository;
        this.modelMapper = modelMapper;
    }

    // Customer can view their own completed rentals
    @Override
    @Transactional(readOnly = true)
    public List<RentalHistoryResponseDto> getMyRentalHistory() {

        User user = getLoggedInUser();

        return bookingRepository.findByUserIdAndStatus(
                        user.getId(), BookingStatus.COMPLETED)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Admin can view all completed rentals
    @Override
    @Transactional(readOnly = true)
    public List<RentalHistoryResponseDto> getAllRentalHistory() {

        User user = getLoggedInUser();

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only admin can view all rental history");
        }

        return bookingRepository.findByStatus(BookingStatus.COMPLETED)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Get currently logged-in user
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    
    private RentalHistoryResponseDto convertToDto(Booking booking) {

        RentalHistoryResponseDto dto =
                modelMapper.map(booking, RentalHistoryResponseDto.class);

        // Booking ID field has a different name in the DTO
        dto.setBookingId(booking.getId());

        // Vehicle details
        dto.setVehicleId(booking.getVehicle().getId());
        dto.setVehicleName(booking.getVehicle().getName());

        // Add return details if a return record exists
        returnRepository.findByBookingId(booking.getId())
                .ifPresent(vehicleReturn -> {
                    dto.setActualReturnDate(
                            vehicleReturn.getActualReturnDate());
                    dto.setLateDays(vehicleReturn.getLateDays());
                    dto.setLateCharge(vehicleReturn.getLateCharge());
                });

        return dto;
    }

    private void setReturnDetails(
            RentalHistoryResponseDto dto, VehicleReturn vehicleReturn) {

        dto.setActualReturnDate(vehicleReturn.getActualReturnDate());
        dto.setLateDays(vehicleReturn.getLateDays());
        dto.setLateCharge(vehicleReturn.getLateCharge());
    }
}
