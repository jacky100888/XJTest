package com.example.demo.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通用 API 包裝類
 * @param <T> 類型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * 狀態碼
     */
    private int status;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 資料
     */
    private T data;
    
    /**
     * 時間戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 創建成功回傳（帶自定義狀態碼和消息）
     * @param status 狀態碼
     * @param data 資料
     * @param <T> 資料類型
     * @return 成功回傳
     */
    public static <T> ApiResponse<T> success(int status , T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message("操作成功")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * 創建成功回傳（無資料）
     * @param status 狀態碼
     * @param <T> 資料類型
     * @return 成功回傳
     */
    public static <T> ApiResponse<T> success(int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message("操作成功")
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * 創建錯誤回傳
     * @param status 狀態碼
     * @param message 錯誤消息
     * @param <T> 資料類型
     * @return 錯誤回傳
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}