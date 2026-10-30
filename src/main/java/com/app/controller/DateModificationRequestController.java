
package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.dto.DateModificationRequestDto;
import com.app.dto.DateModificationResponseDto;
import com.app.service.DateModificationRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/date-modification-requests")
public class DateModificationRequestController {

    private final DateModificationRequestService requestService;

    public DateModificationRequestController(
            DateModificationRequestService requestService) {
        this.requestService = requestService;
    }

    // Customer submits a date change request
    @PostMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public DateModificationResponseDto createRequest(
            @PathVariable Long bookingId,
            @Valid @RequestBody DateModificationRequestDto requestDto) {

        return requestService.createRequest(bookingId, requestDto);
    }

    // Customer views their own requests
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<DateModificationResponseDto> getMyRequests() {
        return requestService.getMyRequests();
    }

    // Admin views all requests
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DateModificationResponseDto> getAllRequests() {
        return requestService.getAllRequests();
    }

    // Admin approves a request
    @PutMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public DateModificationResponseDto approveRequest(
            @PathVariable Long requestId) {

        return requestService.approveRequest(requestId);
    }

    // Admin rejects a request
    @PutMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public DateModificationResponseDto rejectRequest(
            @PathVariable Long requestId) {

        return requestService.rejectRequest(requestId);
    }
}
