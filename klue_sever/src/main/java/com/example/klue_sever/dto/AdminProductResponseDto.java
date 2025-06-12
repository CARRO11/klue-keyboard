package com.example.klue_sever.dto;

import com.example.klue_sever.entity.AdminProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductResponseDto {
    
    private Long id;
    private String productName;
    private String description;
    private String brand;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private String productUrl;
    private AdminProduct.ProductStatus status;
    private String statusDescription;
    private String adminUser;
    private Integer priorityOrder;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    
    // 관리자용 목록 조회용 (간단한 정보만)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String productName;
        private String brand;
        private String category;
        private BigDecimal price;
        private String imageUrl;
        private AdminProduct.ProductStatus status;
        private String statusDescription;
        private String adminUser;
        private Integer priorityOrder;
        private Boolean isFeatured;
        private LocalDateTime createdAt;
        private LocalDateTime publishedAt;
        
        // 설명 미리보기 (100자 제한)
        private String preview;
    }
    
    // 공개용 응답 (일반 사용자용)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Public {
        private Long id;
        private String productName;
        private String description;
        private String brand;
        private String category;
        private BigDecimal price;
        private String imageUrl;
        private String productUrl;
        private Boolean isFeatured;
        private LocalDateTime publishedAt;
    }
} 