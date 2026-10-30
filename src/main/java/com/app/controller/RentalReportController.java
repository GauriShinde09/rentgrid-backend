package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.response.RentalReportDto;
import com.app.service.RentalReportService;

@RestController
@RequestMapping("/rentgrid/admin/reports")
public class RentalReportController {

    private final RentalReportService rentalReportService;

    public RentalReportController(RentalReportService rentalReportService) {
        this.rentalReportService = rentalReportService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalReportDto getRentalReport() {
        return rentalReportService.getRentalReport();
    }
}