package com.app.service;

import java.util.List;

import com.app.dto.SupportRequestDto;
import com.app.dto.SupportResponseDto;
import com.app.enums.SupportStatus;

public interface SupportService {

    // Customer submits a support request
    SupportResponseDto createRequest(
            SupportRequestDto requestDto,
            String email
    );

    // Customer views their own requests
    List<SupportResponseDto> getMyRequests(String email);

    // Admin views all customer requests
    List<SupportResponseDto> getAllRequests();

    // Admin updates request status
    SupportResponseDto updateStatus(
            Long requestId,
            SupportStatus status
    );
}