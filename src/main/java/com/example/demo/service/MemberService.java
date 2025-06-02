package com.example.demo.service;

import com.example.demo.bean.request.MemberCreateRequest;
import com.example.demo.bean.request.MemberUpdateRequest;
import com.example.demo.bean.response.MemberResponse;
import com.example.demo.bean.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    // 創建新會員
    MemberResponse createMember(MemberCreateRequest request);

    // 更新會員訊息
    MemberResponse updateMember(Long id, MemberUpdateRequest request);

    // 根據ID查詢會員
    MemberResponse getMemberById(Long id);

    // 分頁查詢會員
    PageResponse<MemberResponse> getAllMembers(Pageable pageable);

    // 根據用戶名模糊查詢會員（分頁）
    PageResponse<MemberResponse> getMembersByUsername(String username, Pageable pageable);

    // 根據電子郵件模糊查詢會員（分頁）
    PageResponse<MemberResponse> getMembersByEmail(String email, Pageable pageable);

    // 刪除會員
    void deleteMember(Long id);

    // 檢查用戶名是否存在
    boolean isUsernameExists(String username);

    // 檢查電子郵件是否存在
    boolean isEmailExists(String email);
}