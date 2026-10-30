
package com.app.service;

import java.util.List;

import com.app.dto.VehicleReturnRequestDto;
import com.app.dto.VehicleReturnResponseDto;

public interface VehicleReturnService {

    VehicleReturnResponseDto recordReturn(
            Long bookingId, VehicleReturnRequestDto requestDto);

    VehicleReturnResponseDto getReturnByBookingId(Long bookingId);

    List<VehicleReturnResponseDto> getAllReturns();
}
