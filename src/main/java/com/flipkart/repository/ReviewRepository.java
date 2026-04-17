package com.flipkart.repository;

import com.flipkart.entity.Product;
import com.flipkart.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Review entity.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for a product (newest first)
    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    // Check if review exists by product
    boolean existsByProduct(Product product);
}
