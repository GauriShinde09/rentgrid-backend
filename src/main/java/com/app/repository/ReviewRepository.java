
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Review;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByVehicleId(Long vehicleId);

    List<Review> findByUserId(Long userId);
}
