package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Gasket;
import com.example.klue_sever.service.GasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gaskets")
@Tag(name = "🟡 가스켓 API", description = "키보드 가스켓 CRUD 관리")
public class GasketController {

    private final GasketService gasketService;

    @Autowired
    public GasketController(GasketService gasketService) {
        this.gasketService = gasketService;
    }

    @GetMapping
    @Operation(summary = "모든 가스켓 조회", description = "페이지네이션과 정렬을 지원하는 가스켓 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllGaskets(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "정렬 기준", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            
            @Parameter(description = "정렬 방향", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Gasket> gasketPage = gasketService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("gaskets", gasketPage.getContent());
        response.put("currentPage", gasketPage.getNumber());
        response.put("totalItems", gasketPage.getTotalElements());
        response.put("totalPages", gasketPage.getTotalPages());
        response.put("pageSize", gasketPage.getSize());
        response.put("isFirst", gasketPage.isFirst());
        response.put("isLast", gasketPage.isLast());
        response.put("message", "✅ 가스켓 목록 조회 성공 (총 " + gasketPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "가스켓 상세 조회", description = "ID로 특정 가스켓의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getGasketById(
            @Parameter(description = "가스켓 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Gasket> gasketOpt = gasketService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (gasketOpt.isPresent()) {
            response.put("gasket", gasketOpt.get());
            response.put("message", "✅ 가스켓 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 가스켓을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 가스켓 생성", description = "새로운 가스켓을 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createGasket(
            @Parameter(description = "생성할 가스켓 정보")
            @RequestBody Gasket gasket) {
        
        try {
            Gasket savedGasket = gasketService.save(gasket);
            Map<String, Object> response = new HashMap<>();
            response.put("gasket", savedGasket);
            response.put("message", "✅ 가스켓 '" + savedGasket.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 가스켓 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "가스켓 정보 수정", description = "기존 가스켓의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateGasket(
            @Parameter(description = "수정할 가스켓 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 가스켓 정보")
            @RequestBody Gasket gasketDetails) {
        
        try {
            Gasket updatedGasket = gasketService.update(id, gasketDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedGasket != null) {
                response.put("gasket", updatedGasket);
                response.put("message", "✅ 가스켓 '" + updatedGasket.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 가스켓을 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 가스켓 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "가스켓 삭제", description = "ID로 특정 가스켓 삭제")
    public ResponseEntity<Map<String, Object>> deleteGasket(
            @Parameter(description = "삭제할 가스켓 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (gasketService.exists(id)) {
            gasketService.delete(id);
            response.put("message", "✅ ID " + id + "번 가스켓이 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 가스켓을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "가스켓 검색", description = "이름, 재질, 타입으로 가스켓 검색")
    public ResponseEntity<Map<String, Object>> searchGaskets(
            @Parameter(description = "검색 키워드", example = "폼")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Gasket> results = gasketService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("gaskets", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 가스켓 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "가스켓 필터링", description = "재질과 타입으로 가스켓 필터링")
    public ResponseEntity<Map<String, Object>> filterGaskets(
            @Parameter(description = "재질", example = "폴리우레탄")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "타입", example = "두꺼운 타입")
            @RequestParam(required = false) String typing) {
        
        try {
            List<Gasket> results = gasketService.filterGaskets(material, typing);
            
            Map<String, Object> response = new HashMap<>();
            response.put("gaskets", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "typing", typing != null ? typing : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 가스켓");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "가스켓 통계", description = "가스켓 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getGasketStats() {
        try {
            Map<String, Object> stats = gasketService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 가스켓 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 