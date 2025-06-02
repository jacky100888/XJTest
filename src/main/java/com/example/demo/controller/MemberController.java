package com.example.demo.controller;

import com.example.demo.bean.request.MemberCreateRequest;
import com.example.demo.bean.request.MemberUpdateRequest;
import com.example.demo.bean.response.ApiResponse;
import com.example.demo.bean.response.MemberResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 創建會員
    @PostMapping
    public ApiResponse<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        try {
            MemberResponse memberResponse = memberService.createMember(request);
            return ApiResponse.success(HttpStatus.CREATED.value(), memberResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 更新會員
    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(@PathVariable Long id, @Valid @RequestBody MemberUpdateRequest request) {
        try {
            MemberResponse memberResponse = memberService.updateMember(id, request);
            return ApiResponse.success(HttpStatus.OK.value(), memberResponse);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // 根據ID查詢會員
    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberById(id);
        return ApiResponse.success(HttpStatus.OK.value(), memberResponse);
    }

    // 分頁查詢所有會員
    @GetMapping("/page")
    public ApiResponse<PageResponse<MemberResponse>> getAllMembersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        PageResponse<MemberResponse> pageResponse = memberService.getAllMembers(pageable);
        return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
    }

    // 根據用戶名模糊查詢會員（分頁）
    @GetMapping("/search/username")
    public ApiResponse<PageResponse<MemberResponse>> getMembersByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<MemberResponse> pageResponse = memberService.getMembersByUsername(username, pageable);
        return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
    }

    // 根據郵箱模糊查詢會員（分頁）
    @GetMapping("/search/email")
    public ApiResponse<PageResponse<MemberResponse>> getMembersByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponse<MemberResponse> pageResponse = memberService.getMembersByEmail(email, pageable);
        return ApiResponse.success(HttpStatus.OK.value(), pageResponse);
    }

    // 刪除會員
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ApiResponse.success(HttpStatus.OK.value());
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 檢查用戶名是否存在
    @GetMapping("/check/username")
    public ApiResponse<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = memberService.isUsernameExists(username);
        return ApiResponse.success(HttpStatus.OK.value(), exists);
    }

    // 檢查郵箱是否存在
    @GetMapping("/check/email")
    public ApiResponse<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = memberService.isEmailExists(email);
        return ApiResponse.success(HttpStatus.OK.value(), exists);
    }
}