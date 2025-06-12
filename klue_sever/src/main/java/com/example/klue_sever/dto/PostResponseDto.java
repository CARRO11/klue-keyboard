package com.example.klue_sever.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    
    private Long id;
    private String title;
    private String content;
    private String author;
    private String category;
    private Long viewCount;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPinned;
    
    // 목록 조회용 (내용 제외)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String title;
        private String author;
        private String category;
        private Long viewCount;
        private Long likeCount;
        private LocalDateTime createdAt;
        private Boolean isPinned;
        
        // 내용 미리보기 (100자 제한)
        private String preview;
    }
} 