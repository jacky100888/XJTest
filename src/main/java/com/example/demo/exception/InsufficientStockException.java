package com.example.demo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(Long productId, int requestedQuantity, int availableQuantity) {
        super(String.format("產品ID為 %d 的庫存不足，請求數量: %d，可用數量: %d", productId, requestedQuantity, availableQuantity));
    }
}