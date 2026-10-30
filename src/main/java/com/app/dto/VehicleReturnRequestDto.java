package com.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class VehicleReturnRequestDto {

    @NotNull(message = "Actual kilometres driven is required")
    @PositiveOrZero(message = "Actual kilometres cannot be negative")
    private Integer actualKmDriven;

    @NotNull(message = "Actual return date is required")
    private LocalDate actualReturnDate;
}