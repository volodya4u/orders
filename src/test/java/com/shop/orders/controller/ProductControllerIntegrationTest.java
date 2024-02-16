package com.shop.orders.controller;

import com.shop.orders.dto.ProductDto;
import com.shop.orders.model.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIntegrationTest {

    private static UUID productId;

    private static final String TEST_PRODUCT_1_NAME = "Laptop";
    private static final Double TEST_PRODUCT_1_PRICE = 999.99;

    @Container
    @ServiceConnection
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void testCreateProduct() {
        ProductDto productDto = createTestProductDto();

        ResponseEntity<Product> responseEntity = restTemplate.postForEntity(
                "/api/products",
                productDto,
                Product.class
        );

        Product addedProduct = responseEntity.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(addedProduct);
        Assertions.assertNotNull(addedProduct.getId());
        Assertions.assertEquals(productDto.getName(), addedProduct.getName());
        productId = addedProduct.getId();
    }

    @Test
    @Order(2)
    void testGetAllProducts() {

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){}
        );

        List<Product> products = response.getBody();
        Assertions.assertNotNull(products);
        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    void testGetProductById() {

        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/{productId}",
                HttpMethod.GET,
                null,
                Product.class,
                productId
        );

        Product addedProduct = response.getBody();
        Assertions.assertNotNull(addedProduct);
        Assertions.assertEquals(addedProduct.getId(), productId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    void testUpdateProduct() {
        ProductDto updatedProductDto = createTestProductDto();
        updatedProductDto.setPrice(9.99);

        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/{productId}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedProductDto),
                Product.class,
                productId
        );

        Product updatedProduct = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals(updatedProduct.getPrice(), updatedProductDto.getPrice());
    }

    @Test
    @Order(4)
    void testDeleteProduct() {

        HttpStatusCode getStatusCode = restTemplate.exchange(
                "/api/products/{productId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                productId
        ).getStatusCode();

        Assertions.assertEquals(HttpStatus.NO_CONTENT, getStatusCode);
    }

    private static ProductDto createTestProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName(TEST_PRODUCT_1_NAME);
        productDto.setPrice(TEST_PRODUCT_1_PRICE);
        return productDto;
    }
}
