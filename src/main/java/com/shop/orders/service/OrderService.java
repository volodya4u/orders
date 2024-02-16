package com.shop.orders.service;

import com.shop.orders.dto.OrderDto;
import com.shop.orders.model.Order;
import com.shop.orders.model.Product;
import com.shop.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    // Create a new order
    @Transactional
    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        List<Product> products = productService.getProductsByIds(orderDto.getProductIds());
        order.setProducts(products);
        return orderRepository.save(order);
    }

    // Retrieve all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Retrieve an order by ID
    public Order getOrderById(UUID orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }

    // Update an order by ID
    @Transactional
    public Order updateOrder(UUID orderId, OrderDto updatedOrderDto) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            List<Product> products = productService.getProductsByIds(updatedOrderDto.getProductIds());
            order.setProducts(products);
            return orderRepository.save(order);
        }
        return null;
    }

    // Delete an order by ID
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    // Mongo updates are atomic, means it has own queue for updates (locks)
    // and guarantees no simultaneous updates happen to the same document
    // Add product to order
    @Transactional
    public Order addProductToOrder(UUID orderId, UUID productId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            Product product = productService.getProductById(productId);

            // check if we already have the product in order
            if (product != null && !order.getProducts().contains(product)) {
                List<Product> products = order.getProducts();
                if (products == null) {
                    order.setProducts(List.of(product));
                } else products.add(product);
                return orderRepository.save(order);
            }
        }
        return null;
    }

    // Remove product from order
    @Transactional
    public Order removeProductFromOrder(UUID orderId, UUID productId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            List<Product> products = order.getProducts();
            if (products != null) {
                products.removeIf(product -> product.getId().equals(productId));
                return orderRepository.save(order);
            }
        }
        return null;
    }
}
