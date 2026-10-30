
package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class VehicleRequirementDto {

    private String vehicleType;
    private Integer seatingCapacity;
    private BigDecimal maxPricePerDay;
    private LocalDate pickupDate;
    private LocalDate returnDate;

    

}
