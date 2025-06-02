package com.example.demo.service;

import com.example.demo.bean.request.ProductCreateRequest;
import com.example.demo.bean.request.ProductUpdateRequest;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.bean.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    // 創建新產品
    ProductResponse createProduct(ProductCreateRequest request);

    // 更新產品訊息
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    // 根據ID查詢產品
    ProductResponse getProductById(Long id);

    // 根據產品名稱模糊查詢產品（分頁）
    PageResponse<ProductResponse> getProductsByNameContaining(String name, Pageable pageable);

    // 分頁查詢產品
    PageResponse<ProductResponse> getAllProducts(Pageable pageable);

    // 根據價格範圍查詢產品（分頁）
    PageResponse<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // 刪除產品
    void deleteProduct(Long id);
}