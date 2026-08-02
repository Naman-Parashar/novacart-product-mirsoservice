package com.test.product_service.controller;

import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.product.AddProductRequestDTO;
import com.test.product_service.dto.request.product.SearchProductRequestDTO;
import com.test.product_service.dto.request.product.UpdateProductRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.product.GetProductResponseDTO;
import com.test.product_service.service.impl.ProductServiceImpl;
import com.test.product_service.uttils.enums.ProductSortField;
import com.test.product_service.uttils.enums.SortDirection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Product Management",
        description = "APIs for managing products including create, update, delete, retrieve and pagination operations"
)
public class ProductController {

    private final ProductServiceImpl productService;

    @Operation(
            summary = "Get all products",
            description = "Returns paginated list of products with sorting support"
    )
    @GetMapping("/get-all-products")
    public ResponseEntity<ApiResponse<PageResponse<GetProductResponseDTO>>> getAllProducts(
            @ModelAttribute SearchProductRequestDTO searchProductRequestDTO,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Page number cannot be negative") int pageNumber,
            @RequestParam(defaultValue = "10") @Positive(message = "Size must be greater than 0") int size,
            @RequestParam(defaultValue = "ID") ProductSortField sortBy,
            @RequestParam(defaultValue = "ASC") SortDirection direction){
        return ResponseEntity.ok(productService.getAllProducts(searchProductRequestDTO,pageNumber, size, sortBy, direction));
    }

    @Operation(
            summary = "Get product by ID",
            description = "Returns a single product using its unique identifier"
    )
    @GetMapping("/get-product-by-id/{id}")
    public ResponseEntity<ApiResponse<GetProductResponseDTO>> getProductById(@PathVariable @Positive(message = "Id must be greater than 0")  Integer id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product and returns the generated product ID"
    )
    @PostMapping("/add-product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> addProduct(@Valid @RequestBody AddProductRequestDTO addProductRequestDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(addProductRequestDTO));
    }

    @Operation(
            summary = "Delete a product",
            description = "Permanently deletes a product using product ID"
    )
    @DeleteMapping("/remove-product/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> removeProductById(@PathVariable @Positive(message = "Id must be greater than 0") Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.removeProductById(id));
    }

    @Operation(
            summary = "Soft Delete a product",
            description = "Soft deletes a product using product ID"
    )
    @DeleteMapping("/remove-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> softRemoveProductById(@PathVariable @Positive(message = "Id must be greater than 0") Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.softRemoveProductById(id));
    }

    @Operation(
            summary = "Restore a product",
            description = "Restore Soft deleted product using product ID"
    )
    @PatchMapping("/restore-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> restoreProductById(@PathVariable @Positive(message = "Id must be greater than 0") Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.restoreProductById(id));
    }

    @Operation(
            summary = "Get all deleted products",
            description = "Returns paginated list of deleted products with sorting support"
    )
    @GetMapping("/get-deleted-product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<GetProductResponseDTO>>> getDeletedProduct(
            @ModelAttribute SearchProductRequestDTO searchProductRequestDTO,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Page number cannot be negative") int pageNumber,
            @RequestParam(defaultValue = "10") @Positive(message = "Size must be greater than 0") int size,
            @RequestParam(defaultValue = "ID") ProductSortField sortBy,
            @RequestParam(defaultValue = "ASC") SortDirection direction
    ){
        return ResponseEntity.ok(productService.getDeletedProduct(searchProductRequestDTO,pageNumber, size, sortBy, direction));
    }

    @Operation(
            summary = "Get Deleted product by ID",
            description = "Returns a single deleted product using its unique identifier"
    )
    @GetMapping("/get-deleted-product-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GetProductResponseDTO>> getDeletedProductById(@PathVariable @Positive(message = "Id must be greater than 0")  Integer id){
        return ResponseEntity.ok(productService.getDeletedProductById(id));
    }

    @Operation(
            summary = "Update product details",
            description = "Updates selected fields of an existing product"
    )
    @PatchMapping("/update-product-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GetProductResponseDTO>> updateProductById(@PathVariable @Positive(message = "Id must be greater than 0") Integer id, @Valid @RequestBody UpdateProductRequestDTO updateProductRequestDTO){
        return ResponseEntity.ok(productService.updateProductById(id,updateProductRequestDTO));
    }
}
