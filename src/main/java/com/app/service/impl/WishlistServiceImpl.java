
package com.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Wishlist;
import com.app.repository.WishlistRepository;
import com.app.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Wishlist addToWishlist(Wishlist wishlist) {
        boolean exists = wishlistRepository
                .existsByUserIdAndVehicleId(
                        wishlist.getUser().getId(),
                        wishlist.getVehicle().getId());

        if (exists) {
            throw new IllegalArgumentException(
                    "Vehicle is already in the wishlist");
        }

        return wishlistRepository.save(wishlist);
    }

    @Override
    public List<Wishlist> getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    public void removeFromWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}
