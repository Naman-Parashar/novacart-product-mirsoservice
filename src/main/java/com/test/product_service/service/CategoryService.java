package com.test.product_service.service;

import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.category.AddUpdateCategoryRequestDTO;
import com.test.product_service.dto.request.category.SearchCategoryRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.category.GetCategoryResponseDTO;
import com.test.product_service.uttils.enums.CategorySortField;
import com.test.product_service.uttils.enums.SortDirection;

public interface CategoryService {
    ApiResponse<PageResponse<GetCategoryResponseDTO>> getAllCategories(SearchCategoryRequestDTO searchCategoryRequestDTO, int pageNumber, int size, CategorySortField sortBy, SortDirection direction);
    ApiResponse<GetCategoryResponseDTO> getCategoryById(Long id);
    ApiResponse<Long> addCategory(AddUpdateCategoryRequestDTO addCategoryRequestDTO);
    ApiResponse<Long> removeCategoryById(Long id);
    ApiResponse<Long> softRemoveCategoryById(Long id);
    ApiResponse<Long> restoreCategoryById(Long id);
    ApiResponse<GetCategoryResponseDTO> getDeletedCategoryById(Long id);
    ApiResponse<PageResponse<GetCategoryResponseDTO>> getDeletedCategory(SearchCategoryRequestDTO searchCategoryRequestDTO,int pageNumber, int size,CategorySortField sortBy,SortDirection direction);
    ApiResponse<GetCategoryResponseDTO> updateCategoryById(Long id, AddUpdateCategoryRequestDTO updateCategoryRequestDTO);
}
