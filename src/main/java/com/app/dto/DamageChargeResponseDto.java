
package com.app.dto;

import java.math.BigDecimal;

public class DamageChargeResponseDto {

    private Long id;
    private Long bookingId;
    private String damageDescription;
    private BigDecimal chargeAmount;
    private Boolean approved;

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

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
