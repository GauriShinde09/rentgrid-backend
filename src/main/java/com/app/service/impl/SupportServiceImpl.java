package com.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.SupportRequestDto;
import com.app.dto.SupportResponseDto;
import com.app.entity.SupportRequest;
import com.app.entity.User;
import com.app.enums.SupportStatus;
import com.app.repository.SupportRepository;
import com.app.repository.UserRepository;
import com.app.service.SupportService;

@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final UserRepository userRepository;

    public SupportServiceImpl(
            SupportRepository supportRepository,
            UserRepository userRepository) {

        this.supportRepository = supportRepository;
        this.userRepository = userRepository;
    }

    // Customer submits support request
    @Override
    public SupportResponseDto createRequest(
            SupportRequestDto requestDto,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        SupportRequest request = new SupportRequest();

        request.setUser(user);
        request.setSubject(requestDto.getSubject());
        request.setMessage(requestDto.getMessage());

        SupportRequest savedRequest =
                supportRepository.save(request);

        return convertToDto(savedRequest);
    }

    // Customer views their own requests
    @Override
    @Transactional(readOnly = true)
    public List<SupportResponseDto> getMyRequests(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return supportRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Admin views all requests
    @Override
    @Transactional(readOnly = true)
    public List<SupportResponseDto> getAllRequests() {

        return supportRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Admin updates request status
    @Override
    @Transactional
    public SupportResponseDto updateStatus(
            Long requestId,
            SupportStatus status) {

        SupportRequest request =
                supportRepository.findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException("Support request not found"));

        request.setStatus(status);
        request.setUpdatedAt(LocalDateTime.now());

        SupportRequest updatedRequest =
                supportRepository.save(request);

        return convertToDto(updatedRequest);
    }
    // Convert Entity into Response DTO
    private SupportResponseDto convertToDto(
            SupportRequest request) {

        SupportResponseDto dto = new SupportResponseDto();

        dto.setId(request.getId());
        dto.setCustomerName(request.getUser().getName());
        dto.setCustomerEmail(request.getUser().getEmail());
        dto.setSubject(request.getSubject());
        dto.setMessage(request.getMessage());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());

        return dto;
    }
}