package com.example.demo.service.impl;

import com.example.demo.bean.request.ProductCreateRequest;
import com.example.demo.bean.request.ProductUpdateRequest;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.bean.response.ProductResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bean.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return ProductResponse.from(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(request.getName());
                    existingProduct.setDescription(request.getDescription());
                    existingProduct.setPrice(request.getPrice());
                    existingProduct.setStock(request.getStock());
                    existingProduct.setUpdatedAt(LocalDateTime.now());

                    return ProductResponse.from(productRepository.save(existingProduct));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id).map(ProductResponse::from).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProductsByNameContaining(String name, Pageable pageable) {
        return PageResponse.from(productRepository.findByNameContaining(name, pageable).map(ProductResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        return PageResponse.from(productRepository.findAll(pageable).map(ProductResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return PageResponse.from(productRepository.findByPriceBetween(minPrice, maxPrice, pageable).map(ProductResponse::from));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }
}