package com.example.demo.bean.model;

/**
 * 會員訂單數量 DTO
 */
public interface MemberOrderCountDTO {
    
    /**
     * 獲取會員ID
     * @return 會員ID
     */
    Long getMemberId();
    
    /**
     * 獲取會員用戶名
     * @return 會員用戶名
     */
    String getUsername();
    
    /**
     * 獲取會員電子郵件
     * @return 會員電子郵件
     */
    String getEmail();
    
    /**
     * 獲取訂單數量
     * @return 訂單數量
     */
    Long getOrderCount();
}