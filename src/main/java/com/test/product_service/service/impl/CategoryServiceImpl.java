package com.test.product_service.service.impl;

import com.test.product_service.config.PaginationProperties;
import com.test.product_service.dto.ApiResponse;
import com.test.product_service.dto.request.category.AddUpdateCategoryRequestDTO;
import com.test.product_service.dto.request.category.SearchCategoryRequestDTO;
import com.test.product_service.dto.response.PageResponse;
import com.test.product_service.dto.response.category.GetCategoryResponseDTO;
import com.test.product_service.entity.Category;
import com.test.product_service.exception.custom_exception.DuplicateResourceException;
import com.test.product_service.exception.custom_exception.ResourceNotFoundException;
import com.test.product_service.mapper.PagedResponseMapper;
import com.test.product_service.mapper.category.CategoryMapper;
import com.test.product_service.repository.CategoryRepo;
import com.test.product_service.service.CategoryService;
import com.test.product_service.specification.category.CategorySpecificationBuilder;
import com.test.product_service.uttils.VerifyResource;
import com.test.product_service.uttils.enums.CategorySortField;
import com.test.product_service.uttils.enums.SortDirection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final VerifyResource verifyResource;
    private final PaginationProperties paginationProperties;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<GetCategoryResponseDTO>> getAllCategories(SearchCategoryRequestDTO searchCategoryRequestDTO,int pageNumber, int size, CategorySortField sortBy, SortDirection direction) {
        // Creating Sort
        Sort sort = direction == SortDirection.DESC ?
                Sort.by(sortBy.getCategorySortValue()).descending()
                :
                Sort.by(sortBy.getCategorySortValue()).ascending();

        int pageSize = Math.min(
                size,
                paginationProperties.maxPageSize()
        );
        if(size < paginationProperties.defaultPageSize()) pageSize = paginationProperties.defaultPageSize();
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);

        Specification<Category> specification = CategorySpecificationBuilder.buildActive(searchCategoryRequestDTO);

        Page<Category> page = categoryRepo.findAll(specification,pageable);
        List<Category> category = page.getContent();

        List<GetCategoryResponseDTO> listDto = CategoryMapper.toDTO(category);

        log.info("List of Category : {}", listDto);

         return ApiResponse.<PageResponse<GetCategoryResponseDTO>>builder()
                 .success(true)
                 .message("Category List fetched successfully")
                 .data(PagedResponseMapper.toPageResponse(listDto, page))
                 .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GetCategoryResponseDTO> getCategoryById(Long id) {
        Category category = verifyResource.verifyOrGetCategoryById(id);
        log.info("Successfully Get category with id : {}", category.getId());

        return ApiResponse.<GetCategoryResponseDTO>builder()
                .success(true)
                .message("Category with id : " + category.getId() +" fetched successfully")
                .data(CategoryMapper.toDTO(category))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> addCategory(AddUpdateCategoryRequestDTO addCategoryRequestDTO) {
        Category category = CategoryMapper.toEntity(addCategoryRequestDTO);
        log.info("Creating category: {}", addCategoryRequestDTO.categoryName());
        if (categoryRepo.existsByCategoryNameIgnoreCaseAndDeletedAtIsNull(
                addCategoryRequestDTO.categoryName())) {

            throw new DuplicateResourceException(
                    "Category already exists with name: " + addCategoryRequestDTO.categoryName(),
                    "CATEGORY_ALREADY_EXISTS"
            );
        }
        category.setCreatedBy(verifyResource.getCurrentUserId());
       Category savedcategory =  categoryRepo.save(category);
        log.info("Category created successfully. ProductId={}", category.getId());

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Category Added Successfully with name : "+ savedcategory.getCategoryName())
                .data(savedcategory.getId())
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> removeCategoryById(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException ("No Category Found  with the given id : "+ id,"CATEGORY_NOT_FOUND"));
        log.info("Deleting category with id {}", id);
        categoryRepo.deleteById(id);

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Category Removed Successfully with id : "+ category.getId())
                .data(null)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> softRemoveCategoryById(Long id) {
        Category category = verifyResource.verifyOrGetCategoryById(id);
        log.info("Soft Deleting category with id {}", id);
        category.setDeletedAt(LocalDateTime.now());
        category.setIsActive(false);
        category.setUpdatedBy(verifyResource.getCurrentUserId());
        categoryRepo.save(category);

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Category Removed Successfully with id : "+ category.getId())
                .data(null)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Long> restoreCategoryById(Long id) {
        Category category = categoryRepo.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new ResourceNotFoundException ("No Category Found  with the given id : "+ id,"CATEGORY_NOT_FOUND"));
        log.info("Restore category with id {}", id);
        category.setDeletedAt(null);
        category.setIsActive(true);
        category.setUpdatedBy(verifyResource.getCurrentUserId());
        categoryRepo.save(category);

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Category Restored Successfully with id : "+ id)
                .data(category.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<GetCategoryResponseDTO> getDeletedCategoryById(Long id) {
        Category category = categoryRepo.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Deleted Category Found  with the given id : "+ id,"CATEGORY_NOT_FOUND"));
        log.info("Successfully Get deleted category with id : {}", category.getId());

        return ApiResponse.<GetCategoryResponseDTO>builder()
                .success(true)
                .message("Deleted Category with id : " + category.getId() +" fetched successfully")
                .data(CategoryMapper.toDTO(category))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<GetCategoryResponseDTO>> getDeletedCategory(SearchCategoryRequestDTO searchCategoryRequestDTO,int pageNumber, int size, CategorySortField sortBy, SortDirection direction) {
        // Creating Sort
        Sort sort = direction == SortDirection.DESC ?
                Sort.by(sortBy.getCategorySortValue()).descending()
                :
                Sort.by(sortBy.getCategorySortValue()).ascending();

        int pageSize = Math.min(
                size,
                paginationProperties.maxPageSize()
        );
        if(size < paginationProperties.defaultPageSize()) pageSize = paginationProperties.defaultPageSize();

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);

        Specification<Category> specification = CategorySpecificationBuilder.buildDeleted(searchCategoryRequestDTO);

        Page<Category> page = categoryRepo.findAll(specification,pageable);
        List<Category> category = page.getContent();

        List<GetCategoryResponseDTO> listDto = CategoryMapper.toDTO(category);

        log.info("List of Category : {}", listDto);

        return ApiResponse.<PageResponse<GetCategoryResponseDTO>>builder()
                .success(true)
                .message("Deleted Category List fetched successfully")
                .data(PagedResponseMapper.toPageResponse(listDto, page))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<GetCategoryResponseDTO> updateCategoryById(Long id, AddUpdateCategoryRequestDTO updateCategoryRequestDTO) {
        if (categoryRepo.existsByCategoryNameIgnoreCaseAndIdNotAndDeletedAtIsNull(updateCategoryRequestDTO.categoryName(), id)) {
            throw new DuplicateResourceException(
                    "Category already exists with name: " + updateCategoryRequestDTO.categoryName(),
                    "CATEGORY_ALREADY_EXISTS"
            );
        }
       Category category = verifyResource.verifyOrGetCategoryById(id);
        log.info("Updating category with id {}", id);
       if(updateCategoryRequestDTO.categoryName() != null && !updateCategoryRequestDTO.categoryName().isEmpty()){
           category.setCategoryName(updateCategoryRequestDTO.categoryName());
       }
        category.setUpdatedBy(verifyResource.getCurrentUserId());
       categoryRepo.save(category);
        log.info("Category updated successfully. CategoryId={}", category.getId());
        return ApiResponse.<GetCategoryResponseDTO>builder()
                .success(true)
                .message("Category updated successfully with id : "+ id)
                .data(CategoryMapper.toDTO(category))
                .build();
    }

}
