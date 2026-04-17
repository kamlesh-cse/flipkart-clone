package com.flipkart.controller;

import com.flipkart.entity.Product;
import com.flipkart.entity.Review;
import com.flipkart.service.ProductService;
import com.flipkart.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controller for product detail pages.
 * Shows product info, stock status, and reviews.
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    // Product details page
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Review> reviews = reviewService.getProductReviews(id);
        double avgRating = reviewService.getAverageRating(id);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);

        return "product-details";
    }
}
