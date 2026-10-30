
package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VehicleReturnResponseDto {

    private Long id;
    private Long bookingId;
    private LocalDate actualReturnDate;
    private Integer lateDays;
    private BigDecimal lateCharge;
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

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public BigDecimal getLateCharge() {
        return lateCharge;
    }

    public void setLateCharge(BigDecimal lateCharge) {
        this.lateCharge = lateCharge;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
