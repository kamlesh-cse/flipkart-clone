package com.flipkart.controller;

import com.flipkart.entity.Order;
import com.flipkart.entity.Product;
import com.flipkart.entity.User;
import com.flipkart.service.OrderService;
import com.flipkart.service.ProductService;
import com.flipkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for admin dashboard.
 * Only accessible by users with ADMIN role.
 * Admin can manage products, orders, and view users.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Redirect /admin to /admin/dashboard
    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    // Admin dashboard - shows summary
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Order> orders = orderService.getAllOrders();
        List<User> users = userService.getAllUsers();

        model.addAttribute("totalProducts", products.size());
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("recentOrders", orders.stream().limit(5).toList());

        return "admin/dashboard";
    }

    // --- PRODUCT MANAGEMENT ---

    // List all products (admin view)
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    // Show add product form
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    // Save new product
    @PostMapping("/products/add")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        return "redirect:/admin/products";
    }

    // Show edit product form
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "admin/add-product";
    }

    // Update existing product
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id, Product product,
                                 RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        return "redirect:/admin/products";
    }

    // Delete a product
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete product — it has existing orders, reviews, or cart items. Remove those first.");
        }
        return "redirect:/admin/products";
    }

    // --- ORDER MANAGEMENT ---

    // List all orders (admin view)
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    // Update order status
    @PostMapping("/orders/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Order status updated to " + status);
        return "redirect:/admin/orders";
    }

    // --- USER MANAGEMENT ---

    // List all users (admin view)
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // Delete user
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete user. Ensure no dependencies exist.");
        }
        return "redirect:/admin/users";
    }

    // Update user
    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String role,
                             RedirectAttributes redirectAttributes) {

        java.util.Optional<User> existingUser = userService.getUserById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setEmail(email);
            user.setRole(role); 
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }

        return "redirect:/admin/users";
    }
}
