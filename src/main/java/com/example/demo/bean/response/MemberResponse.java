package com.example.demo.bean.response;

import com.example.demo.bean.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 會員返回 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 從實體轉換為 DTO
     * @param member 會員實體
     * @return 會員返回 DTO
     */
    public static MemberResponse from(Member member) {
        if (member == null) {
            return null;
        }
        
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .phone(member.getPhone())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}