
package com.app.service.impl;

import java.math.BigDecimal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.AdminReportResponseDto;
import com.app.enums.BookingStatus;
import com.app.enums.Role;
import com.app.enums.VehicleStatus;
import com.app.repository.BookingRepository;
import com.app.repository.VehicleRepository;
import com.app.repository.VehicleReturnRepository;
import com.app.repository.DamageChargeRepository;
import com.app.repository.UserRepository;
import com.app.service.AdminReportService;

@Service
public class AdminReportServiceImpl implements AdminReportService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleReturnRepository returnRepository;
    private final DamageChargeRepository damageChargeRepository;
    private final UserRepository userRepository;

    public AdminReportServiceImpl(
            BookingRepository bookingRepository,
            VehicleRepository vehicleRepository,
            VehicleReturnRepository returnRepository,
            DamageChargeRepository damageChargeRepository,
            UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.returnRepository = returnRepository;
        this.damageChargeRepository = damageChargeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminReportResponseDto getAdminReport() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only admin can view reports");
        }

        AdminReportResponseDto dto = new AdminReportResponseDto();

        dto.setTotalBookings(bookingRepository.count());
        dto.setPendingBookings(
                bookingRepository.countByStatus(BookingStatus.PENDING));
        dto.setConfirmedBookings(
                bookingRepository.countByStatus(BookingStatus.CONFIRMED));
        dto.setCompletedBookings(
                bookingRepository.countByStatus(BookingStatus.COMPLETED));
        dto.setCancelledBookings(
                bookingRepository.countByStatus(BookingStatus.CANCELLED));
        dto.setRejectedBookings(
                bookingRepository.countByStatus(BookingStatus.REJECTED));

        dto.setTotalVehicles(vehicleRepository.count());
        dto.setAvailableVehicles(
                vehicleRepository.countByStatus(VehicleStatus.AVAILABLE));
        dto.setBookedVehicles(
                vehicleRepository.countByStatus(VehicleStatus.BOOKED));
        dto.setVehiclesUnderMaintenance(
                vehicleRepository.countByStatus(
                        VehicleStatus.UNDER_MAINTENANCE));

        BigDecimal rentalRevenue = bookingRepository
                .findByStatus(BookingStatus.COMPLETED)
                .stream()
                .map(booking -> booking.getTotalAmount())
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lateCharges = returnRepository.findAll()
                .stream()
                .map(vehicleReturn -> vehicleReturn.getLateCharge())
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal damageCharges =
                damageChargeRepository.getTotalApprovedDamageCharges();

        dto.setTotalRentalRevenue(rentalRevenue);
        dto.setTotalLateCharges(lateCharges);
        dto.setTotalApprovedDamageCharges(
                damageCharges == null ? BigDecimal.ZERO : damageCharges);

        return dto;
    }
}
