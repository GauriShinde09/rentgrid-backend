
package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.dto.DamageChargeRequestDto;
import com.app.dto.DamageChargeResponseDto;
import com.app.service.DamageChargeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/damage-charges")
public class DamageChargeController {

    private final DamageChargeService damageChargeService;

    public DamageChargeController(
            DamageChargeService damageChargeService) {
        this.damageChargeService = damageChargeService;
    }

    // Add damage charge to a booking
    @PostMapping("/booking/{bookingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DamageChargeResponseDto addDamageCharge(
            @PathVariable Long bookingId,
            @Valid @RequestBody DamageChargeRequestDto requestDto) {

        return damageChargeService.addDamageCharge(
                bookingId, requestDto);
    }

    // Get damage charges for a booking
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DamageChargeResponseDto> getChargesByBookingId(
            @PathVariable Long bookingId) {

        return damageChargeService.getChargesByBookingId(bookingId);
    }

    // Approve a damage charge
    @PutMapping("/{chargeId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public DamageChargeResponseDto approveDamageCharge(
            @PathVariable Long chargeId) {

        return damageChargeService.approveDamageCharge(chargeId);
    }
}
