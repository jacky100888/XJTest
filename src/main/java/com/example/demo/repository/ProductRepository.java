package com.example.demo.repository;

import com.example.demo.bean.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // 根據產品名稱查詢產品
    List<Product> findByName(String name);
    
    // 根據產品名稱模糊查詢產品（分頁）
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    // 根據價格範圍查詢產品（分頁）
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}