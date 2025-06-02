package com.example.demo.controller;

import com.example.demo.bean.model.MemberOrderCountDTO;
import com.example.demo.bean.request.OrderCreateRequest;
import com.example.demo.bean.response.ApiResponse;
import com.example.demo.bean.response.OrderResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.bean.model.Order;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 創建訂單
    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        try {
            OrderResponse orderResponse = orderService.createOrder(request);
            return ApiResponse.success(HttpStatus.CREATED.value(), orderResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 根據ID查詢訂單
    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        try {
            OrderResponse orderResponse = orderService.getOrderById(id);
            return ApiResponse.success(HttpStatus.OK.value(), orderResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 根據訂單編號查詢訂單
    @GetMapping("/number/{orderNumber}")
    public ApiResponse<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            OrderResponse orderResponse = orderService.getOrderByOrderNumber(orderNumber);
            return ApiResponse.success(HttpStatus.OK.value(), orderResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 根據會員ID查詢訂單
    @GetMapping("/member/{memberId}")
    public ApiResponse<PageResponse<OrderResponse>> getOrdersByMemberId(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        try {
            PageResponse<OrderResponse> pageResponse = orderService.getOrdersByMemberId(memberId, pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 根據產品名稱查詢訂單
    @GetMapping("/product")
    public ApiResponse<PageResponse<OrderResponse>> getOrdersByProductName(
            @RequestParam String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        try {
            PageResponse<OrderResponse> pageResponse = orderService.getOrdersByProductName(productName, pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 根據日期範圍查詢訂單
    @GetMapping("/date-range")
    public ApiResponse<PageResponse<OrderResponse>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        try {
            PageResponse<OrderResponse> pageResponse = orderService.getOrdersByDateRange(startDate, endDate, pageable);
            return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 綜合查詢訂單
    @GetMapping("/search")
    public ApiResponse<PageResponse<OrderResponse>> searchOrders(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        try {
            if (orderNumber != null && !orderNumber.isEmpty()) {
                OrderResponse orderResponse = orderService.getOrderByOrderNumber(orderNumber);
                Page<OrderResponse> singleOrderPage = new PageImpl<>(
                    List.of(orderResponse),
                    pageable,
                    1
                );
                return ApiResponse.success(HttpStatus.OK.value(), PageResponse.from(singleOrderPage));
            } else if (productName != null && !productName.isEmpty()) {
                PageResponse<OrderResponse> pageResponse = orderService.getOrdersByProductName(productName, pageable);
                return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
            } else if (startDate != null && endDate != null) {
                PageResponse<OrderResponse> pageResponse = orderService.getOrdersByDateRange(startDate, endDate, pageable);
                return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
            } else {
                PageResponse<OrderResponse> pageResponse = orderService.getAllOrders(pageable);
                return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
            }
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 更新訂單狀態
    @PutMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam Order.OrderStatus status) {
        try {
            OrderResponse orderResponse = orderService.updateOrderStatus(id, status);
            return ApiResponse.success(HttpStatus.OK.value(), orderResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 取消訂單
    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable Long id) {
        try {
            OrderResponse orderResponse = orderService.cancelOrder(id);
            return ApiResponse.success(HttpStatus.OK.value(), orderResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 刪除訂單
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ApiResponse.success(HttpStatus.OK.value());
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 統計訂單數量大於指定數量的會員
    @GetMapping("/stats/members")
    public ApiResponse<List<MemberOrderCountDTO>> getMembersWithOrderCountGreaterThan(
            @RequestParam(defaultValue = "0") Long count) {
        return ApiResponse.success(HttpStatus.OK.value(), orderService.getMembersWithOrderCountGreaterThan(count));
    }
}