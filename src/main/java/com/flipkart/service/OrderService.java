package com.flipkart.service;

import com.flipkart.entity.*;
import com.flipkart.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Order operations.
 * Handles placing orders, viewing orders, and status updates.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    // Place a new order from the user's cart
    @Transactional
    public Order placeOrder(User user, String shippingAddress) {
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PLACED");

        // Convert cart items to order items
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);

            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();

            // Reduce product stock
            productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(user);

        // Send order confirmation email
        try {
            emailService.sendOrderConfirmation(
                    user.getEmail(), user.getName(), savedOrder.getId(), total);
        } catch (Exception e) {
            log.warn("Failed to send order confirmation email to {}: {}", user.getEmail(), e.getMessage());
        }

        return savedOrder;
    }

    // Get all orders for a user
    @Transactional(readOnly = true)
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    // Get all orders (for admin)
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    // Get order by ID
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Update order status (admin)
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        // Send status update email
        try {
            emailService.sendOrderStatusUpdate(
                    order.getUser().getEmail(),
                    order.getUser().getName(),
                    orderId, status);
        } catch (Exception e) {
            log.warn("Failed to send status update email for order #{}: {}", orderId, e.getMessage());
        }
    }
}
