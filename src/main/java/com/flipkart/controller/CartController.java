package com.flipkart.controller;

import com.flipkart.entity.CartItem;
import com.flipkart.entity.User;
import com.flipkart.service.CartService;
import com.flipkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for shopping cart operations.
 * Requires USER role (must be logged in).
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // Helper method to get current logged-in user
    private User getCurrentUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // View cart page
    @GetMapping("/cart")
    public String viewCart(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        List<CartItem> cartItems = cartService.getCartItems(user);
        double total = cartService.getCartTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    // Add product to cart
    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        cartService.addToCart(user, productId);
        redirectAttributes.addFlashAttribute("success", "Product added to cart!");
        return "redirect:/cart";
    }

    // Buy Now - Add product to cart and jump straight to checkout
    @PostMapping("/cart/buy-now/{productId}")
    public String buyNow(@PathVariable Long productId,
                         Authentication auth) {
        User user = getCurrentUser(auth);
        cartService.addToCart(user, productId);
        // Redirect directly to checkout
        return "redirect:/checkout";
    }

    // Remove item from cart
    @PostMapping("/cart/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        cartService.removeFromCart(cartItemId, user);
        redirectAttributes.addFlashAttribute("success", "Item removed from cart.");
        return "redirect:/cart";
    }

    // Update quantity of a cart item
    @PostMapping("/cart/update/{cartItemId}")
    public String updateQuantity(@PathVariable Long cartItemId,
                                  @RequestParam int quantity,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        if (quantity < 1) {
            cartService.removeFromCart(cartItemId, user);
        } else {
            cartService.updateQuantity(cartItemId, quantity, user);
        }
        redirectAttributes.addFlashAttribute("success", "Cart updated.");
        return "redirect:/cart";
    }

    // Show checkout page
    @GetMapping("/checkout")
    public String checkoutPage(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        List<CartItem> cartItems = cartService.getCartItems(user);

        // Guard: Redirect if cart is empty
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty. Add items before checking out.");
            return "redirect:/cart";
        }

        double total = cartService.getCartTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        return "checkout";
    }
}
