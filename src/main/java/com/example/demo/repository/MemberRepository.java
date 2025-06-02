package com.example.demo.repository;

import com.example.demo.bean.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 根據用戶名查詢會員
    Optional<Member> findByUsername(String username);
    
    // 檢查用戶名是否存在
    boolean existsByUsername(String username);
    
    // 檢查電子郵件是否存在
    boolean existsByEmail(String email);
    
    // 根據用戶名模糊查詢會員（分頁）
    Page<Member> findByUsernameContaining(String username, Pageable pageable);
    
    // 根據電子郵件模糊查詢會員（分頁）
    Page<Member> findByEmailContaining(String email, Pageable pageable);
}