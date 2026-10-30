
package com.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class BookingStatisticsDto {

    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long activeBookings;
    private long completedBookings;
    private long cancelledBookings;
    private long rejectedBookings;
}
