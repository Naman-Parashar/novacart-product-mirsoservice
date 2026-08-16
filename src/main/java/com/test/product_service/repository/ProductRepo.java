package com.test.product_service.repository;

import com.test.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);
    Optional<Product> findByIdAndDeletedAtIsNotNull(Long id);
    boolean existsByProductNameIgnoreCaseAndDeletedAtIsNull(String productName);
}
