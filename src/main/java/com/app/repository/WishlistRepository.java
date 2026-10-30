
package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserId(Long userId);

    boolean existsByUserIdAndVehicleId(Long userId, Long vehicleId);
}
