
package com.app.service.impl;

import org.springframework.stereotype.Service;

import com.app.dto.BookingStatisticsDto;
import com.app.enums.BookingStatus;
import com.app.repository.BookingRepository;
import com.app.service.BookingStatisticsService;

@Service
public class BookingStatisticsServiceImpl
        implements BookingStatisticsService {

    private final BookingRepository bookingRepository;

    public BookingStatisticsServiceImpl(
            BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingStatisticsDto getBookingStatistics() {

        BookingStatisticsDto dto = new BookingStatisticsDto();

        dto.setTotalBookings(bookingRepository.count());

        dto.setPendingBookings(
                bookingRepository.countByStatus(BookingStatus.PENDING));

        dto.setConfirmedBookings(
                bookingRepository.countByStatus(BookingStatus.CONFIRMED));

        dto.setActiveBookings(
                bookingRepository.countByStatus(BookingStatus.ACTIVE));

        dto.setCompletedBookings(
                bookingRepository.countByStatus(BookingStatus.COMPLETED));

        dto.setCancelledBookings(
                bookingRepository.countByStatus(BookingStatus.CANCELLED));

        dto.setRejectedBookings(
                bookingRepository.countByStatus(BookingStatus.REJECTED));

        return dto;
    }
}
