
package com.app.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.app.dto.request.ProfileUpdateDto;
import com.app.dto.request.UserRequestDto;
import com.app.dto.response.UserResponseDto;
import com.app.entity.User;
import com.app.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(
            UserService userService,
            ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    // Register a new customer
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerUser(
            @Valid @RequestBody UserRequestDto userRequestDto) {

        User user = userService.registerUser(userRequestDto);

        return modelMapper.map(user, UserResponseDto.class);
    }

    // Admin views all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(user -> modelMapper.map(
                        user, UserResponseDto.class))
                .toList();
    }

    // Get logged-in user's profile
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<UserResponseDto> getMyProfile() {

        User user = userService.getMyProfile();

        return ResponseEntity.ok(
                modelMapper.map(user, UserResponseDto.class));
    }

    // Update logged-in user's profile
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @Valid @RequestBody ProfileUpdateDto profileUpdateDto) {

        User updatedUser =
                userService.updateMyProfile(profileUpdateDto);

        return ResponseEntity.ok(
                modelMapper.map(
                        updatedUser, UserResponseDto.class));
    }
}
