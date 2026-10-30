
package com.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.app.enums.DateModificationStatus;

public class DateModificationResponseDto {

    private Long id;
    private Long bookingId;
    private LocalDate requestedPickupDate;
    private LocalDate requestedReturnDate;
    private String reason;
    private DateModificationStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

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

    public DateModificationStatus getStatus() {
        return status;
    }

    public void setStatus(DateModificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
