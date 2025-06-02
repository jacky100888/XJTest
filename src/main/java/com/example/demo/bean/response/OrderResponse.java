package com.example.demo.bean.response;

import com.example.demo.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 訂單返回 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private String orderNumber;
    private Long memberId;
    private String memberUsername;
    private String memberEmail;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    
    /**
     * 從實體轉換為 DTO
     * @param order 訂單實體
     * @return 訂單返回 DTO
     */
    public static OrderResponse from(Order order) {
        if (order == null) {
            return null;
        }
        
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberId(order.getMember() != null ? order.getMember().getId() : null)
                .memberUsername(order.getMember() != null ? order.getMember().getUsername() : null)
                .memberEmail(order.getMember() != null ? order.getMember().getEmail() : null)
                .orderItems(order.getOrderItems() != null ?
                        order.getOrderItems().stream()
                                .map(OrderItemResponse::from)
                                .collect(Collectors.toList()) :
                        null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .orderDate(order.getOrderDate())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}