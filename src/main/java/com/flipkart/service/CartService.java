package com.flipkart.service;

import com.flipkart.entity.CartItem;
import com.flipkart.entity.Product;
import com.flipkart.entity.User;
import com.flipkart.repository.CartItemRepository;
import com.flipkart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Cart operations.
 * Handles add, remove, update quantity, and cart total calculation.
 */
@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // Get all cart items for a user
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    // Add a product to cart (or increase quantity if already in cart)
    @Transactional
    public void addToCart(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Guard: cannot add out-of-stock items to cart
        if (product.getStock() <= 0) {
            throw new RuntimeException("\"" + product.getName() + "\" is currently out of stock.");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            // Product already in cart - increase quantity (capped at stock)
            CartItem cartItem = existingItem.get();
            int newQty = cartItem.getQuantity() + 1;
            if (newQty > product.getStock()) {
                throw new RuntimeException("Cannot add more — only " + product.getStock() + " units available.");
            }
            cartItem.setQuantity(newQty);
            cartItemRepository.save(cartItem);
        } else {
            // New product - add to cart
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
        }
    }

    // Remove an item from cart (with user ownership check)
    public void removeFromCart(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: This cart item does not belong to you.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    // Update quantity of a cart item (with user ownership check)
    public void updateQuantity(Long cartItemId, int quantity, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: This cart item does not belong to you.");
        }
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    // Calculate total price of all items in cart
    @Transactional(readOnly = true)
    public double getCartTotal(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // Clear the entire cart for a user (after checkout)
    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
