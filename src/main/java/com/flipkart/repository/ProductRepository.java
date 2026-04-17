package com.flipkart.repository;

import com.flipkart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product entity.
 * Contains custom queries for search and category filtering.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Filter products by category
    List<Product> findByCategory(String category);

    // Search products by name (case-insensitive partial match)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Search within a specific category
    List<Product> findByCategoryAndNameContainingIgnoreCase(String category, String keyword);
}
