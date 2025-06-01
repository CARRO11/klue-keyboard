package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Stabilizer;
import com.example.klue_sever.service.StabilizerService;
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
@RequestMapping("/api/stabilizers")
@Tag(name = "🔧 스테빌라이저 API", description = "키보드 스테빌라이저 CRUD 관리")
public class StabilizerController {

    private final StabilizerService stabilizerService;

    @Autowired
    public StabilizerController(StabilizerService stabilizerService) {
        this.stabilizerService = stabilizerService;
    }

    @GetMapping
    @Operation(summary = "모든 스테빌라이저 조회", description = "페이지네이션과 정렬을 지원하는 스테빌라이저 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllStabilizers(
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
        Page<Stabilizer> stabilizerPage = stabilizerService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("stabilizers", stabilizerPage.getContent());
        response.put("currentPage", stabilizerPage.getNumber());
        response.put("totalItems", stabilizerPage.getTotalElements());
        response.put("totalPages", stabilizerPage.getTotalPages());
        response.put("pageSize", stabilizerPage.getSize());
        response.put("isFirst", stabilizerPage.isFirst());
        response.put("isLast", stabilizerPage.isLast());
        response.put("message", "✅ 스테빌라이저 목록 조회 성공 (총 " + stabilizerPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "스테빌라이저 상세 조회", description = "ID로 특정 스테빌라이저의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getStabilizerById(
            @Parameter(description = "스테빌라이저 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Stabilizer> stabilizerOpt = stabilizerService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (stabilizerOpt.isPresent()) {
            response.put("stabilizer", stabilizerOpt.get());
            response.put("message", "✅ 스테빌라이저 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 스테빌라이저를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 스테빌라이저 생성", description = "새로운 스테빌라이저를 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createStabilizer(
            @Parameter(description = "생성할 스테빌라이저 정보")
            @RequestBody Stabilizer stabilizer) {
        
        try {
            Stabilizer savedStabilizer = stabilizerService.save(stabilizer);
            Map<String, Object> response = new HashMap<>();
            response.put("stabilizer", savedStabilizer);
            response.put("message", "✅ 스테빌라이저 '" + savedStabilizer.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 스테빌라이저 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "스테빌라이저 정보 수정", description = "기존 스테빌라이저의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateStabilizer(
            @Parameter(description = "수정할 스테빌라이저 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 스테빌라이저 정보")
            @RequestBody Stabilizer stabilizerDetails) {
        
        try {
            Stabilizer updatedStabilizer = stabilizerService.update(id, stabilizerDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedStabilizer != null) {
                response.put("stabilizer", updatedStabilizer);
                response.put("message", "✅ 스테빌라이저 '" + updatedStabilizer.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 스테빌라이저를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 스테빌라이저 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스테빌라이저 삭제", description = "ID로 특정 스테빌라이저 삭제")
    public ResponseEntity<Map<String, Object>> deleteStabilizer(
            @Parameter(description = "삭제할 스테빌라이저 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (stabilizerService.exists(id)) {
            stabilizerService.delete(id);
            response.put("message", "✅ ID " + id + "번 스테빌라이저가 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 스테빌라이저를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "스테빌라이저 검색", description = "이름, 재질, 크기로 스테빌라이저 검색")
    public ResponseEntity<Map<String, Object>> searchStabilizers(
            @Parameter(description = "검색 키워드", example = "체리")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Stabilizer> results = stabilizerService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("stabilizers", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 스테빌라이저 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "스테빌라이저 필터링", description = "재질과 크기로 스테빌라이저 필터링")
    public ResponseEntity<Map<String, Object>> filterStabilizers(
            @Parameter(description = "재질", example = "PC")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "크기", example = "2u")
            @RequestParam(required = false) String size) {
        
        try {
            List<Stabilizer> results = stabilizerService.filterStabilizers(material, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("stabilizers", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "size", size != null ? size : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 스테빌라이저");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "스테빌라이저 통계", description = "스테빌라이저 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getStabilizerStats() {
        try {
            Map<String, Object> stats = stabilizerService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 스테빌라이저 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 