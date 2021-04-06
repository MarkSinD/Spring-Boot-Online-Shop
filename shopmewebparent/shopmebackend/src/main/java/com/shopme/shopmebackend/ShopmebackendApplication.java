package com.shopme.shopmebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.shopme.shopmebackend.user"})
public class ShopmebackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmebackendApplication.class, args);
    }

}
