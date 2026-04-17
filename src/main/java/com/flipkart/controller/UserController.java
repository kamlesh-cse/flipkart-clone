package com.flipkart.controller;

import com.flipkart.entity.User;
import com.flipkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for user authentication: Login, Signup, Forgot/Reset Password.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Show signup page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // Process signup form
    @PostMapping("/signup")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        String result = userService.registerUser(user);

        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/signup";
        }
    }

    // Show forgot password page
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // Process forgot password form
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        boolean sent = userService.initiatePasswordReset(email);

        if (sent) {
            redirectAttributes.addFlashAttribute("success", "Password reset link sent to your email!");
        } else {
            redirectAttributes.addFlashAttribute("error", "No account found with that email.");
        }
        return "redirect:/forgot-password";
    }

    // Show reset password page (accessed via email link)
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    // Process reset password form
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String password,
                                       RedirectAttributes redirectAttributes) {
        boolean reset = userService.resetPassword(token, password);

        if (reset) {
            redirectAttributes.addFlashAttribute("success", "Password reset successfully! Please login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired reset link.");
            return "redirect:/forgot-password";
        }
    }

    // SHOW USERS
    @GetMapping({"/showrecords", "/show"})
    public String display(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        return "showRecords";
    }

    // DELETE USER
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete user. Ensure no dependencies exist.");
        }
        return "redirect:/show";
    }

    // UPDATE USER (Now includes Role)
    @PostMapping("/user/update")
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

        return "redirect:/show";
    }
}
