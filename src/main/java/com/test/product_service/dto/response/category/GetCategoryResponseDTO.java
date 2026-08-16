package com.test.product_service.dto.response.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(
        name = "GetCategoryResponseDTO",
        description = "Category details returned to the client"
)
@Builder
public record GetCategoryResponseDTO(

        @Schema(
                description = "Unique identifier of the category",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Name of the category",
                example = "Electronics"
        )
        String categoryName,

        @Schema(
                description = "Timestamp when the category was created",
                example = "2026-07-17T10:30:15"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Timestamp when the category was last updated",
                example = "2026-07-18T14:45:20"
        )
        LocalDateTime updatedAt,

        @Schema(
                description = "ID of the user who created the category",
                example = "1"
        )
        Long createdBy,

        @Schema(
                description = "ID of the user who last updated the category",
                example = "2"
        )
        Long updatedBy,

        @Schema(
                description = "Whether the category is active",
                example = "true"
        )
        boolean isActive,

        @Schema(
                description = "Timestamp when the category was soft deleted. Null if the category is not deleted.",
                example = "2026-07-20T09:15:30",
                nullable = true
        )
        LocalDateTime deletedAt

) {
}