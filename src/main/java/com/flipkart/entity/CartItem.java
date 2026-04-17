package com.flipkart.entity;

import jakarta.persistence.*;

/**
 * CartItem entity - represents a product added to user's shopping cart.
 * Each cart item links a user to a product with a quantity.
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who owns this cart item
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The product added to cart
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // How many of this product the user wants
    @Column(nullable = false)
    private int quantity = 1;

    // ---------- Constructors ----------

    public CartItem() {
    }

    public CartItem(Long id, User user, Product product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
