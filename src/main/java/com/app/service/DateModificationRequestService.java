package com.app.service;

import java.util.List;

import com.app.dto.DateModificationRequestDto;
import com.app.dto.DateModificationResponseDto;
import com.app.enums.DateModificationStatus;

public interface DateModificationRequestService {

    DateModificationResponseDto createRequest(
            Long bookingId, DateModificationRequestDto requestDto);

    List<DateModificationResponseDto> getMyRequests();

    List<DateModificationResponseDto> getAllRequests();

    DateModificationResponseDto approveRequest(Long requestId);

    DateModificationResponseDto rejectRequest(Long requestId);

}