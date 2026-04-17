package com.flipkart.service;

import com.flipkart.entity.Product;
import com.flipkart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Product-related business logic.
 * Handles CRUD operations, search, and category filtering.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private com.flipkart.repository.CartItemRepository cartItemRepository;

    @Autowired
    private com.flipkart.repository.OrderItemRepository orderItemRepository;

    @Autowired
    private com.flipkart.repository.ReviewRepository reviewRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Search products by name keyword
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Save a new product (admin)
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product (admin)
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(java.util.Objects.requireNonNull(id, "Product ID must not be null"))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setCategory(updatedProduct.getCategory());
        product.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(product);
    }

    // Delete a product (admin) — guarded with @Transactional
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (cartItemRepository.existsByProduct(product)) {
            throw new RuntimeException("Cannot delete: product is in active carts.");
        }
        if (orderItemRepository.existsByProduct(product)) {
            throw new RuntimeException("Cannot delete: product has existing orders.");
        }
        if (reviewRepository.existsByProduct(product)) {
            throw new RuntimeException("Cannot delete: product has reviews.");
        }

        productRepository.deleteById(id);
    }

    // Reduce stock after purchase
    @Transactional
    public void reduceStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
