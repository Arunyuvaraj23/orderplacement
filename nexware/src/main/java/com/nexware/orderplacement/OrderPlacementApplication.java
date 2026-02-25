package com.nexware.orderplacement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class OrderPlacementApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderPlacementApplication.class, args);
    }
}
