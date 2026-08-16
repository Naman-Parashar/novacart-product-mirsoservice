package com.test.product_service.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(
        name = "UpdateProductRequestDTO",
        description = "Request payload for partially updating an existing product"
)
public record UpdateProductRequestDTO(

        @Schema(
                description = "Updated name of the product",
                example = "Apple iPhone 16 Pro",
                minLength = 2,
                maxLength = 100
        )
        @Size(
                min = 2,
                max = 100,
                message = "Product name must be between 2 and 100 characters"
        )
        String productName,

        @Schema(
                description = "Updated URL of the product image",
                example = "https://example.com/images/iphone16-pro.jpg",
                maxLength = 500
        )
        @Size(
                max = 500,
                message = "Product image URL cannot exceed 500 characters"
        )
        String productImageUrl,

        @Schema(
                description = "Updated brand of the product",
                example = "Apple",
                maxLength = 100
        )
        @Size(
                max = 100,
                message = "Product brand cannot exceed 100 characters"
        )
        String productBrand,

        @Schema(
                description = "Updated product rating",
                example = "4.8",
                minimum = "0",
                maximum = "5"
        )
        @PositiveOrZero(message = "Rating cannot be negative")
        @DecimalMax(
                value = "5.0",
                message = "Rating cannot be greater than 5"
        )
        BigDecimal rating,

        @Schema(
                description = "Updated product description",
                example = "Latest Apple smartphone with A18 chip, 256GB storage, and improved camera.",
                maxLength = 2000
        )
        @Size(
                max = 2000,
                message = "Product description cannot exceed 2000 characters"
        )
        String productDescription,

        @Schema(
                description = "Updated product price",
                example = "89999.99"
        )
        @Positive(message = "Price must be greater than 0")
        @Digits(
                integer = 8,
                fraction = 2,
                message = "Price can have up to 8 digits and 2 decimal places"
        )
        BigDecimal price,

        @Schema(
                description = "Updated available stock quantity",
                example = "75",
                minimum = "0"
        )
        @PositiveOrZero(message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @Schema(
                description = "Updated active status of the product",
                example = "true"
        )
        Boolean isActive,

        @Schema(
                description = "Updated category ID for the product",
                example = "2",
                minimum = "1"
        )
        @Positive(message = "Category ID must be greater than 0")
        Long categoryId

) {
}