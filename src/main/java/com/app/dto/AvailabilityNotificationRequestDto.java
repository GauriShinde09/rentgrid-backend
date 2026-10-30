
package com.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

public class AvailabilityNotificationRequestDto {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date cannot be in the past")
    private LocalDate pickupDate;

    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate returnDate;

    public AvailabilityNotificationRequestDto() {
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
