package com.shop.orders;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.shop.orders"})
@OpenAPIDefinition
public class MyNewShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNewShopApplication.class, args);
	}

}
