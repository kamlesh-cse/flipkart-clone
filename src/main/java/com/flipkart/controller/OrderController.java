package com.flipkart.controller;

import com.flipkart.entity.Order;
import com.flipkart.entity.User;
import com.flipkart.service.OrderService;
import com.flipkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for order operations (user-facing).
 * Users can place orders and view their order history.
 * Admin order management is handled by AdminController under /admin/orders.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Helper method to get current logged-in user
    private User getCurrentUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Place an order (from checkout page)
    // Note: form in checkout.html must post to /orders/place
    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);

        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Shipping address is required.");
            return "redirect:/checkout";
        }

        try {
            Order order = orderService.placeOrder(user, shippingAddress);
            redirectAttributes.addFlashAttribute("success",
                    "Order placed successfully! Order ID: #" + order.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }

        return "redirect:/orders";
    }

    // View user's order history
    @GetMapping
    public String viewOrders(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "orders";
    }
}
