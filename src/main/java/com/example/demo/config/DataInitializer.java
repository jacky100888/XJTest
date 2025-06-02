package com.example.demo.config;

import com.example.demo.bean.request.OrderCreateRequest;
import com.example.demo.bean.model.Member;
import com.example.demo.bean.model.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @PostConstruct
    public void initData() {
        // 初始化會員資料
        initMembers();
        // 初始化產品資料
        initProducts();
        // 初始化訂單資料
        initOrders();
    }

    private void initMembers() {
        // 檢查是否已有會員資料
        if (memberRepository.count() == 0) {
            // 創建測試會員
            Member member1 = new Member("user1", "user1@example.com", "password1", "1234567890");
            Member member2 = new Member("user2", "user2@example.com", "password2", "0987654321");
            Member member3 = new Member("user3", "user3@example.com", "password3", "1357924680");

            // 保存會員
            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);

            System.out.println("Initialized member data");
        }
    }

    private void initProducts() {
        // 檢查是否已有產品資料
        if (productRepository.count() == 0) {
            // 創建測試產品
            Product product1 = new Product("iPhone 13", "Apple iPhone 13 128GB", new BigDecimal("799.99"), 100);
            Product product2 = new Product("Samsung Galaxy S21", "Samsung Galaxy S21 5G 128GB", new BigDecimal("699.99"), 80);
            Product product3 = new Product("MacBook Pro", "Apple MacBook Pro 13-inch M1", new BigDecimal("1299.99"), 50);
            Product product4 = new Product("iPad Air", "Apple iPad Air 10.9-inch 64GB", new BigDecimal("599.99"), 70);
            Product product5 = new Product("AirPods Pro", "Apple AirPods Pro with MagSafe Charging Case", new BigDecimal("249.99"), 120);

            // 保存產品
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

            System.out.println("Initialized product data");
        }
    }

    private void initOrders() {
        // 檢查是否已有會員和產品資料
        if (memberRepository.count() > 0 && productRepository.count() > 0) {
            // 獲取會員
            Member member1 = memberRepository.findByUsername("user1").orElse(null);
            Member member2 = memberRepository.findByUsername("user2").orElse(null);

            // 獲取產品
            Product product1 = productRepository.findByName("iPhone 13").get(0);
            Product product2 = productRepository.findByName("Samsung Galaxy S21").get(0);
            Product product3 = productRepository.findByName("MacBook Pro").get(0);
            Product product4 = productRepository.findByName("iPad Air").get(0);
            Product product5 = productRepository.findByName("AirPods Pro").get(0);

            // 創建訂單1（會員1購買產品1和產品5）
            if (member1 != null) {
                List<OrderCreateRequest.OrderItemRequest> items1 = new ArrayList<>();
                items1.add(new OrderCreateRequest.OrderItemRequest(product1.getId(), 1));
                items1.add(new OrderCreateRequest.OrderItemRequest(product5.getId(), 2));

                OrderCreateRequest request1 = new OrderCreateRequest(member1.getId(), items1);
                orderService.createOrder(request1);
            }

            // 創建訂單2（會員1購買產品3）
            if (member1 != null) {
                List<OrderCreateRequest.OrderItemRequest> items2 = new ArrayList<>();
                items2.add(new OrderCreateRequest.OrderItemRequest(product3.getId(), 1));

                OrderCreateRequest request2 = new OrderCreateRequest(member1.getId(), items2);
                orderService.createOrder(request2);
            }

            // 創建訂單3（會員2購買產品2和產品4）
            if (member2 != null) {
                List<OrderCreateRequest.OrderItemRequest> items3 = new ArrayList<>();
                items3.add(new OrderCreateRequest.OrderItemRequest(product2.getId(), 1));
                items3.add(new OrderCreateRequest.OrderItemRequest(product4.getId(), 1));

                OrderCreateRequest request3 = new OrderCreateRequest(member2.getId(), items3);
                orderService.createOrder(request3);
            }

            System.out.println("Initialized order data");
        }
    }
}