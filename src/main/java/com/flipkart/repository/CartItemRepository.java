package com.flipkart.repository;

import com.flipkart.entity.CartItem;
import com.flipkart.entity.User;
import com.flipkart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CartItem entity.
 * Manages shopping cart operations per user.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get all cart items for a specific user
    List<CartItem> findByUser(User user);

    // Find a specific product in user's cart
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    // Delete all cart items for a user (after checkout)
    void deleteByUser(User user);

    // Check if cart item exists by product
    boolean existsByProduct(Product product);
}
