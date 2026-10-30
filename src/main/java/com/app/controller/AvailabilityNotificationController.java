
package com.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.dto.AvailabilityNotificationRequestDto;
import com.app.service.AvailabilityNotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/availability-notifications")
public class AvailabilityNotificationController {

    private final AvailabilityNotificationService notificationService;

    public AvailabilityNotificationController(
            AvailabilityNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, String>> createRequest(
            @Valid @RequestBody AvailabilityNotificationRequestDto request) {

        String message = notificationService.createRequest(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", message));
    }
}
