
package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.app.enums.BookingStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class RentalHistoryResponseDto {

    private Long bookingId;
    private Long vehicleId;
    private String vehicleName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;

    private LocalDate actualReturnDate;
    private Integer lateDays;
    private BigDecimal lateCharge;


}
