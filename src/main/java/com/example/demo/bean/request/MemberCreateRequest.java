package com.example.demo.bean.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 會員創建請求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {
    
    @NotBlank(message = "用戶名不能為空")
    @Size(min = 3, max = 50, message = "用戶名長度必須在3到50個字符之間")
    private String username;
    
    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "電子郵件格式不正確")
    private String email;
    
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 100, message = "密碼長度必須在6到100個字符之間")
    private String password;
    
    @Size(max = 20, message = "電話號碼長度不能超過20個字符")
    private String phone;
}