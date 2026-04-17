package com.flipkart.service;

import com.flipkart.entity.Product;
import com.flipkart.entity.Review;
import com.flipkart.entity.User;
import com.flipkart.repository.ProductRepository;
import com.flipkart.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Review operations.
 * Handles adding reviews and calculating average ratings.
 * Bug fix: Added rating validation (1-5) and @Transactional on write.
 */
@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add a new review for a product
    @Transactional
    public Review addReview(User user, Long productId, int rating, String comment) {
        // Validate rating range
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validate comment is not empty
        if (comment == null || comment.trim().isEmpty()) {
            comment = "No comment";
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment.trim());

        log.info("Review submitted by user [{}] for product [{}] with rating [{}]", user.getEmail(), product.getName(),
                rating);
        return reviewRepository.save(review);
    }

    // Get all reviews for a product
    @Transactional(readOnly = true)
    public List<Review> getProductReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    // Calculate average rating for a product
    @Transactional(readOnly = true)
    public double getAverageRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<Review> reviews = reviewRepository.findByProductOrderByCreatedAtDesc(product);

        if (reviews.isEmpty())
            return 0;

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }
}
