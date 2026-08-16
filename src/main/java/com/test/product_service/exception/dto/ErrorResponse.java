package com.test.product_service.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(description = "Error response")
@Builder
public record ErrorResponse(
         LocalDateTime timestamp,
         HttpStatus status,
         String error,
         String message,
         String path

) {
}
