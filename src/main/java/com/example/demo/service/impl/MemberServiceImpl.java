package com.example.demo.service.impl;

import com.example.demo.bean.request.MemberCreateRequest;
import com.example.demo.bean.request.MemberUpdateRequest;
import com.example.demo.bean.response.MemberResponse;
import com.example.demo.bean.response.PageResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bean.model.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        // 檢查用戶名和電子郵件是否已存在
        if (isUsernameExists(request.getUsername())) {
            throw new BusinessException("Username already exists: " + request.getUsername());
        }

        if (isEmailExists(request.getEmail())) {
            throw new BusinessException("Email already exists: " + request.getEmail());
        }

        // 創建新的會員實體
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setEmail(request.getEmail());
        member.setPassword(request.getPassword());
        member.setPhone(request.getPhone());
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());

        Member create = memberRepository.save(member);
        return MemberResponse.from(create);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        return memberRepository.findById(id)
                .map(existingMember -> {
                    // 檢查用戶名是否已被其他用戶使用
                    if (!existingMember.getUsername().equals(request.getUsername())
                            && isUsernameExists(request.getUsername())) {
                        throw new BusinessException("Username already exists: " + request.getUsername());
                    }

                    // 檢查電子郵件是否已被其他用戶使用
                    if (!existingMember.getEmail().equals(request.getEmail())
                            && isEmailExists(request.getEmail())) {
                        throw new BusinessException("Email already exists: " + request.getEmail());
                    }

                    // 更新會員訊息
                    existingMember.setUsername(request.getUsername());
                    existingMember.setEmail(request.getEmail());
                    existingMember.setPhone(request.getPhone());
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        existingMember.setPassword(request.getPassword());
                    }
                    existingMember.setUpdatedAt(LocalDateTime.now());

                    return MemberResponse.from(memberRepository.save(existingMember));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        return memberRepository.findById(id).map(MemberResponse::from).orElseThrow(() -> new RuntimeException("會員不存在"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getAllMembers(Pageable pageable) {
        return PageResponse.from(memberRepository.findAll(pageable).map(MemberResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getMembersByUsername(String username, Pageable pageable) {
        return PageResponse.from(memberRepository.findByUsernameContaining(username, pageable).map(MemberResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getMembersByEmail(String email, Pageable pageable) {
        return PageResponse.from(memberRepository.findByEmailContaining(email, pageable).map(MemberResponse::from));
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member", "id", id);
        }
        memberRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }
}