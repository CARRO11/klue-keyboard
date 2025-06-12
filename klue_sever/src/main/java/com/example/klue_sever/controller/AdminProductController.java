package com.example.klue_sever.controller;

import com.example.klue_sever.dto.AdminProductRequestDto;
import com.example.klue_sever.dto.AdminProductResponseDto;
import com.example.klue_sever.entity.AdminProduct;
import com.example.klue_sever.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminProductController {
    
    private final AdminProductService adminProductService;
    
    // ========== 관리자 전용 API ==========
    
    // 관리자용: 제품 목록 조회 (전체)
    @GetMapping
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 제품 목록 조회 요청: page={}, size={}", page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.getAllProducts(page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 상태별 제품 조회
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> getProductsByStatus(
            @PathVariable AdminProduct.ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 상태별 제품 조회 요청: status={}, page={}, size={}", status, page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.getProductsByStatus(status, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 카테고리별 제품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 카테고리별 제품 조회 요청: category={}, page={}, size={}", category, page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.getProductsByCategory(category, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 브랜드별 제품 조회
    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> getProductsByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 브랜드별 제품 조회 요청: brand={}, page={}, size={}", brand, page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.getProductsByBrand(brand, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 제품 검색
    @GetMapping("/search")
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 제품 검색 요청: keyword={}, page={}, size={}", keyword, page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.searchProducts(keyword, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 관리자별 제품 조회
    @GetMapping("/admin/{adminUser}")
    public ResponseEntity<Page<AdminProductResponseDto.Summary>> getProductsByAdmin(
            @PathVariable String adminUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("관리자용 관리자별 제품 조회 요청: adminUser={}, page={}, size={}", adminUser, page, size);
        Page<AdminProductResponseDto.Summary> products = adminProductService.getProductsByAdmin(adminUser, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 관리자용: 제품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<AdminProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("관리자용 제품 상세 조회 요청: id={}", id);
        AdminProductResponseDto product = adminProductService.getProductById(id);
        
        return ResponseEntity.ok(product);
    }
    
    // 관리자용: 제품 생성
    @PostMapping
    public ResponseEntity<AdminProductResponseDto> createProduct(@Valid @RequestBody AdminProductRequestDto requestDto) {
        log.info("관리자용 제품 생성 요청: productName={}, adminUser={}", requestDto.getProductName(), requestDto.getAdminUser());
        AdminProductResponseDto product = adminProductService.createProduct(requestDto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    // 관리자용: 제품 수정
    @PutMapping("/{id}")
    public ResponseEntity<AdminProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody AdminProductRequestDto requestDto) {
        
        log.info("관리자용 제품 수정 요청: id={}, productName={}", id, requestDto.getProductName());
        AdminProductResponseDto product = adminProductService.updateProduct(id, requestDto);
        
        return ResponseEntity.ok(product);
    }
    
    // 관리자용: 제품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        log.info("관리자용 제품 삭제 요청: id={}", id);
        adminProductService.deleteProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "제품이 성공적으로 삭제되었습니다.");
        response.put("deletedId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // 관리자용: 제품 게시
    @PatchMapping("/{id}/publish")
    public ResponseEntity<Map<String, String>> publishProduct(@PathVariable Long id) {
        log.info("관리자용 제품 게시 요청: id={}", id);
        adminProductService.publishProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "제품이 성공적으로 게시되었습니다.");
        response.put("productId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // 관리자용: 제품 숨김
    @PatchMapping("/{id}/hide")
    public ResponseEntity<Map<String, String>> hideProduct(@PathVariable Long id) {
        log.info("관리자용 제품 숨김 요청: id={}", id);
        adminProductService.hideProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "제품이 성공적으로 숨겨졌습니다.");
        response.put("productId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // 관리자용: 제품 우선순위 변경
    @PatchMapping("/{id}/priority")
    public ResponseEntity<Map<String, String>> updatePriority(
            @PathVariable Long id,
            @RequestParam Integer priority) {
        
        log.info("관리자용 제품 우선순위 변경 요청: id={}, priority={}", id, priority);
        adminProductService.updatePriority(id, priority);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "제품 우선순위가 성공적으로 변경되었습니다.");
        response.put("productId", id.toString());
        response.put("priority", priority.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // 관리자용: 상태별 통계
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatusStatistics() {
        log.info("관리자용 상태별 통계 조회 요청");
        Map<String, Long> statistics = adminProductService.getStatusStatistics();
        
        return ResponseEntity.ok(statistics);
    }
    
    // 관리자용: 전체 카테고리 목록
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("관리자용 전체 카테고리 목록 조회 요청");
        List<String> categories = adminProductService.getAllCategories();
        
        return ResponseEntity.ok(categories);
    }
    
    // 관리자용: 전체 브랜드 목록
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        log.info("관리자용 전체 브랜드 목록 조회 요청");
        List<String> brands = adminProductService.getAllBrands();
        
        return ResponseEntity.ok(brands);
    }
    
    // ========== 공개 API (일반 사용자용) ==========
    
    // 공개용: 게시된 제품 목록 조회
    @GetMapping("/public")
    public ResponseEntity<Page<AdminProductResponseDto.Public>> getPublishedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("공개용 제품 목록 조회 요청: page={}, size={}", page, size);
        Page<AdminProductResponseDto.Public> products = adminProductService.getPublishedProducts(page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 공개용: 게시된 제품 중 카테고리별 조회
    @GetMapping("/public/category/{category}")
    public ResponseEntity<Page<AdminProductResponseDto.Public>> getPublishedProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("공개용 카테고리별 제품 조회 요청: category={}, page={}, size={}", category, page, size);
        Page<AdminProductResponseDto.Public> products = adminProductService.getPublishedProductsByCategory(category, page, size);
        
        return ResponseEntity.ok(products);
    }
    
    // 공개용: 특별 추천 제품 조회
    @GetMapping("/public/featured")
    public ResponseEntity<List<AdminProductResponseDto.Public>> getFeaturedProducts(
            @RequestParam(defaultValue = "5") int limit) {
        
        log.info("공개용 특별 추천 제품 조회 요청: limit={}", limit);
        List<AdminProductResponseDto.Public> products = adminProductService.getFeaturedProducts(limit);
        
        return ResponseEntity.ok(products);
    }
    
    // 공개용: 게시된 제품 상세 조회
    @GetMapping("/public/{id}")
    public ResponseEntity<AdminProductResponseDto.Public> getPublishedProductById(@PathVariable Long id) {
        log.info("공개용 제품 상세 조회 요청: id={}", id);
        AdminProductResponseDto.Public product = adminProductService.getPublishedProductById(id);
        
        return ResponseEntity.ok(product);
    }
    
    // 공개용: 카테고리 목록 조회
    @GetMapping("/public/categories")
    public ResponseEntity<List<String>> getPublishedCategories() {
        log.info("공개용 카테고리 목록 조회 요청");
        List<String> categories = adminProductService.getPublishedCategories();
        
        return ResponseEntity.ok(categories);
    }
    
    // 공개용: 브랜드 목록 조회
    @GetMapping("/public/brands")
    public ResponseEntity<List<String>> getPublishedBrands() {
        log.info("공개용 브랜드 목록 조회 요청");
        List<String> brands = adminProductService.getPublishedBrands();
        
        return ResponseEntity.ok(brands);
    }
    
    // ========== 예외 처리 ==========
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception occurred: {}", e.getMessage());
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "오류가 발생했습니다.");
        errorResponse.put("message", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        log.error("Unexpected exception occurred: ", e);
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "서버 내부 오류가 발생했습니다.");
        errorResponse.put("message", "잠시 후 다시 시도해주세요.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 