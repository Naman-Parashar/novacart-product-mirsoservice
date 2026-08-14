package com.test.product_service.service;

import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.product.AddProductRequestDTO;
import com.test.product_service.dto.request.product.SearchProductRequestDTO;
import com.test.product_service.dto.request.product.UpdateProductRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.product.GetProductResponseDTO;
import com.test.product_service.uttils.enums.ProductSortField;
import com.test.product_service.uttils.enums.SortDirection;

public interface ProductService {
    ApiResponse<PageResponse<GetProductResponseDTO>> getAllProducts(SearchProductRequestDTO searchProductRequestDTO,int pageNumber, int size, ProductSortField sortBy, SortDirection direction);
    ApiResponse<GetProductResponseDTO> getProductById(Long id);
    ApiResponse<Long> addProduct(AddProductRequestDTO addProductRequestDTO);
    ApiResponse<Long> removeProductById(Long id);
    ApiResponse<Long>softRemoveProductById(Long id);
    ApiResponse<Long>restoreProductById(Long id);
    ApiResponse<GetProductResponseDTO> getDeletedProductById(Long id);
    ApiResponse<PageResponse<GetProductResponseDTO>>getDeletedProduct(SearchProductRequestDTO searchProductRequestDTO,int pageNumber, int size, ProductSortField sortBy, SortDirection direction);
    ApiResponse<GetProductResponseDTO> updateProductById( Long id, UpdateProductRequestDTO updateProductRequestDTO);
}
