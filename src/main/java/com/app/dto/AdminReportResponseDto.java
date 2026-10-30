
package com.app.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AdminReportResponseDto {

    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long completedBookings;
    private long cancelledBookings;
    private long rejectedBookings;

    private long totalVehicles;
    private long availableVehicles;
    private long bookedVehicles;
    private long vehiclesUnderMaintenance;

    private BigDecimal totalRentalRevenue;
    private BigDecimal totalLateCharges;
    private BigDecimal totalApprovedDamageCharges;
}
