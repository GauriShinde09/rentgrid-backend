
package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.dto.BookingRequestDto;
import com.app.dto.BookingResponseDto;
import com.app.dto.VehicleReturnRequestDto;
import com.app.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Customer creates a booking
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequestDto requestDto) {

        BookingResponseDto response =
                bookingService.createBooking(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Customer views their own bookings
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings() {

        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    // View a specific booking
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // Cancel a booking
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
    
 // Admin views all bookings
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {

        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    
 // Admin confirms a pending booking
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingResponseDto> confirmBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.confirmBooking(id));
    }
 // Admin rejects a pending booking
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingResponseDto> rejectBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.rejectBooking(id));
    }
 // Customer views completed rental history
 // Customer views completed rental history
    @GetMapping("/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponseDto>> getRentalHistory() {

        return ResponseEntity.ok(
                bookingService.getRentalHistory());
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/return")
    public ResponseEntity<BookingResponseDto> returnVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleReturnRequestDto request) {

        BookingResponseDto response = bookingService.returnVehicle(
                id,
                request.getActualKmDriven(),
                request.getActualReturnDate());

        return ResponseEntity.ok(response);
    }
//Admin activates a confirmed booking
@PutMapping("/{id}/activate")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<BookingResponseDto> activateBooking(
     @PathVariable Long id) {

 return ResponseEntity.ok(
         bookingService.activateBooking(id));
}
}


