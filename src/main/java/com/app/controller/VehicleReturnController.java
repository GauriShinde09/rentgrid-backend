
package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.dto.VehicleReturnRequestDto;
import com.app.dto.VehicleReturnResponseDto;
import com.app.service.VehicleReturnService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/returns")
public class VehicleReturnController {

    private final VehicleReturnService returnService;

    public VehicleReturnController(VehicleReturnService returnService) {
        this.returnService = returnService;
    }

    // Record a vehicle return
    @PostMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleReturnResponseDto recordReturn(
            @PathVariable Long bookingId,
            @Valid @RequestBody VehicleReturnRequestDto requestDto) {

        return returnService.recordReturn(bookingId, requestDto);
    }

    // Get return details for a particular booking
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public VehicleReturnResponseDto getReturnByBookingId(
            @PathVariable Long bookingId) {

        return returnService.getReturnByBookingId(bookingId);
    }

    // Get all vehicle returns
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<VehicleReturnResponseDto> getAllReturns() {

        return returnService.getAllReturns();
    }
}
