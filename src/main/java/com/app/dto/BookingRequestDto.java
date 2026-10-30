
package com.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequestDto {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date cannot be in the past")
    private LocalDate pickupDate;

    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate returnDate;
}
