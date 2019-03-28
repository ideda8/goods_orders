package com.da;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
