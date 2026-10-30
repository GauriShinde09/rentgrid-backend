
package com.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.DateModificationRequestDto;
import com.app.dto.DateModificationResponseDto;
import com.app.entity.Booking;
import com.app.entity.DateModificationRequest;
import com.app.entity.User;
import com.app.entity.Vehicle;
import com.app.enums.BookingStatus;
import com.app.enums.DateModificationStatus;
import com.app.enums.Role;
import com.app.repository.BookingRepository;
import com.app.repository.DateModificationRequestRepository;
import com.app.repository.UserRepository;
import com.app.service.DateModificationRequestService;

@Service
public class DateModificationRequestServiceImpl
        implements DateModificationRequestService {

    private final DateModificationRequestRepository requestRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DateModificationRequestServiceImpl(
            DateModificationRequestRepository requestRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository) {

        this.requestRepository = requestRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    @Transactional
    public DateModificationResponseDto createRequest(
            Long bookingId,
            DateModificationRequestDto requestDto) {

        User user = getLoggedInUser();

        if (user.getRole() != Role.CUSTOMER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only customers can request date modifications");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot modify another customer's booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING
                && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending or confirmed bookings can be modified");
        }

        LocalDate pickupDate = requestDto.getRequestedPickupDate();
        LocalDate returnDate = requestDto.getRequestedReturnDate();

        if (pickupDate == null || returnDate == null
                || !returnDate.isAfter(pickupDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Return date must be after pickup date");
        }

        if (pickupDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Pickup date cannot be in the past");
        }

        if (pickupDate.equals(booking.getPickupDate())
                && returnDate.equals(booking.getReturnDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New dates must differ from current booking dates");
        }

        boolean pendingRequestExists =
                requestRepository.existsByBookingIdAndStatus(
                        bookingId, DateModificationStatus.PENDING);

        if (pendingRequestExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A pending date modification request already exists");
        }

        DateModificationRequest request =
                new DateModificationRequest();

        request.setBooking(booking);
        request.setRequestedPickupDate(pickupDate);
        request.setRequestedReturnDate(returnDate);
        request.setReason(requestDto.getReason());
        request.setStatus(DateModificationStatus.PENDING);

        DateModificationRequest savedRequest =
                requestRepository.save(request);

        return convertToDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DateModificationResponseDto> getMyRequests() {

        User user = getLoggedInUser();

        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getBooking()
                        .getUser().getId().equals(user.getId()))
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DateModificationResponseDto> getAllRequests() {

        return requestRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }


@Override
@Transactional
public DateModificationResponseDto approveRequest(Long requestId) {

    DateModificationRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Date modification request not found"));

    if (request.getStatus() != DateModificationStatus.PENDING) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only pending requests can be approved");
    }

    Booking booking = request.getBooking();

    if (booking.getStatus() != BookingStatus.PENDING
            && booking.getStatus() != BookingStatus.CONFIRMED) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This booking can no longer be modified");
    }

    LocalDate pickupDate = request.getRequestedPickupDate();
    LocalDate returnDate = request.getRequestedReturnDate();

    if (!returnDate.isAfter(pickupDate)) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Return date must be after pickup date");
    }

    if (pickupDate.isBefore(LocalDate.now())) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Pickup date cannot be in the past");
    }

    List<BookingStatus> blockingStatuses = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.ACTIVE);

    List<Booking> overlappingBookings =
            bookingRepository
                .findByVehicleIdAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
                        booking.getVehicle().getId(),
                        blockingStatuses,
                        returnDate,
                        pickupDate);

    for (Booking otherBooking : overlappingBookings) {

        // Ignore the booking whose dates are being changed.
        if (!otherBooking.getId().equals(booking.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle is already booked for these dates");
        }
    }

    Vehicle vehicle = booking.getVehicle();

    long rentalDays = ChronoUnit.DAYS.between(
            pickupDate, returnDate);

    BigDecimal totalAmount = vehicle.getPricePerDay()
            .multiply(BigDecimal.valueOf(rentalDays));

    // Update the original booking.
    booking.setPickupDate(pickupDate);
    booking.setReturnDate(returnDate);
    booking.setTotalAmount(totalAmount);

    bookingRepository.save(booking);

    // Mark the modification request as approved.
    request.setStatus(DateModificationStatus.APPROVED);

    DateModificationRequest savedRequest =
            requestRepository.save(request);

    return convertToDto(savedRequest);
}



@Override
@Transactional
public DateModificationResponseDto rejectRequest(Long requestId) {

    DateModificationRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Date modification request not found"));

    if (request.getStatus() != DateModificationStatus.PENDING) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only pending requests can be rejected");
    }

    request.setStatus(DateModificationStatus.REJECTED);

    DateModificationRequest savedRequest =
            requestRepository.save(request);

    return convertToDto(savedRequest);
}


    private DateModificationResponseDto convertToDto(
            DateModificationRequest request) {

        DateModificationResponseDto dto =
                new DateModificationResponseDto();

        dto.setId(request.getId());
        dto.setBookingId(request.getBooking().getId());
        dto.setRequestedPickupDate(request.getRequestedPickupDate());
        dto.setRequestedReturnDate(request.getRequestedReturnDate());
        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());

        return dto;
    }

	
}
