package com.example.klue_sever.service;

import com.example.klue_sever.dto.AdminProductRequestDto;
import com.example.klue_sever.dto.AdminProductResponseDto;
import com.example.klue_sever.entity.AdminProduct;
import com.example.klue_sever.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminProductService {
    
    private final AdminProductRepository adminProductRepository;
    
    // ========== 관리자 전용 메서드 ==========
    
    // 관리자용: 제품 목록 조회 (전체)
    public Page<AdminProductResponseDto.Summary> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 상태별 제품 조회
    public Page<AdminProductResponseDto.Summary> getProductsByStatus(AdminProduct.ProductStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByStatusAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(status, pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 카테고리별 제품 조회
    public Page<AdminProductResponseDto.Summary> getProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByCategoryAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(category, pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 브랜드별 제품 조회
    public Page<AdminProductResponseDto.Summary> getProductsByBrand(String brand, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByBrandAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(brand, pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 검색 (제품명 또는 설명)
    public Page<AdminProductResponseDto.Summary> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByKeywordContaining(keyword, pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 관리자별 제품 조회
    public Page<AdminProductResponseDto.Summary> getProductsByAdmin(String adminUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findByAdminUserAndIsDeletedFalseOrderByCreatedAtDesc(adminUser, pageable);
        
        return products.map(this::convertToSummary);
    }
    
    // 관리자용: 제품 상세 조회
    public AdminProductResponseDto getProductById(Long id) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        return convertToResponseDto(product);
    }
    
    // 관리자용: 제품 생성
    @Transactional
    public AdminProductResponseDto createProduct(AdminProductRequestDto requestDto) {
        AdminProduct product = AdminProduct.builder()
                .productName(requestDto.getProductName())
                .description(requestDto.getDescription())
                .brand(requestDto.getBrand())
                .category(requestDto.getCategory())
                .price(requestDto.getPrice())
                .imageUrl(requestDto.getImageUrl())
                .productUrl(requestDto.getProductUrl())
                .status(requestDto.getStatus() != null ? requestDto.getStatus() : AdminProduct.ProductStatus.DRAFT)
                .adminUser(requestDto.getAdminUser())
                .priorityOrder(requestDto.getPriorityOrder() != null ? requestDto.getPriorityOrder() : 0)
                .isFeatured(requestDto.getIsFeatured() != null ? requestDto.getIsFeatured() : false)
                .build();
        
        AdminProduct savedProduct = adminProductRepository.save(product);
        log.info("새 제품 생성: ID={}, 제품명={}, 관리자={}", savedProduct.getId(), savedProduct.getProductName(), savedProduct.getAdminUser());
        
        return convertToResponseDto(savedProduct);
    }
    
    // 관리자용: 제품 수정
    @Transactional
    public AdminProductResponseDto updateProduct(Long id, AdminProductRequestDto requestDto) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        product.setProductName(requestDto.getProductName());
        product.setDescription(requestDto.getDescription());
        product.setBrand(requestDto.getBrand());
        product.setCategory(requestDto.getCategory());
        product.setPrice(requestDto.getPrice());
        product.setImageUrl(requestDto.getImageUrl());
        product.setProductUrl(requestDto.getProductUrl());
        product.setStatus(requestDto.getStatus());
        product.setPriorityOrder(requestDto.getPriorityOrder());
        product.setIsFeatured(requestDto.getIsFeatured());
        
        AdminProduct updatedProduct = adminProductRepository.save(product);
        log.info("제품 수정: ID={}, 제품명={}", updatedProduct.getId(), updatedProduct.getProductName());
        
        return convertToResponseDto(updatedProduct);
    }
    
    // 관리자용: 제품 삭제 (소프트 삭제)
    @Transactional
    public void deleteProduct(Long id) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        product.setIsDeleted(true);
        adminProductRepository.save(product);
        log.info("제품 삭제: ID={}, 제품명={}", product.getId(), product.getProductName());
    }
    
    // 관리자용: 제품 게시
    @Transactional
    public void publishProduct(Long id) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        product.setStatus(AdminProduct.ProductStatus.PUBLISHED);
        product.setPublishedAt(LocalDateTime.now());
        adminProductRepository.save(product);
        log.info("제품 게시: ID={}, 제품명={}", product.getId(), product.getProductName());
    }
    
    // 관리자용: 제품 숨김
    @Transactional
    public void hideProduct(Long id) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        product.setStatus(AdminProduct.ProductStatus.HIDDEN);
        adminProductRepository.save(product);
        log.info("제품 숨김: ID={}, 제품명={}", product.getId(), product.getProductName());
    }
    
    // 관리자용: 우선순위 변경
    @Transactional
    public void updatePriority(Long id, Integer priority) {
        if (!adminProductRepository.existsById(id)) {
            throw new RuntimeException("제품을 찾을 수 없습니다. ID: " + id);
        }
        
        adminProductRepository.updatePriority(id, priority);
        log.info("제품 우선순위 변경: ID={}, 우선순위={}", id, priority);
    }
    
    // 관리자용: 상태별 통계
    public Map<String, Long> getStatusStatistics() {
        List<Object[]> stats = adminProductRepository.countByStatus();
        Map<String, Long> statistics = new HashMap<>();
        
        for (AdminProduct.ProductStatus status : AdminProduct.ProductStatus.values()) {
            statistics.put(status.name(), 0L);
        }
        
        for (Object[] stat : stats) {
            AdminProduct.ProductStatus status = (AdminProduct.ProductStatus) stat[0];
            Long count = (Long) stat[1];
            statistics.put(status.name(), count);
        }
        
        return statistics;
    }
    
    // ========== 공개 메서드 (일반 사용자용) ==========
    
    // 공개용: 게시된 제품 목록 조회
    public Page<AdminProductResponseDto.Public> getPublishedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findPublishedProducts(pageable);
        
        return products.map(this::convertToPublicDto);
    }
    
    // 공개용: 게시된 제품 중 카테고리별 조회
    public Page<AdminProductResponseDto.Public> getPublishedProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminProduct> products = adminProductRepository.findPublishedProductsByCategory(category, pageable);
        
        return products.map(this::convertToPublicDto);
    }
    
    // 공개용: 특별 추천 제품 조회
    public List<AdminProductResponseDto.Public> getFeaturedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<AdminProduct> products = adminProductRepository.findFeaturedProducts(pageable);
        
        return products.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }
    
    // 공개용: 게시된 제품 상세 조회
    public AdminProductResponseDto.Public getPublishedProductById(Long id) {
        AdminProduct product = adminProductRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다. ID: " + id));
        
        if (product.getStatus() != AdminProduct.ProductStatus.PUBLISHED) {
            throw new RuntimeException("게시되지 않은 제품입니다.");
        }
        
        return convertToPublicDto(product);
    }
    
    // 공개용: 카테고리 목록 조회
    public List<String> getPublishedCategories() {
        return adminProductRepository.findDistinctPublishedCategories();
    }
    
    // 공개용: 브랜드 목록 조회
    public List<String> getPublishedBrands() {
        return adminProductRepository.findDistinctPublishedBrands();
    }
    
    // 관리자용: 전체 카테고리 목록
    public List<String> getAllCategories() {
        return adminProductRepository.findDistinctCategories();
    }
    
    // 관리자용: 전체 브랜드 목록
    public List<String> getAllBrands() {
        return adminProductRepository.findDistinctBrands();
    }
    
    // ========== 변환 메서드 ==========
    
    // Entity -> 관리자용 ResponseDto 변환
    private AdminProductResponseDto convertToResponseDto(AdminProduct product) {
        return AdminProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .productUrl(product.getProductUrl())
                .status(product.getStatus())
                .statusDescription(product.getStatus().getDescription())
                .adminUser(product.getAdminUser())
                .priorityOrder(product.getPriorityOrder())
                .isFeatured(product.getIsFeatured())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .publishedAt(product.getPublishedAt())
                .build();
    }
    
    // Entity -> 관리자용 Summary 변환
    private AdminProductResponseDto.Summary convertToSummary(AdminProduct product) {
        String preview = product.getDescription().length() > 100 ? 
                product.getDescription().substring(0, 100) + "..." : 
                product.getDescription();
        
        return AdminProductResponseDto.Summary.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .statusDescription(product.getStatus().getDescription())
                .adminUser(product.getAdminUser())
                .priorityOrder(product.getPriorityOrder())
                .isFeatured(product.getIsFeatured())
                .createdAt(product.getCreatedAt())
                .publishedAt(product.getPublishedAt())
                .preview(preview)
                .build();
    }
    
    // Entity -> 공개용 Public DTO 변환
    private AdminProductResponseDto.Public convertToPublicDto(AdminProduct product) {
        return AdminProductResponseDto.Public.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .productUrl(product.getProductUrl())
                .isFeatured(product.getIsFeatured())
                .publishedAt(product.getPublishedAt())
                .build();
    }
} 