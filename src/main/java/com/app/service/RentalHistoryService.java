
package com.app.service;

import java.util.List;

import com.app.dto.RentalHistoryResponseDto;

public interface RentalHistoryService {

    List<RentalHistoryResponseDto> getMyRentalHistory();

    List<RentalHistoryResponseDto> getAllRentalHistory();
}
