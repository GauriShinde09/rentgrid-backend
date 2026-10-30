package com.app.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.app.dto.response.RentalReportDto;
import com.app.entity.Booking;
import com.app.enums.BookingStatus;
import com.app.repository.BookingRepository;
import com.app.service.RentalReportService;

import java.util.List;

@Service
public class RentalReportServiceImpl implements RentalReportService {

    private final BookingRepository bookingRepository;

    public RentalReportServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public RentalReportDto getRentalReport() {

        RentalReportDto report = new RentalReportDto();

        // Count all bookings
        report.setTotalBookings(bookingRepository.count());

        // Count bookings by status
        report.setPendingBookings(
                bookingRepository.countByStatus(BookingStatus.PENDING));

        report.setConfirmedBookings(
                bookingRepository.countByStatus(BookingStatus.CONFIRMED));

        report.setActiveBookings(
                bookingRepository.countByStatus(BookingStatus.ACTIVE));

        report.setCompletedBookings(
                bookingRepository.countByStatus(BookingStatus.COMPLETED));

        report.setCancelledBookings(
                bookingRepository.countByStatus(BookingStatus.CANCELLED));

        report.setRejectedBookings(
                bookingRepository.countByStatus(BookingStatus.REJECTED));

        // Calculate revenue from completed bookings only
        List<Booking> completedBookings =
                bookingRepository.findByStatus(BookingStatus.COMPLETED);

        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Booking booking : completedBookings) {
            if (booking.getTotalAmount() != null) {
                totalRevenue = totalRevenue.add(booking.getTotalAmount());
            }
        }

        report.setTotalRevenue(totalRevenue);

        return report;
    }
}
