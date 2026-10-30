
package com.app.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Vehicle;
import com.app.enums.VehicleStatus;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByStatus(VehicleStatus status);

    List<Vehicle> findByBrandIgnoreCase(String brand);

    @Query("SELECT v FROM Vehicle v WHERE UPPER(v.vehicleType) = UPPER(:vehicleType)")
	List<Vehicle> findByVehicleTypeIgnoreCase(String vehicleType);

    @Query("SELECT v FROM Vehicle v WHERE v.pricePerDay BETWEEN :minPrice AND :maxPrice")
	List<Vehicle> findByPricePerDayBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice);
    
    List<Vehicle> findBySeatingCapacity(Integer seatingCapacity);
    List<Vehicle> findAll(Sort sort);

long countByStatus(com.app.enums.VehicleStatus status);

}
