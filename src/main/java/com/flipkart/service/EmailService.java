package com.flipkart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service to send email notifications.
 * Used for: signup welcome, order confirmation, status updates, password reset.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    // Reads the sender address from application.properties (spring.mail.username)
    @Value("${spring.mail.username}")
    private String fromEmail;

    // Send a simple text email
    @org.springframework.scheduling.annotation.Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);
        log.info("Sending email to [{}] subject: [{}]", to, subject);
        mailSender.send(message);
    }

    // Welcome email sent after user signup
    public void sendWelcomeEmail(String to, String name) {
        String subject = "Welcome to Flipkart Clone!";
        String body = "Hi " + name + ",\n\n"
                + "Welcome to Flipkart Clone! Your account has been created successfully.\n"
                + "Start shopping now and enjoy great deals!\n\n"
                + "Regards,\nFlipkart Clone Team";
        sendEmail(to, subject, body);
    }

    // Order confirmation email
    public void sendOrderConfirmation(String to, String name, Long orderId, double total) {
        String subject = "Order Confirmed - #" + orderId;
        String body = "Hi " + name + ",\n\n"
                + "Your order #" + orderId + " has been placed successfully!\n"
                + "Total Amount: Rs. " + total + "\n\n"
                + "We will notify you when your order is shipped.\n\n"
                + "Regards,\nFlipkart Clone Team";
        sendEmail(to, subject, body);
    }

    // Order status update email
    public void sendOrderStatusUpdate(String to, String name, Long orderId, String status) {
        String subject = "Order #" + orderId + " - Status Updated";
        String body = "Hi " + name + ",\n\n"
                + "Your order #" + orderId + " status has been updated to: " + status + "\n\n"
                + "Regards,\nFlipkart Clone Team";
        sendEmail(to, subject, body);
    }

    // Password reset email with reset link
    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Password Reset Request";
        String body = "Hi,\n\n"
                + "You requested a password reset. Click the link below to reset your password:\n\n"
                + resetLink + "\n\n"
                + "If you didn't request this, please ignore this email.\n\n"
                + "Regards,\nFlipkart Clone Team";
        sendEmail(to, subject, body);
    }
}
