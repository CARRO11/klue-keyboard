package com.example.klue_sever.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String productName;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, length = 100)
    private String brand;
    
    @Column(length = 100)
    private String category;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(length = 500)
    private String imageUrl;
    
    @Column(length = 500)
    private String productUrl;
    
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.DRAFT;
    
    @Column(nullable = false, length = 100)
    private String adminUser;
    
    @Column(name = "priority_order")
    private Integer priorityOrder = 0;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    // 제품 상태 열거형
    public enum ProductStatus {
        DRAFT("임시저장"),
        PUBLISHED("게시됨"),
        HIDDEN("숨김"),
        DISCONTINUED("단종");
        
        private final String description;
        
        ProductStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 