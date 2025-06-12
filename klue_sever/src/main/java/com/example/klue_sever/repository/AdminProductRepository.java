package com.example.klue_sever.repository;

import com.example.klue_sever.entity.AdminProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminProductRepository extends JpaRepository<AdminProduct, Long> {
    
    // 관리자용: 모든 제품 조회 (삭제된 것 제외)
    Page<AdminProduct> findByIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(Pageable pageable);
    
    // 관리자용: 상태별 제품 조회
    Page<AdminProduct> findByStatusAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(
            AdminProduct.ProductStatus status, Pageable pageable);
    
    // 관리자용: 카테고리별 제품 조회
    Page<AdminProduct> findByCategoryAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(
            String category, Pageable pageable);
    
    // 관리자용: 브랜드별 제품 조회
    Page<AdminProduct> findByBrandAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(
            String brand, Pageable pageable);
    
    // 관리자용: 제품명 검색
    Page<AdminProduct> findByProductNameContainingAndIsDeletedFalseOrderByPriorityOrderDescCreatedAtDesc(
            String productName, Pageable pageable);
    
    // 관리자용: 제품명 또는 설명 검색
    @Query("SELECT p FROM AdminProduct p WHERE (p.productName LIKE %:keyword% OR p.description LIKE %:keyword%) AND p.isDeleted = false ORDER BY p.priorityOrder DESC, p.createdAt DESC")
    Page<AdminProduct> findByKeywordContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 관리자용: 관리자별 제품 조회
    Page<AdminProduct> findByAdminUserAndIsDeletedFalseOrderByCreatedAtDesc(String adminUser, Pageable pageable);
    
    // 관리자용: 상세 조회 (삭제되지 않은 제품)
    Optional<AdminProduct> findByIdAndIsDeletedFalse(Long id);
    
    // 공개용: 게시된 제품만 조회 (일반 사용자용)
    @Query("SELECT p FROM AdminProduct p WHERE p.status = 'PUBLISHED' AND p.isDeleted = false ORDER BY p.priorityOrder DESC, p.publishedAt DESC")
    Page<AdminProduct> findPublishedProducts(Pageable pageable);
    
    // 공개용: 게시된 제품 중 카테고리별 조회
    @Query("SELECT p FROM AdminProduct p WHERE p.status = 'PUBLISHED' AND p.category = :category AND p.isDeleted = false ORDER BY p.priorityOrder DESC, p.publishedAt DESC")
    Page<AdminProduct> findPublishedProductsByCategory(@Param("category") String category, Pageable pageable);
    
    // 공개용: 특별 추천 제품 조회
    @Query("SELECT p FROM AdminProduct p WHERE p.status = 'PUBLISHED' AND p.isFeatured = true AND p.isDeleted = false ORDER BY p.priorityOrder DESC, p.publishedAt DESC")
    List<AdminProduct> findFeaturedProducts(Pageable pageable);
    
    // 카테고리 목록 조회 (게시된 제품 기준)
    @Query("SELECT DISTINCT p.category FROM AdminProduct p WHERE p.status = 'PUBLISHED' AND p.isDeleted = false AND p.category IS NOT NULL")
    List<String> findDistinctPublishedCategories();
    
    // 브랜드 목록 조회 (게시된 제품 기준)
    @Query("SELECT DISTINCT p.brand FROM AdminProduct p WHERE p.status = 'PUBLISHED' AND p.isDeleted = false AND p.brand IS NOT NULL")
    List<String> findDistinctPublishedBrands();
    
    // 관리자용: 전체 카테고리 목록
    @Query("SELECT DISTINCT p.category FROM AdminProduct p WHERE p.isDeleted = false AND p.category IS NOT NULL")
    List<String> findDistinctCategories();
    
    // 관리자용: 전체 브랜드 목록
    @Query("SELECT DISTINCT p.brand FROM AdminProduct p WHERE p.isDeleted = false AND p.brand IS NOT NULL")
    List<String> findDistinctBrands();
    
    // 상태별 제품 개수 조회
    @Query("SELECT p.status, COUNT(p) FROM AdminProduct p WHERE p.isDeleted = false GROUP BY p.status")
    List<Object[]> countByStatus();
    
    // 제품 게시 처리
    @Modifying
    @Query("UPDATE AdminProduct p SET p.status = 'PUBLISHED', p.publishedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void publishProduct(@Param("id") Long id);
    
    // 제품 숨김 처리
    @Modifying
    @Query("UPDATE AdminProduct p SET p.status = 'HIDDEN' WHERE p.id = :id")
    void hideProduct(@Param("id") Long id);
    
    // 우선순위 업데이트
    @Modifying
    @Query("UPDATE AdminProduct p SET p.priorityOrder = :priority WHERE p.id = :id")
    void updatePriority(@Param("id") Long id, @Param("priority") Integer priority);
} 