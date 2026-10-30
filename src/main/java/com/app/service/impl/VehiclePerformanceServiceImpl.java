
package com.app.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.VehiclePerformanceDto;
import com.app.enums.BookingStatus;
import com.app.repository.BookingRepository;
import com.app.service.VehiclePerformanceService;

@Service
@Transactional(readOnly = true)
public class VehiclePerformanceServiceImpl
        implements VehiclePerformanceService {

    private final BookingRepository bookingRepository;

    public VehiclePerformanceServiceImpl(
            BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<VehiclePerformanceDto> getVehiclePerformance() {

        List<Object[]> results = bookingRepository
                .getVehiclePerformance(BookingStatus.COMPLETED);

        List<VehiclePerformanceDto> performanceList = new ArrayList<>();

        for (Object[] row : results) {

            VehiclePerformanceDto dto = new VehiclePerformanceDto();

            dto.setVehicleId((Long) row[0]);
            dto.setVehicleName((String) row[1]);
            dto.setTotalBookings((Long) row[2]);

            // COUNT with CASE may return a Long or another Number type
            dto.setCompletedRentals(((Number) row[3]).longValue());

            BigDecimal rentalRevenue = row[4] == null
                    ? BigDecimal.ZERO
                    : (BigDecimal) row[4];

            dto.setRentalRevenue(rentalRevenue);

            performanceList.add(dto);
        }

        return performanceList;
    }
}
