
package com.app.service;

import java.util.List;
import com.app.entity.Review;

public interface ReviewService {
    Review addReview(Review review);
    List<Review> getReviewsByVehicleId(Long vehicleId);
    List<Review> getReviewsByUserId(Long userId);
}
