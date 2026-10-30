
package com.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DamageChargeRequestDto {

    @NotBlank(message = "Damage description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String damageDescription;

    @NotNull(message = "Charge amount is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Charge amount must be greater than zero")
    private BigDecimal chargeAmount;

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
}
