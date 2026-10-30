
package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminDashboardResponseDto;
import com.app.service.AdminDashboardService;

@RestController
@RequestMapping("/rentgrid/admin")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(
            AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponseDto> getDashboardStats() {

        AdminDashboardResponseDto response =
                adminDashboardService.getDashboardStats();

        return ResponseEntity.ok(response);
    }
}
