
package com.app.service;

import java.util.List;

import com.app.dto.DamageChargeRequestDto;
import com.app.dto.DamageChargeResponseDto;

public interface DamageChargeService {

    DamageChargeResponseDto addDamageCharge(
            Long bookingId, DamageChargeRequestDto requestDto);

    List<DamageChargeResponseDto> getChargesByBookingId(Long bookingId);

    DamageChargeResponseDto approveDamageCharge(Long chargeId);
}
