
package com.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.BookingStatisticsDto;
import com.app.dto.RevenueReportDto;
import com.app.dto.VehiclePerformanceDto;
import com.app.service.BookingStatisticsService;
import com.app.service.RevenueReportService;
import com.app.service.VehiclePerformanceService;

@RestController
@RequestMapping("/rentgrid/admin/reports")
public class AdminReportController {

    private final BookingStatisticsService bookingStatisticsService;
    private final RevenueReportService revenueReportService;
    private final VehiclePerformanceService vehiclePerformanceService;
    
    public AdminReportController(
            BookingStatisticsService bookingStatisticsService, RevenueReportService revenueReportService, VehiclePerformanceService vehiclePerformanceService) {
        this.bookingStatisticsService = bookingStatisticsService;
		this.revenueReportService = revenueReportService;
		this.vehiclePerformanceService = vehiclePerformanceService;
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingStatisticsDto> getBookingStatistics() {

        return ResponseEntity.ok(
                bookingStatisticsService.getBookingStatistics());
    }

@GetMapping("/revenue")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<RevenueReportDto> getRevenueReport() {

    return ResponseEntity.ok(
            revenueReportService.getRevenueReport());
}



@GetMapping("/vehicles")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<VehiclePerformanceDto>>
        getVehiclePerformance() {

    return ResponseEntity.ok(
            vehiclePerformanceService.getVehiclePerformance());
}

}



