
package com.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.app.dto.request.LoginRequestDto;
import com.app.dto.response.LoginResponseDto;
import com.app.service.AuthService;

import org.springframework.web.bind.annotation.PutMapping;
import com.app.dto.request.ForgotPasswordRequestDto;
import com.app.dto.request.ResetPasswordRequestDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        return authService.login(loginRequestDto);
    }
    
    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication) {
        return "Logged in as: " + authentication.getName();
    }
    

@PostMapping("/forgot-password")
public ResponseEntity<Map<String, String>> forgotPassword(
        @Valid @RequestBody ForgotPasswordRequestDto request) {

    authService.forgotPassword(request.getEmail());

    return ResponseEntity.ok(Map.of(
            "message",
            "If an account exists for this email, password reset instructions will be sent."
    ));
}

@PutMapping("/reset-password")
public ResponseEntity<Map<String, String>> resetPassword(
        @Valid @RequestBody ResetPasswordRequestDto request) {

    authService.resetPassword(
            request.getToken(),
            request.getNewPassword());

    return ResponseEntity.ok(Map.of(
            "message",
            "Password reset successfully. Please log in with your new password."
    ));
}

}
