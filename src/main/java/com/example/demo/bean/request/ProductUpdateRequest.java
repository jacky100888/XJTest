package com.example.demo.bean.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 產品更新請求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    
    @NotBlank(message = "產品名稱不能為空")
    @Size(max = 100, message = "產品名稱長度不能超過100個字符")
    private String name;
    
    @Size(max = 500, message = "產品描述長度不能超過500個字符")
    private String description;
    
    @NotNull(message = "產品價格不能為空")
    @DecimalMin(value = "0.01", message = "產品價格必須大於0")
    private BigDecimal price;
    
    @NotNull(message = "產品庫存不能為空")
    @Min(value = 0, message = "產品庫存不能小於0")
    private Integer stock;
}