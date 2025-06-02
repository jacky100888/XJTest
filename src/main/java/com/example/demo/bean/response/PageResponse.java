package com.example.demo.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 通用分頁類
 * @param <T> 類型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    /**
     * 當前頁
     */
    private List<T> content;
    
    /**
     * 當前頁碼（從0開始）
     */
    private int currentPage;
    
    /**
     * 每頁大小
     */
    private int pageSize;
    
    /**
     * 總記錄數
     */
    private long totalItems;
    
    /**
     * 總頁數
     */
    private int totalPages;
    
    /**
     * 是否為第一頁
     */
    private boolean isFirst;
    
    /**
     * 是否為最後一頁
     */
    private boolean isLast;
    
    /**
     * 是否有下一頁
     */
    private boolean hasNext;
    
    /**
     * 是否有上一頁
     */
    private boolean hasPrevious;
    
    /**
     * 從Spring Data的Page對象創建PageResponse
     * @param page Spring Data的Page對象
     * @param <T> 類型
     * @return PageResponse對象
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}