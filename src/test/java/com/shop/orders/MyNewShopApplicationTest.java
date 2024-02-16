package com.shop.orders;

import org.springframework.boot.SpringApplication;

public class MyNewShopApplicationTest {

    public static void main(String[] args) {
        SpringApplication.from(MyNewShopApplicationTest::main)
                .with(MongoDBContainerDevMode.class)
                .run(args);
    }

}
