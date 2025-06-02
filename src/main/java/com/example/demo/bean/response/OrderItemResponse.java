package com.example.demo.bean.response;

import com.example.demo.bean.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 訂單項目返回 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    
    /**
     * 從實體轉換為 DTO
     * @param orderItem 訂單項目實體
     * @return 訂單項目返回 DTO
     */
    public static OrderItemResponse from(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .productName(orderItem.getProductName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .build();
    }
}