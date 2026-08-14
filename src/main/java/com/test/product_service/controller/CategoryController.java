package com.test.product_service.controller;

import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.category.AddUpdateCategoryRequestDTO;
import com.test.product_service.dto.request.category.SearchCategoryRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.category.GetCategoryResponseDTO;
import com.test.product_service.service.CategoryService;
import com.test.product_service.uttils.enums.CategorySortField;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Category Management",
        description = "APIs for managing product categories including create, update, delete and retrieval operations"
)
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get all categories",
            description = "Returns paginated list of categories with sorting support"
    )
    @GetMapping("/get-all-categories")
    public ResponseEntity<ApiResponse<PageResponse<GetCategoryResponseDTO>>> getAllCategories(
            @ModelAttribute SearchCategoryRequestDTO searchCategoryRequestDTO,
            @RequestParam(defaultValue = "0")@PositiveOrZero(message = "Page number cannot be negative") int pageNumber,
            @RequestParam(defaultValue = "10") @Positive(message = "Size must be greater than 0") int size,
            @RequestParam(defaultValue = "ID") CategorySortField sortBy,
            @RequestParam(defaultValue = "ASC") SortDirection direction
    ){
        return ResponseEntity.ok(categoryService.getAllCategories(searchCategoryRequestDTO,pageNumber, size, sortBy, direction));
    }

    @Operation(
            summary = "Get category by ID",
            description = "Returns category details using its unique identifier"
    )
    @GetMapping("/get-category-by-id/{id}")
    public ResponseEntity<ApiResponse<GetCategoryResponseDTO>> getCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates a new category and returns generated category ID"
    )
    @PostMapping("/add-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> addCategory(@Valid @RequestBody AddUpdateCategoryRequestDTO addCategoryRequestDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.addCategory(addCategoryRequestDTO));
    }

    @Operation(
            summary = "Delete a category",
            description = "Permanently Deletes a category using category ID"
    )
    @DeleteMapping("/remove-category/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> removeCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.removeCategoryById(id));
    }

    @Operation(
            summary = "Soft Delete a category",
            description = "Soft deletes a category using category ID"
    )
    @DeleteMapping("/remove-category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> softRemoveCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.softRemoveCategoryById(id));
    }

    @Operation(
            summary = "Restore a category",
            description = "Restore Soft deleted category using category ID"
    )
    @PatchMapping("/restore-category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> restoreCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.restoreCategoryById(id));
    }

    @Operation(
            summary = "Get all deleted categories",
            description = "Returns paginated list of deleted categories with sorting support"
    )
    @GetMapping("/get-deleted-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<GetCategoryResponseDTO>>> getDeletedCategory(
            @ModelAttribute SearchCategoryRequestDTO searchCategoryRequestDTO,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Page number cannot be negative") int pageNumber,
            @RequestParam(defaultValue = "10") @Positive(message = "Size must be greater than 0") int size,
            @RequestParam(defaultValue = "ID") CategorySortField sortBy,
            @RequestParam(defaultValue = "ASC") SortDirection direction
    ){
        return ResponseEntity.ok(categoryService.getDeletedCategory(searchCategoryRequestDTO,pageNumber, size, sortBy, direction));
    }

    @Operation(
            summary = "Get Deleted category by ID",
            description = "Returns deleted category details using its unique identifier"
    )
    @GetMapping("/get-deleted-category-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GetCategoryResponseDTO>> getDeletedCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
        return ResponseEntity.ok(categoryService.getDeletedCategoryById(id));
    }

    @Operation(
            summary = "Update category details",
            description = "Updates an existing category"
    )
    @PatchMapping("/update-category-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GetCategoryResponseDTO>> updateCategoryById(@PathVariable @Positive(message = "Id must be greater than 0") Long id,@Valid @RequestBody AddUpdateCategoryRequestDTO  updateCategoryRequestDTO){
        return ResponseEntity.ok(categoryService.updateCategoryById(id,updateCategoryRequestDTO));
    }
}
