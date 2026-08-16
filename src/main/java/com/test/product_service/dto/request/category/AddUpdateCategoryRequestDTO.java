package com.test.product_service.dto.request.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(
        name = "AddUpdateCategoryRequestDTO",
        description = "Request payload for creating or updating a category"
)
public record AddUpdateCategoryRequestDTO(

        @Schema(
                description = "Name of the category",
                example = "Electronics",
                minLength = 2,
                maxLength = 50
        )
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 50,
                message = "Category name must be between 2 and 50 characters")
        String categoryName,

        @Schema(
                description = "Whether the category is active",
                example = "true"
        )
        @NotNull(message = "Category active status is required")
        Boolean isActive
) {
}
