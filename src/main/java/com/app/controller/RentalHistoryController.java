
package com.app.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.RentalHistoryResponseDto;
import com.app.service.RentalHistoryService;

@RestController
@RequestMapping("/rentgrid/rental-history")
public class RentalHistoryController {

    private final RentalHistoryService rentalHistoryService;

    public RentalHistoryController(
            RentalHistoryService rentalHistoryService) {
        this.rentalHistoryService = rentalHistoryService;
    }

    // Customer: view their own rental history
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<RentalHistoryResponseDto> getMyRentalHistory() {
        return rentalHistoryService.getMyRentalHistory();
    }

    // Admin: view all rental history
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RentalHistoryResponseDto> getAllRentalHistory() {
        return rentalHistoryService.getAllRentalHistory();
    }
}
