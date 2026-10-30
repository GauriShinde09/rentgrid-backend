
package com.app.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class VehiclePerformanceDto {

    private Long vehicleId;
    private String vehicleName;
    private Long totalBookings;
    private Long completedRentals;
    private BigDecimal rentalRevenue;
}
