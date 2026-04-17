package com.flipkart.config;

import com.flipkart.entity.Product;
import com.flipkart.entity.User;
import com.flipkart.repository.ProductRepository;
import com.flipkart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataLoader - Runs on application startup.
 * Seeds the database with an admin user and sample products
 * so the app works immediately after first run.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Create admin user if not exists
        if (!userRepository.existsByEmail("admin@flipkart.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@flipkart.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setPhone("9876543210");
            admin.setAddress("FlipkartClone HQ, Mumbai");
            userRepository.save(admin);
            log.info(">>> Admin user created: admin@flipkart.com / admin123");
        }

        // Add sample products if database is empty
        if (productRepository.count() == 0) {
            productRepository.save(new Product(null, "iPhone 15", "Apple iPhone 15 with A16 Bionic chip, 128GB storage", 79999, 10, "Electronics", "/images/products/iphone-15.png", null));
            productRepository.save(new Product(null, "Samsung Galaxy S24", "Samsung Galaxy S24 5G, 256GB, Phantom Black", 69999, 5, "Electronics", "/images/products/galaxy-s24.png", null));
            productRepository.save(new Product(null, "Sony WH-1000XM5", "Sony wireless noise-cancelling headphones", 24999, 15, "Electronics", "/images/products/sony-headphones.png", null));
            productRepository.save(new Product(null, "Men's Casual Shirt", "Cotton casual shirt, available in multiple colors", 899, 50, "Clothing", "/images/products/casual-shirt.png", null));
            productRepository.save(new Product(null, "Women's Kurti Set", "Elegant designer kurti set with dupatta", 1299, 30, "Clothing", "/images/products/kurti-set.png", null));
            productRepository.save(new Product(null, "Nike Running Shoes", "Nike Air Max lightweight running shoes", 5499, 3, "Accessories", "/images/products/nike-shoes.png", null));
            productRepository.save(new Product(null, "Leather Wallet", "Premium genuine leather wallet for men", 499, 25, "Accessories", "/images/products/leather-wallet.png", null));
            productRepository.save(new Product(null, "Smartwatch Band", "Silicone replacement band for smartwatches", 299, 0, "Accessories", "/images/products/watch-band.png", null));
            productRepository.save(new Product(null, "Java Programming Book", "Head First Java - beginner friendly book", 599, 20, "Books", "/images/products/java-book.png", null));
            productRepository.save(new Product(null, "Non-Stick Cookware Set", "5-piece non-stick cookware set for kitchen", 1999, 12, "Home & Kitchen", "/images/products/cookware-set.png", null));
            productRepository.save(new Product(null, "LED Desk Lamp", "Adjustable LED desk lamp with USB charging", 799, 8, "Home & Kitchen", "/images/products/desk-lamp.png", null));
            productRepository.save(new Product(null, "Bluetooth Speaker", "JBL portable bluetooth speaker, waterproof", 3499, 2, "Electronics", "/images/products/bt-speaker.png", null));

            log.info(">>> 12 sample products loaded into database");
        }
    }
}
