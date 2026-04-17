package com.flipkart.repository;

import com.flipkart.entity.Order;
import com.flipkart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Order entity.
 * Uses JOIN FETCH to eagerly load orderItems and their products,
 * preventing LazyInitializationException in Thymeleaf templates.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders for a specific user, eagerly fetching items + products
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.user " +
           "WHERE o.user = :user " +
           "ORDER BY o.orderDate DESC")
    List<Order> findByUserOrderByOrderDateDesc(@Param("user") User user);

    // Get all orders for admin, eagerly fetching items + products
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.user " +
           "ORDER BY o.orderDate DESC")
    List<Order> findAllByOrderByOrderDateDesc();
}
