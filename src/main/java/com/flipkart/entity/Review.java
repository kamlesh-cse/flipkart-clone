package com.flipkart.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Review entity - stores product reviews and ratings by users.
 * Rating is from 1 to 5 stars.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who wrote this review
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The product being reviewed
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Star rating: 1 to 5
    @Column(nullable = false)
    private int rating;

    // Review text/comment
    @Column(length = 500)
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // ---------- Constructors ----------

    public Review() {
    }

    public Review(Long id, User user, Product product, int rating,
                  String comment, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // ---------- Getters and Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
