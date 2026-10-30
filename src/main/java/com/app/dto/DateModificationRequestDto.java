package com.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DateModificationRequestDto {

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date cannot be in the past")
    private LocalDate requestedPickupDate;

    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate requestedReturnDate;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    public LocalDate getRequestedPickupDate() {
        return requestedPickupDate;
    }

    public void setRequestedPickupDate(LocalDate requestedPickupDate) {
        this.requestedPickupDate = requestedPickupDate;
    }

    public LocalDate getRequestedReturnDate() {
        return requestedReturnDate;
    }

    public void setRequestedReturnDate(LocalDate requestedReturnDate) {
        this.requestedReturnDate = requestedReturnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}