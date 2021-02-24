package com.github.casside.pac4jx.clients.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Boot Starter
 */
@SpringBootApplication(scanBasePackages = {"com.github.casside.pac4jx.clients"})
@EnableWebMvc
@SpringBootTest
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
