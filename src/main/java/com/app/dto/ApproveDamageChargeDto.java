
package com.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApproveDamageChargeDto {

    @NotNull(message = "Damage charge is required")
    @DecimalMin(value = "0.0", message = "Damage charge cannot be negative")
    private BigDecimal damageCharge;
}
