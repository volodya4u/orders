package com.shop.orders.controller;

import com.shop.orders.dto.OrderDto;
import com.shop.orders.dto.ProductDto;
import com.shop.orders.model.Order;
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
public class OrderControllerIntegrationTest {

    private static UUID orderId;

    private static UUID productId;

    private static final String TEST_PRODUCT_1_NAME = "Laptop";
    private static final String TEST_PRODUCT_2_NAME = "Laptop2";
    private static final Double TEST_PRODUCT_1_PRICE = 999.99;

    @Container
    @ServiceConnection
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @org.junit.jupiter.api.Order(1)
    void testCreateOrder() {
        ProductDto productDto = createTestProductDto();

        ResponseEntity<Product> responseProduct = restTemplate.postForEntity(
                "/api/products",
                productDto,
                Product.class
        );

        productId = responseProduct.getBody().getId();

        OrderDto orderDto = new OrderDto();
        orderDto.setProductIds(List.of(productId));

        ResponseEntity<Order> responseOrder = restTemplate.postForEntity(
                "/api/orders",
                orderDto,
                Order.class
        );

        Order addedOrder = responseOrder.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, responseOrder.getStatusCode());
        Assertions.assertNotNull(addedOrder);
        Assertions.assertNotNull(addedOrder.getId());
        Assertions.assertEquals(responseProduct.getBody().getName(), addedOrder.getProducts().getFirst().getName());
        orderId = addedOrder.getId();
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testGetAllOrders() {

        ResponseEntity<List<Order>> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){}
        );

        List<Order> orders = response.getBody();
        Assertions.assertNotNull(orders);
        Assertions.assertFalse(orders.isEmpty());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testGetOrderById() {

        ResponseEntity<Order> response = restTemplate.exchange(
                "/api/orders/{orderId}",
                HttpMethod.GET,
                null,
                Order.class,
                orderId
        );

        Order addedOrder = response.getBody();
        Assertions.assertNotNull(addedOrder);
        Assertions.assertEquals(addedOrder.getId(), orderId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void testUpdateOrder() {
        ProductDto productDto = createTestProductDto();
        productDto.setName(TEST_PRODUCT_2_NAME);

        ResponseEntity<Product> responseProduct = restTemplate.postForEntity(
                "/api/products",
                productDto,
                Product.class
        );

        UUID product2Id = responseProduct.getBody().getId();

        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setProductIds(List.of(product2Id));

        ResponseEntity<Order> response = restTemplate.exchange(
                "/api/orders/{orderId}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedOrderDto),
                Order.class,
                orderId
        );

        Order updatedOrder = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(1, updatedOrder.getProducts().size());
        Assertions.assertEquals(updatedOrder.getProducts().getFirst().getName(), productDto.getName());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testAddProductToOrder() {

        ResponseEntity<Order> response = restTemplate.postForEntity(
                "/api/orders/{orderId}/products/{productId}",
                null,
                Order.class,
                orderId,
                productId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Order updatedOrder = response.getBody();
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(2, updatedOrder.getProducts().size());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void testRemoveProductFromOrder() {

        ResponseEntity<Order> response = restTemplate.exchange(
                "/api/orders/{orderId}/products/{productId}",
                HttpMethod.DELETE,
                null,
                Order.class,
                orderId,
                productId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Order updatedOrder = response.getBody();
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(1, updatedOrder.getProducts().size());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void testDeleteOrder() {

        HttpStatusCode getStatusCode = restTemplate.exchange(
                "/api/orders/{orderId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                orderId
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
