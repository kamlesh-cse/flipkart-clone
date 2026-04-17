package com.flipkart.controller;

import com.flipkart.entity.User;
import com.flipkart.service.ReviewService;
import com.flipkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for product review operations.
 * Logged-in users can submit reviews and ratings for products.
 */
@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // Submit a review for a product
    @PostMapping("/review/{productId}")
    public String addReview(@PathVariable Long productId,
                             @RequestParam int rating,
                             @RequestParam String comment,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        reviewService.addReview(user, productId, rating, comment);
        redirectAttributes.addFlashAttribute("success", "Review submitted successfully!");

        return "redirect:/product/" + productId;
    }
}
