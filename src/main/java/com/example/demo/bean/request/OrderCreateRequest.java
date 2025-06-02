package com.example.demo.bean.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 訂單創建請求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    
    @NotNull(message = "會員ID不能為空")
    private Long memberId;

    @Valid
    @NotEmpty(message = "訂單項目不能為空")
    private List<OrderItemRequest> items;

    /**
     * 訂單項目請求 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        
        @NotNull(message = "產品ID不能為空")
        private Long productId;
        
        @NotNull(message = "數量不能為空")
        @Min(value = 1, message = "數量必須大於0")
        private Integer quantity;
    }
}