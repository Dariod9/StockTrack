package com.example.stocktracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StocktrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocktrackingApplication.class, args);
    }

}
