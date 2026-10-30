
package com.app.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DamageAssessmentDto {

    private String description;

    private String damageCategory;

    private String severity;

    private BigDecimal estimatedRepairCost;

    private String recommendation;
}
