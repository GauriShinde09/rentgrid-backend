
package com.app.dto;

public class AdminDashboardResponseDto {

    private long totalVehicles;
    private long availableVehicles;
    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long rejectedBookings;
    private long cancelledBookings;

    public AdminDashboardResponseDto() {
    }

    public AdminDashboardResponseDto(
            long totalVehicles,
            long availableVehicles,
            long totalBookings,
            long pendingBookings,
            long confirmedBookings,
            long rejectedBookings,
            long cancelledBookings) {

        this.totalVehicles = totalVehicles;
        this.availableVehicles = availableVehicles;
        this.totalBookings = totalBookings;
        this.pendingBookings = pendingBookings;
        this.confirmedBookings = confirmedBookings;
        this.rejectedBookings = rejectedBookings;
        this.cancelledBookings = cancelledBookings;
    }

    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(long availableVehicles) {
        this.availableVehicles = availableVehicles;
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

    public long getRejectedBookings() {
        return rejectedBookings;
    }

    public void setRejectedBookings(long rejectedBookings) {
        this.rejectedBookings = rejectedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }
}
