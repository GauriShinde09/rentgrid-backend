
package com.app.service;

import java.util.List;
import com.app.entity.Wishlist;

public interface WishlistService {
    Wishlist addToWishlist(Wishlist wishlist);
    List<Wishlist> getWishlistByUserId(Long userId);
    void removeFromWishlist(Long id);
}
