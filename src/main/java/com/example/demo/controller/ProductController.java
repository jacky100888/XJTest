package com.example.demo.controller;

import com.example.demo.bean.request.ProductCreateRequest;
import com.example.demo.bean.request.ProductUpdateRequest;
import com.example.demo.bean.response.ApiResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.bean.response.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 創建產品
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        try {
            ProductResponse productResponse = productService.createProduct(request);
            return ApiResponse.success(HttpStatus.CREATED.value(), productResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 更新產品
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        try {
            ProductResponse productResponse = productService.updateProduct(id, request);
            return ApiResponse.success(HttpStatus.OK.value(), productResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 根據ID查詢產品
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            ProductResponse productResponse = productService.getProductById(id);
            return ApiResponse.success(HttpStatus.OK.value(), productResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 查詢所有產品
    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        try {
            PageResponse<ProductResponse> pageResponse = productService.getAllProducts(pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 根據產品名稱模糊查詢產品（分頁）
    @GetMapping("/search/name")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        try {
            PageResponse<ProductResponse> pageResponse = productService.getProductsByNameContaining(name, pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 根據價格範圍查詢產品（分頁）
    @GetMapping("/search/price")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        try {
            PageResponse<ProductResponse> pageResponse = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 刪除產品
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ApiResponse.success(HttpStatus.OK.value());
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}