
package com.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.AdminDashboardResponseDto;
import com.app.enums.BookingStatus;
import com.app.enums.VehicleStatus;
import com.app.repository.BookingRepository;
import com.app.repository.VehicleRepository;
import com.app.service.AdminDashboardService;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;

    public AdminDashboardServiceImpl(
            BookingRepository bookingRepository,
            VehicleRepository vehicleRepository) {

        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponseDto getDashboardStats() {

        long totalVehicles = vehicleRepository.count();

        long availableVehicles =
                vehicleRepository.countByStatus(VehicleStatus.AVAILABLE);

        long totalBookings = bookingRepository.count();

        long pendingBookings =
                bookingRepository.countByStatus(BookingStatus.PENDING);

        long confirmedBookings =
                bookingRepository.countByStatus(BookingStatus.CONFIRMED);

        long rejectedBookings =
                bookingRepository.countByStatus(BookingStatus.REJECTED);

        long cancelledBookings =
                bookingRepository.countByStatus(BookingStatus.CANCELLED);

        return new AdminDashboardResponseDto(
                totalVehicles,
                availableVehicles,
                totalBookings,
                pendingBookings,
                confirmedBookings,
                rejectedBookings,
                cancelledBookings
        );
    }
}
