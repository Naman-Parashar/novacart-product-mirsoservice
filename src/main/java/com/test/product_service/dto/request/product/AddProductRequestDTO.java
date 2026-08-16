package com.test.product_service.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(
        name = "AddProductRequestDTO",
        description = "Request payload for creating a new product"
)
public record AddProductRequestDTO(

        @Schema(
                description = "Name of the product",
                example = "Apple iPhone 16",
                minLength = 2,
                maxLength = 100
        )
        @NotBlank(message = "Product name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Product name must be between 2 and 100 characters"
        )
        String productName,

        @Schema(
                description = "URL of the product image",
                example = "https://example.com/images/iphone16.jpg",
                maxLength = 500
        )
        @Size(
                max = 500,
                message = "Product image URL cannot exceed 500 characters"
        )
        String productImageUrl,

        @Schema(
                description = "Brand of the product",
                example = "Apple",
                maxLength = 100
        )
        @Size(
                max = 100,
                message = "Product brand cannot exceed 100 characters"
        )
        String productBrand,

        @Schema(
                description = "Detailed description of the product",
                example = "Latest Apple smartphone with A18 chip and 256GB storage.",
                maxLength = 2000
        )
        @Size(
                max = 2000,
                message = "Product description cannot exceed 2000 characters"
        )
        String productDescription,

        @Schema(
                description = "Price of the product",
                example = "79999.99"
        )
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        @Digits(
                integer = 8,
                fraction = 2,
                message = "Price can have up to 8 digits and 2 decimal places"
        )
        BigDecimal price,

        @Schema(
                description = "Available stock quantity",
                example = "50",
                minimum = "0"
        )
        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @Schema(
                description = "Whether the product is active",
                example = "true"
        )
        @NotNull(message = "Product active status is required")
        Boolean isActive,

        @Schema(
                description = "ID of the category to which the product belongs",
                example = "1",
                minimum = "1"
        )
        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be greater than 0")
        Long categoryId

) {
}