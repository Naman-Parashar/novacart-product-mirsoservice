package com.test.product_service.uttils;

import com.test.product_service.entity.Category;
import com.test.product_service.entity.Product;
import com.test.product_service.exception.custom_exception.ResourceNotFoundException;
import com.test.product_service.repository.CategoryRepo;
import com.test.product_service.repository.ProductRepo;
import com.test.product_service.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerifyResource {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public Product verifyOrGetProductById(Long id){
        log.info("Verifying product with id={}", id);
        return  productRepo.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Product Found with the given id :"+ id, "PRODUCT_NOT_FOUND"));
    }

    public Category verifyOrGetCategoryById(Long id){
        log.info("Verifying category with id={}", id);
         return categoryRepo.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException ("No Category Found  with the given id : "+ id,"CATEGORY_NOT_FOUND"));

    }

    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        throw new ResourceNotFoundException("User not authenticated", "USER_NOT_AUTHENTICATED");
    }
}
