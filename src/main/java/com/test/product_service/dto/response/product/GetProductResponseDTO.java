package com.test.product_service.dto.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        name = "GetProductResponseDTO",
        description = "Product details returned to the client"
)
@Builder
public record GetProductResponseDTO(

        @Schema(
                description = "Unique identifier of the product",
                example = "101"
        )
        Long id,

        @Schema(
                description = "Name of the product",
                example = "Apple iPhone 16"
        )
        String productName,

        @Schema(
                description = "URL of the product image",
                example = "https://example.com/images/iphone16.jpg"
        )
        String productImageUrl,

        @Schema(
                description = "Brand of the product",
                example = "Apple"
        )
        String productBrand,

        @Schema(
                description = "Average rating of the product",
                example = "4.7"
        )
        BigDecimal rating,

        @Schema(
                description = "Detailed description of the product",
                example = "Latest Apple smartphone with A18 chip and 256GB storage."
        )
        String productDescription,

        @Schema(
                description = "Price of the product",
                example = "79999.99"
        )
        BigDecimal price,

        @Schema(
                description = "Available stock quantity",
                example = "50"
        )
        Integer stockQuantity,

        @Schema(
                description = "Whether the product is active",
                example = "true"
        )
        Boolean isActive,

        @Schema(
                description = "ID of the user who created the product",
                example = "1"
        )
        Long createdBy,

        @Schema(
                description = "ID of the user who last updated the product",
                example = "2"
        )
        Long updatedBy,

        @Schema(
                description = "Timestamp when the product was created",
                example = "2026-07-17T10:30:15"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Timestamp when the product was last updated",
                example = "2026-07-18T14:45:20"
        )
        LocalDateTime updatedAt,

        @Schema(
                description = "Timestamp when the product was soft deleted. Null if the product is not deleted.",
                example = "2026-07-20T09:15:30",
                nullable = true
        )
        LocalDateTime deletedAt,

        @Schema(
                description = "Unique identifier of the category",
                example = "1"
        )
        Long categoryId,

        @Schema(
                description = "Name of the category",
                example = "Electronics"
        )
        String categoryName

) {
}