package com.example.klue_sever.dto;

import com.example.klue_sever.entity.AdminProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductRequestDto {
    
    @NotBlank(message = "제품명은 필수입니다.")
    @Size(max = 200, message = "제품명은 200자를 초과할 수 없습니다.")
    private String productName;
    
    @NotBlank(message = "제품 설명은 필수입니다.")
    private String description;
    
    @NotBlank(message = "브랜드는 필수입니다.")
    @Size(max = 100, message = "브랜드는 100자를 초과할 수 없습니다.")
    private String brand;
    
    @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다.")
    private String category;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;
    
    @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다.")
    private String imageUrl;
    
    @Size(max = 500, message = "제품 URL은 500자를 초과할 수 없습니다.")
    private String productUrl;
    
    private AdminProduct.ProductStatus status = AdminProduct.ProductStatus.DRAFT;
    
    @NotBlank(message = "관리자 사용자명은 필수입니다.")
    @Size(max = 100, message = "관리자 사용자명은 100자를 초과할 수 없습니다.")
    private String adminUser;
    
    private Integer priorityOrder = 0;
    
    private Boolean isFeatured = false;
} 