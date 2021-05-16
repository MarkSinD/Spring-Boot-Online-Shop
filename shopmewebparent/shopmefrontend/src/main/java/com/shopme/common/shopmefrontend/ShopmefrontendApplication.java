package com.shopme.common.shopmefrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.shopme.shopmebackend.category"})
public class ShopmefrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmefrontendApplication.class, args);
    }

}
