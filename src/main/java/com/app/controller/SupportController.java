package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.dto.SupportRequestDto;
import com.app.dto.SupportResponseDto;
import com.app.enums.SupportStatus;
import com.app.service.SupportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    // Customer submits support request
    @PostMapping("/support")
    public ResponseEntity<SupportResponseDto> createRequest(
            @Valid @RequestBody SupportRequestDto requestDto,
            Authentication authentication) {

        String email = authentication.getName();

        SupportResponseDto response =
                supportService.createRequest(requestDto, email);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Customer views their own requests
    @GetMapping("/support/my-requests")
    public ResponseEntity<List<SupportResponseDto>> getMyRequests(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                supportService.getMyRequests(email)
        );
    }

    // Admin views all support requests
    @GetMapping("/admin/support")
    public ResponseEntity<List<SupportResponseDto>> getAllRequests() {

        return ResponseEntity.ok(
                supportService.getAllRequests()
        );
    }

    // Admin updates support request status
    @PutMapping("/admin/support/{id}/status")
    public ResponseEntity<SupportResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam SupportStatus status) {

        return ResponseEntity.ok(
                supportService.updateStatus(id, status)
        );
    }
}