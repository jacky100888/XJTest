package com.example.demo.repository;

import com.example.demo.bean.model.Member;
import com.example.demo.bean.model.Order;
import com.example.demo.bean.model.MemberOrderCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 根據訂單編號查詢訂單
    Order findByOrderNumber(String orderNumber);

    // 根據會員查詢訂單（分頁）
    Page<Order> findByMember(Member member, Pageable pageable);

    // 根據訂單日期範圍查詢訂單（分頁）
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 查詢包含特定產品名稱的訂單（分頁）
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.productName LIKE %:productName%")
    Page<Order> findByProductName(@Param("productName") String productName, Pageable pageable);

    // 查詢訂單數量大於指定數量的會員
    @Query("SELECT o.member.id as memberId, o.member.username as username, o.member.email as email, COUNT(o) as orderCount FROM Order o GROUP BY o.member.id, o.member.username, o.member.email HAVING COUNT(o) > :count")
    List<MemberOrderCountDTO> findMembersWithOrderCountGreaterThan(@Param("count") Long count);
}