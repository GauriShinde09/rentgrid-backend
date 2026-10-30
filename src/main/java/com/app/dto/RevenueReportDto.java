
package com.app.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RevenueReportDto {

    private BigDecimal totalRevenue;
    private BigDecimal totalRentalRevenue;
    private BigDecimal totalExtraKmCharges;
    private BigDecimal totalLateReturnCharges;
    private BigDecimal totalDamageCharges;
}
