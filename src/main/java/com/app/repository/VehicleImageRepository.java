
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.VehicleImage;

public interface VehicleImageRepository
        extends JpaRepository<VehicleImage, Long> {

    List<VehicleImage> findByVehicleId(Long vehicleId);
}
