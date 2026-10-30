
package com.app.service;

import java.util.List;
import com.app.entity.VehicleImage;

public interface VehicleImageService {
    VehicleImage addImage(VehicleImage image);
    List<VehicleImage> getImagesByVehicleId(Long vehicleId);
}
