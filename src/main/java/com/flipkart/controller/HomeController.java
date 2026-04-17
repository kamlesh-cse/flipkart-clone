package com.flipkart.controller;

import com.flipkart.entity.Product;
import com.flipkart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller for public pages: Home, About, Contact, Search.
 * These pages are accessible without login.
 */
@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    // Home page - shows all products with optional category filter
    @GetMapping({"/", "/home"})
    public String home(@RequestParam(required = false) String category, Model model) {
        List<Product> products;

        if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        return "home";
    }

    // Search products by keyword
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    // About Us page
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // Contact Us page
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // New Footer Pages
    @GetMapping("/careers")
    public String careers() {
        return "careers";
    }

    @GetMapping("/payments")
    public String payments() {
        return "payments";
    }

    @GetMapping("/shipping")
    public String shipping() {
        return "shipping";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/returns")
    public String returns() {
        return "returns";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}
