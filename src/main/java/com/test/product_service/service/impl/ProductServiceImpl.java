package com.test.product_service.service.impl;

import com.test.product_service.config.PaginationProperties;
import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.product.AddProductRequestDTO;
import com.test.product_service.dto.request.product.SearchProductRequestDTO;
import com.test.product_service.dto.request.product.UpdateProductRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.product.GetProductResponseDTO;
import com.test.product_service.entity.Category;
import com.test.product_service.entity.Product;
import com.test.product_service.exception.custom_exception.DuplicateResourceException;
import com.test.product_service.exception.custom_exception.ResourceNotFoundException;
import com.test.product_service.mapper.PagedResponseMapper;
import com.test.product_service.mapper.product.ProductMapper;
import com.test.product_service.repository.ProductRepo;
import com.test.product_service.service.ProductService;
import com.test.product_service.specification.product.ProductSpecificationBuilder;
import com.test.product_service.uttils.VerifyResource;
import com.test.product_service.uttils.enums.ProductSortField;
import com.test.product_service.uttils.enums.SortDirection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.test.product_service.mapper.product.ProductMapper.toGetProductResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final VerifyResource verifyResource;
    private final PaginationProperties paginationProperties;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<GetProductResponseDTO>> getAllProducts(SearchProductRequestDTO searchProductRequestDTO, int pageNumber, int size, ProductSortField sortBy, SortDirection direction) {

        // Creating Sort
        Sort sort = direction == SortDirection.DESC ?
                Sort.by(sortBy.getProductSortValue()).descending()
                :
                Sort.by(sortBy.getProductSortValue()).ascending();

        int pageSize =  Math.min(size, paginationProperties.maxPageSize());
        if(size < paginationProperties.defaultPageSize()) pageSize = paginationProperties.defaultPageSize();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Specification<Product> specification = ProductSpecificationBuilder.buildActive(searchProductRequestDTO);

        Page<Product> page = productRepo.findAll(specification,pageable);
        List<Product> products = page.getContent(); // actual data

        List<GetProductResponseDTO> listDto =  ProductMapper.toGetProductResponseDTO(products);

        log.info("List of Products : {}", listDto);
        return ApiResponse.<PageResponse<GetProductResponseDTO>>builder()
                .success(true)
                .message("Product List fetched successfully")
                .data(PagedResponseMapper.toPageResponse(listDto, page))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GetProductResponseDTO> getProductById(Long id){
        Product product = verifyResource.verifyOrGetProductById(id);
        log.info("Successfully Get product with id : {}", product.getId());
        return ApiResponse.<GetProductResponseDTO>builder()
                .success(true)
                .message("Product fetched successfully with id : "+ product.getId())
                .data(toGetProductResponseDTO(product))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> addProduct(AddProductRequestDTO addProductRequestDTO) {
        Category category = verifyResource.verifyOrGetCategoryById(addProductRequestDTO.categoryId());
        log.info("Creating product: {}", addProductRequestDTO.productName());
        Product product = ProductMapper.toProductEntity(addProductRequestDTO, category);
        if(productRepo.existsByProductNameIgnoreCaseAndDeletedAtIsNull(
                addProductRequestDTO.productName())){
            throw new DuplicateResourceException(
                    "Product already exists with name: " + addProductRequestDTO.productName(),
                    "PRODUCT_ALREADY_EXISTS");
        }

        product.setCreatedBy(verifyResource.getCurrentUserId());
        productRepo.save(product);
        log.info("Product created successfully. ProductId={}", product.getId());
        return  ApiResponse.<Long>builder()
                .success(true)
                .message("Product added successfully with id : " +  product.getId())
                .data(product.getId())
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> removeProductById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException ("No Product Found with the given id :"+ id, "PRODUCT_NOT_FOUND"));
        log.info("Deleting product with id {}", id);
            productRepo.deleteById(id);
            return    ApiResponse.<Long>builder()
                    .success(true)
                    .message("Product removed successfully with id : " +  product.getId())
                    .data(null)
                    .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> softRemoveProductById(Long id) {
        Product product = verifyResource.verifyOrGetProductById(id);
        log.info("Soft Deleting product with id {}", id);
        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(false);
        product.setUpdatedBy(verifyResource.getCurrentUserId());
        productRepo.save(product);

        return    ApiResponse.<Long>builder()
                .success(true)
                .message("Product removed successfully with id : " +  product.getId())
                .data(null)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> restoreProductById(Long id) {
        Product product = productRepo.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new  ResourceNotFoundException ("No deleted product found with id : " + id,"PRODUCT_NOT_FOUND"));
        log.info("Restore product with id {}", id);
        product.setDeletedAt(null);
        product.setIsActive(true);
        product.setUpdatedBy(verifyResource.getCurrentUserId());
        productRepo.save(product);

        return    ApiResponse.<Long>builder()
                .success(true)
                .message("Product restored successfully with id : " +  product.getId())
                .data(null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GetProductResponseDTO> getDeletedProductById(Long id) {
        Product product = productRepo.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("No deleted Product Found with the given id :"+ id, "PRODUCT_NOT_FOUND"));

        log.info("Successfully Get deleted product with id : {}", product.getId());
        return ApiResponse.<GetProductResponseDTO>builder()
                .success(true)
                .message("Deleted Product fetched successfully with id : "+ product.getId())
                .data(toGetProductResponseDTO(product))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<GetProductResponseDTO>> getDeletedProduct(SearchProductRequestDTO searchProductRequestDTO,int pageNumber, int size, ProductSortField sortBy, SortDirection direction) {
        // Creating Sort
        Sort sort = direction == SortDirection.DESC ?
                Sort.by(sortBy.getProductSortValue()).descending()
                :
                Sort.by(sortBy.getProductSortValue()).ascending();

        int pageSize =  Math.min(size, paginationProperties.maxPageSize());
        if(size < paginationProperties.defaultPageSize()) pageSize = paginationProperties.defaultPageSize();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Specification<Product> specification = ProductSpecificationBuilder.buildDeleted(searchProductRequestDTO);

        Page<Product> page = productRepo.findAll(specification,pageable);
        List<Product> products = page.getContent(); // actual data

        List<GetProductResponseDTO> listDto =  ProductMapper.toGetProductResponseDTO(products);

        log.info("List of Products : {}", listDto);
        return ApiResponse.<PageResponse<GetProductResponseDTO>>builder()
                .success(true)
                .message("Deleted Product List fetched successfully")
                .data(PagedResponseMapper.toPageResponse(listDto, page))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<GetProductResponseDTO> updateProductById(Long id, UpdateProductRequestDTO updateProductRequestDTO) {
        Product product = verifyResource.verifyOrGetProductById(id);
        // duplicate names check
        if(productRepo.existsByProductNameIgnoreCaseAndDeletedAtIsNull(
                updateProductRequestDTO.productName())){
            throw new DuplicateResourceException(
                    "Product already exists with name: " + updateProductRequestDTO.productName(),
                    "PRODUCT_ALREADY_EXISTS");
        }
        log.info("Updating product with id {}", id);

        product = ProductMapper.updateProductFormDTO(updateProductRequestDTO, product);
        if(updateProductRequestDTO.categoryId() != null){
            Category category = verifyResource.verifyOrGetCategoryById(updateProductRequestDTO.categoryId());
            product.setCategory(category);
        }
        product.setUpdatedBy(verifyResource.getCurrentUserId());
        productRepo.save(product);

        log.info("Product updated successfully. ProductId={}", product.getId());
        return   ApiResponse.<GetProductResponseDTO>builder()
                .success(true)
                .message("Product update successfully with id : " +  product.getId())
                .data(toGetProductResponseDTO(product))
                .build();

    }

}
