package com.flipkart.repository;

import com.flipkart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity.
 * Spring Data JPA auto-generates SQL queries from method names.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used during login)
    Optional<User> findByEmail(String email);

    // Find user by reset token (used during password reset)
    Optional<User> findByResetToken(String resetToken);

    // Check if email already exists (used during signup)
    boolean existsByEmail(String email);
}
