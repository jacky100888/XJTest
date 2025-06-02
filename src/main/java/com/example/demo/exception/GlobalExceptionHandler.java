package com.example.demo.exception;

import com.example.demo.bean.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理自定義業務異常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(BusinessException ex, WebRequest request) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 處理資源未找到異常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * 處理參數驗證異常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> apiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "參數驗證失敗");
        apiResponse.setData(errors);
        return apiResponse;
    }

    /**
     * 處理庫存不足異常
     */
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInsufficientStockException(InsufficientStockException ex, WebRequest request) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 處理所有其他未捕獲的異常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(Exception ex, WebRequest request) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "發生內部伺服器錯誤");
    }
}