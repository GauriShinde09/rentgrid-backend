

package com.app.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;


import com.app.entity.Vehicle;
import com.app.enums.VehicleStatus;
import java.time.LocalDate;

public interface VehicleService {

    Vehicle addVehicle(Vehicle vehicle);

    List<Vehicle> getAllVehicles();

    Vehicle getVehicleById(Long id);

    Vehicle updateVehicle(Long id, Vehicle vehicle);

    void deleteVehicle(Long id);
    
    List<Vehicle> getVehiclesByStatus(VehicleStatus status);
    List<Vehicle> searchVehiclesByBrand(String brand);
    List<Vehicle> searchVehiclesByType(String vehicleType);
    List<Vehicle> searchVehiclesByPrice(
            BigDecimal minPrice,
            BigDecimal maxPrice);
    List<Vehicle> searchVehiclesBySeatingCapacity(Integer seatingCapacity);
    List<Vehicle> getVehiclesSortedByPriceAsc();

    List<Vehicle> getVehiclesSortedByPriceDesc();
    Page<Vehicle>  getAllVehicles(int page, int size);
    List<Vehicle> getAvailableVehicles(
            LocalDate pickupDate,
            LocalDate returnDate);
}
