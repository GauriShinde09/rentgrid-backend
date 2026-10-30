
package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceResponseDto {

    private Long id;
    private Long bookingId;
    private BigDecimal rentalAmount;
    private BigDecimal extraKmCharge;
    private BigDecimal damageCharge;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private BigDecimal lateReturnCharge;
}

