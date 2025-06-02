package com.example.demo.service.impl;

import com.example.demo.bean.model.MemberOrderCountDTO;
import com.example.demo.bean.request.OrderCreateRequest;
import com.example.demo.bean.response.OrderResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bean.model.Member;
import com.example.demo.bean.model.Order;
import com.example.demo.bean.model.OrderItem;
import com.example.demo.bean.model.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        // 查詢會員
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", request.getMemberId()));

        // 創建訂單
        String orderNumber = generateOrderNumber();
        Order order = new Order(member, orderNumber);

        // 添加訂單項目
        for (OrderCreateRequest.OrderItemRequest item : request.getItems()) {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();

            // 查詢產品
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

            // 檢查庫存
            if (product.getStock() < quantity) {
                throw new InsufficientStockException(productId, quantity, product.getStock());
            }

            // 減少庫存
            product.decreaseStock(quantity);
            productRepository.save(product);

            // 創建訂單項目
            OrderItem orderItem = new OrderItem(product, quantity);
            order.addOrderItem(orderItem);
        }

        // 保存訂單
        return OrderResponse.from(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(status);
                    existingOrder.updateTimestamp();
                    return OrderResponse.from(orderRepository.save(existingOrder));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderResponse::from).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderNumber", orderNumber);
        }
        return OrderResponse.from(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersByMemberId(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
        return PageResponse.from(orderRepository.findByMember(member, pageable).map(OrderResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return PageResponse.from(orderRepository.findByOrderDateBetween(startDate, endDate, pageable).map(OrderResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersByProductName(String productName, Pageable pageable) {
        return PageResponse.from(orderRepository.findByProductName(productName, pageable).map(OrderResponse::from));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    // 只有待處理或處理中的訂單可以取消
                    if (existingOrder.getStatus() == Order.OrderStatus.PENDING ||
                            existingOrder.getStatus() == Order.OrderStatus.PROCESSING) {
                        existingOrder.setStatus(Order.OrderStatus.CANCELLED);
                        existingOrder.updateTimestamp();

                        // 恢復庫存
                        for (OrderItem item : existingOrder.getOrderItems()) {
                            Product product = item.getProduct();
                            if (product != null) {
                                product.setStock(product.getStock() + item.getQuantity());
                                productRepository.save(product);
                            }
                        }

                        return OrderResponse.from(orderRepository.save(existingOrder));
                    } else {
                        throw new BusinessException("Cannot cancel order with status: " + existingOrder.getStatus());
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberOrderCountDTO> getMembersWithOrderCountGreaterThan(Long count) {
        return orderRepository.findMembersWithOrderCountGreaterThan(count);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getAllOrders(Pageable pageable) {
        return PageResponse.from(orderRepository.findAll(pageable).map(OrderResponse::from));
    }

    // 生成訂單編號
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}