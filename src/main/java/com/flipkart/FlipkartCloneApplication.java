package com.flipkart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Flipkart Clone application.
 * This starts the embedded Tomcat server and initializes Spring context.
 */
@SpringBootApplication
@org.springframework.scheduling.annotation.EnableAsync
public class FlipkartCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlipkartCloneApplication.class, args);
    }
}
