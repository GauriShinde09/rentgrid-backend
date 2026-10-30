package com.app.dto.response;

import java.math.BigDecimal;

public class RentalReportDto {

    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long activeBookings;
    private long completedBookings;
    private long cancelledBookings;
    private long rejectedBookings;
    private BigDecimal totalRevenue;

    public RentalReportDto() {
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(long confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }

    public long getActiveBookings() {
        return activeBookings;
    }

    public void setActiveBookings(long activeBookings) {
        this.activeBookings = activeBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    public long getRejectedBookings() {
        return rejectedBookings;
    }

    public void setRejectedBookings(long rejectedBookings) {
        this.rejectedBookings = rejectedBookings;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}