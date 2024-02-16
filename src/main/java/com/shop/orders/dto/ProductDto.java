package com.shop.orders.dto;

import com.shop.orders.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating or updating a product")
public class ProductDto {

    @Schema(description = "Name of the product")
    @NotNull
    private String name;

    @Schema(description = "Price of the product")
    @NotNull
    private Double price;

    public static Product dtoToEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }
}
