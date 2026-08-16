package com.test.product_service.repository;

import com.test.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByIdAndDeletedAtIsNull(Long id);
    Optional<Category> findByIdAndDeletedAtIsNotNull(Long id);
    boolean existsByCategoryNameIgnoreCaseAndDeletedAtIsNull(String categoryName);
    boolean existsByCategoryNameIgnoreCaseAndIdNotAndDeletedAtIsNull(String categoryName, Long id);
}
