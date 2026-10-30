
package com.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Review;
import com.app.repository.ReviewRepository;
import com.app.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review addReview(Review review) {
        if (review.getRating() == null
                || review.getRating() < 1
                || review.getRating() > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5");
        }

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByVehicleId(Long vehicleId) {
        return reviewRepository.findByVehicleId(vehicleId);
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
