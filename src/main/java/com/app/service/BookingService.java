

package com.app.service;

import java.time.LocalDate;
import java.util.List;

import com.app.dto.BookingRequestDto;
import com.app.dto.BookingResponseDto;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto requestDto);

    List<BookingResponseDto> getMyBookings();

    BookingResponseDto getBookingById(Long id);

    BookingResponseDto cancelBooking(Long id);
    
    List<BookingResponseDto> getAllBookings();
    BookingResponseDto confirmBooking(Long id);
    BookingResponseDto rejectBooking(Long id);
    List<BookingResponseDto> getRentalHistory();
    
    BookingResponseDto returnVehicle(
            Long bookingId,
            Integer actualKmDriven,
            LocalDate actualReturnDate);
    BookingResponseDto activateBooking(Long id);
    
    
}
