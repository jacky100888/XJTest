package com.example.demo.service;

import com.example.demo.bean.model.MemberOrderCountDTO;
import com.example.demo.bean.request.OrderCreateRequest;
import com.example.demo.bean.response.OrderResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.bean.model.Order;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    // 創建訂單
    OrderResponse createOrder(OrderCreateRequest request);

    // 根據ID查詢訂單
    OrderResponse getOrderById(Long id);

    // 根據訂單編號查詢訂單
    OrderResponse getOrderByOrderNumber(String orderNumber);

    // 根據會員ID查詢訂單（分頁）
    PageResponse<OrderResponse> getOrdersByMemberId(Long memberId, Pageable pageable);

    // 根據產品名稱查詢訂單（分頁）
    PageResponse<OrderResponse> getOrdersByProductName(String productName, Pageable pageable);

    // 根據日期範圍查詢訂單（分頁）
    PageResponse<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 查詢所有訂單
    PageResponse<OrderResponse> getAllOrders(Pageable pageable);

    // 更新訂單狀態
    OrderResponse updateOrderStatus(Long id, Order.OrderStatus status);

    // 取消訂單
    OrderResponse cancelOrder(Long id);

    // 刪除訂單
    void deleteOrder(Long id);

    // 統計訂單數量大於指定數量的會員
    List<MemberOrderCountDTO> getMembersWithOrderCountGreaterThan(Long count);
}