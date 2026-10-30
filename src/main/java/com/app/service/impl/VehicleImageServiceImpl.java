
package com.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.VehicleImage;
import com.app.repository.VehicleImageRepository;
import com.app.service.VehicleImageService;

@Service
public class VehicleImageServiceImpl implements VehicleImageService {

    private final VehicleImageRepository vehicleImageRepository;

    public VehicleImageServiceImpl(
            VehicleImageRepository vehicleImageRepository) {
        this.vehicleImageRepository = vehicleImageRepository;
    }

    @Override
    public VehicleImage addImage(VehicleImage image) {
        return vehicleImageRepository.save(image);
    }

    @Override
    public List<VehicleImage> getImagesByVehicleId(Long vehicleId) {
        return vehicleImageRepository.findByVehicleId(vehicleId);
    }
}
