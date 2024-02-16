package com.shop.orders.service;

import com.shop.orders.dto.ProductDto;
import com.shop.orders.model.Product;
import com.shop.orders.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a new product
    public Product createProduct(ProductDto productDto) {
        Product product = ProductDto.dtoToEntity(productDto);
        return productRepository.save(product);
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve a product by ID
    public Product getProductById(UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null);
    }

    // Retrieve products by IDs
    public List<Product> getProductsByIds(List<UUID> productIds) {
        return productRepository.findAllById(productIds);
    }

    // Update a product by ID
    @Transactional
    public Product updateProduct(UUID productId, ProductDto updatedProductDto) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setName(updatedProductDto.getName());
            product.setPrice(updatedProductDto.getPrice());
            return productRepository.save(product);
        }
        return null;
    }

    // Delete a product by ID
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }
}
