package com.flipkart.repository;

import com.flipkart.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for OrderItem entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Check if order item exists by product
    boolean existsByProduct(com.flipkart.entity.Product product);
}
