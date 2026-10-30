
package com.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.BookingRequestDto;
import com.app.dto.BookingResponseDto;
import com.app.entity.Booking;
import com.app.entity.Invoice;
import com.app.entity.User;
import com.app.entity.Vehicle;
import com.app.enums.BookingStatus;
import com.app.enums.Role;
import com.app.enums.VehicleStatus;
import com.app.repository.BookingRepository;
import com.app.repository.InvoiceRepository;
import com.app.repository.UserRepository;
import com.app.repository.VehicleRepository;
import com.app.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;
    private final InvoiceRepository invoiceRepository;
    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            VehicleRepository vehicleRepository,
            ModelMapper modelMapper, InvoiceRepository invoiceRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
		this.invoiceRepository = invoiceRepository;
    }

    // Customer creates a booking
    @Override
    @Transactional
    public BookingResponseDto createBooking(
            BookingRequestDto requestDto) {

        User user = getLoggedInUser();

        LocalDate pickupDate = requestDto.getPickupDate();
        LocalDate returnDate = requestDto.getReturnDate();

        if (pickupDate == null || returnDate == null
                || pickupDate.isBefore(LocalDate.now())
                || !returnDate.isAfter(pickupDate)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Select valid pickup and return dates");
        }

        Vehicle vehicle = vehicleRepository.findById(
                requestDto.getVehicleId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle is not available");
        }


List<BookingStatus> blockingStatuses = List.of(
        BookingStatus.PENDING,
        BookingStatus.CONFIRMED,
        BookingStatus.ACTIVE
);

List<Booking> conflicts = bookingRepository
        .findByVehicleIdAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
                vehicle.getId(),
                blockingStatuses,
                returnDate,
                pickupDate
        );

if (!conflicts.isEmpty()) {
    Booking conflictingBooking = conflicts.get(0);

    String bookedFrom = conflictingBooking.getPickupDate()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"));

    String bookedUntil = conflictingBooking.getReturnDate()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"));

    throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "This vehicle is already booked from "
                    + bookedFrom + " to " + bookedUntil + "."
    );
}


        long days = ChronoUnit.DAYS.between(
                pickupDate, returnDate);

        BigDecimal totalAmount = vehicle.getPricePerDay()
                .multiply(BigDecimal.valueOf(days));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setPickupDate(pickupDate);
        booking.setReturnDate(returnDate);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        return convertToDto(savedBooking);
    }

    // Customer views their own bookings
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getMyBookings() {

        User user = getLoggedInUser();

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Get one booking; customers can only view their own
    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id) {

        User user = getLoggedInUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (user.getRole() != Role.ADMIN
                && !booking.getUser().getId().equals(user.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another customer's booking");
        }

        return convertToDto(booking);
    }

    // Cancel a booking
    @Override
    @Transactional
    public BookingResponseDto cancelBooking(Long id) {

        User user = getLoggedInUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (user.getRole() != Role.ADMIN
                && !booking.getUser().getId().equals(user.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot cancel another customer's booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING
                && booking.getStatus() != BookingStatus.CONFIRMED) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending or confirmed bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return convertToDto(bookingRepository.save(booking));
    }

    // Admin views all bookings
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Admin confirms a pending booking
    @Override
    @Transactional
    public BookingResponseDto confirmBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        return convertToDto(bookingRepository.save(booking));
    }

    // Admin rejects a pending booking
    @Override
    @Transactional
    public BookingResponseDto rejectBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending bookings can be rejected");
        }

        booking.setStatus(BookingStatus.REJECTED);

        return convertToDto(bookingRepository.save(booking));
    }

    // Get currently logged-in user
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    // Convert Booking entity to response DTO
    private BookingResponseDto convertToDto(Booking booking) {

        BookingResponseDto dto = modelMapper.map(
                booking, BookingResponseDto.class);

        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setVehicleId(booking.getVehicle().getId());
        dto.setVehicleName(booking.getVehicle().getName());

        return dto;
    }
    
 // Customer views completed rental history
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getRentalHistory() {

        User user = getLoggedInUser();

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .filter(booking ->
                        booking.getStatus() == BookingStatus.COMPLETED)
                .map(this::convertToDto)
                .toList();
    }



@Override
@Transactional
public BookingResponseDto activateBooking(Long id) {

    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Booking not found"));

    if (booking.getStatus() != BookingStatus.CONFIRMED) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only confirmed bookings can be activated");
    }

    booking.setStatus(BookingStatus.ACTIVE);

    Booking updatedBooking = bookingRepository.save(booking);

    return convertToDto(updatedBooking);
}

@Override
@Transactional
public BookingResponseDto returnVehicle(
        Long bookingId,
        Integer actualKmDriven,
        LocalDate actualReturnDate) {

    // Validate actual kilometres
    if (actualKmDriven == null || actualKmDriven < 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Actual kilometres cannot be negative");
    }

    if (actualReturnDate == null) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Actual return date is required");
    }

    // Find booking
    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Booking not found"));

    // Only active bookings can be returned
    if (booking.getStatus() != BookingStatus.ACTIVE) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Only active bookings can be returned");
    }

    // Validate actual return date
    if (actualReturnDate.isBefore(booking.getPickupDate())) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Actual return date cannot be before pickup date");
    }

    Vehicle vehicle = booking.getVehicle();

    // Calculate scheduled rental days
    long rentalDays = ChronoUnit.DAYS.between(
            booking.getPickupDate(),
            booking.getReturnDate());

    rentalDays = Math.max(1, rentalDays);

    // Calculate extra kilometres
    long includedKm =
            (long) vehicle.getIncludedKmPerDay() * rentalDays;

    long extraKm = Math.max(0, actualKmDriven - includedKm);

    BigDecimal extraKmCharge = vehicle.getExtraKmRate()
            .multiply(BigDecimal.valueOf(extraKm));

    // Calculate late-return days
    long lateDays = Math.max(
            0,
            ChronoUnit.DAYS.between(
                    booking.getReturnDate(),
                    actualReturnDate));

    // Calculate late-return charge
    BigDecimal lateReturnCharge = vehicle.getPricePerDay()
            .multiply(BigDecimal.valueOf(lateDays));

    // Update booking
    booking.setActualKmDriven(actualKmDriven);
    booking.setActualReturnDate(actualReturnDate);
    booking.setStatus(BookingStatus.COMPLETED);

    bookingRepository.save(booking);


    // Find existing invoice or create a new invoice
    Invoice invoice = invoiceRepository
            .findByBookingId(bookingId)
            .orElseGet(Invoice::new);

    if (invoice.getId() == null) {
        invoice.setBooking(booking);
        invoice.setRentalAmount(booking.getTotalAmount());
    }

    // Initialize charges if any value is null
    if (invoice.getExtraKmCharge() == null) {
        invoice.setExtraKmCharge(BigDecimal.ZERO);
    }

    if (invoice.getLateReturnCharge() == null) {
        invoice.setLateReturnCharge(BigDecimal.ZERO);
    }

    if (invoice.getDamageCharge() == null) {
        invoice.setDamageCharge(BigDecimal.ZERO);
    }

    // Update calculated charges
    invoice.setExtraKmCharge(extraKmCharge);
    invoice.setLateReturnCharge(lateReturnCharge);

    // Calculate invoice total
    BigDecimal total = invoice.getRentalAmount()
            .add(invoice.getExtraKmCharge())
            .add(invoice.getLateReturnCharge())
            .add(invoice.getDamageCharge());

    invoice.setTotalAmount(total);

    // Save invoice
    invoiceRepository.save(invoice);

    
   

    return convertToDto(booking);
}



}
