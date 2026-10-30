
package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.app.enums.BookingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDto {

    private Long id;
    private Long userId;
    private Long vehicleId;
    private String vehicleName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private Integer actualKmDriven;
    private LocalDate actualReturnDate;
}
