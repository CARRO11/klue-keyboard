package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Keycap;
import com.example.klue_sever.service.KeycapService;
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
@RequestMapping("/api/keycaps")
@Tag(name = "⌨️ 키캡 API", description = "키보드 키캡 CRUD 관리")
public class KeycapController {

    private final KeycapService keycapService;

    @Autowired
    public KeycapController(KeycapService keycapService) {
        this.keycapService = keycapService;
    }

    @GetMapping
    @Operation(summary = "모든 키캡 조회", description = "페이지네이션과 정렬을 지원하는 키캡 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllKeycaps(
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
        Page<Keycap> keycapPage = keycapService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("keycaps", keycapPage.getContent());
        response.put("currentPage", keycapPage.getNumber());
        response.put("totalItems", keycapPage.getTotalElements());
        response.put("totalPages", keycapPage.getTotalPages());
        response.put("pageSize", keycapPage.getSize());
        response.put("isFirst", keycapPage.isFirst());
        response.put("isLast", keycapPage.isLast());
        response.put("message", "✅ 키캡 목록 조회 성공 (총 " + keycapPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "키캡 상세 조회", description = "ID로 특정 키캡의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getKeycapById(
            @Parameter(description = "키캡 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Keycap> keycapOpt = keycapService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (keycapOpt.isPresent()) {
            response.put("keycap", keycapOpt.get());
            response.put("message", "✅ 키캡 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 키캡을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 키캡 생성", description = "새로운 키캡을 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createKeycap(
            @Parameter(description = "생성할 키캡 정보")
            @RequestBody Keycap keycap) {
        
        try {
            Keycap savedKeycap = keycapService.save(keycap);
            Map<String, Object> response = new HashMap<>();
            response.put("keycap", savedKeycap);
            response.put("message", "✅ 키캡 '" + savedKeycap.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 키캡 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "키캡 정보 수정", description = "기존 키캡의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateKeycap(
            @Parameter(description = "수정할 키캡 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 키캡 정보")
            @RequestBody Keycap keycapDetails) {
        
        try {
            Keycap updatedKeycap = keycapService.update(id, keycapDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedKeycap != null) {
                response.put("keycap", updatedKeycap);
                response.put("message", "✅ 키캡 '" + updatedKeycap.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 키캡을 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 키캡 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "키캡 삭제", description = "ID로 특정 키캡 삭제")
    public ResponseEntity<Map<String, Object>> deleteKeycap(
            @Parameter(description = "삭제할 키캡 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (keycapService.exists(id)) {
            keycapService.delete(id);
            response.put("message", "✅ ID " + id + "번 키캡이 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 키캡을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "키캡 검색", description = "이름, 재질, 프로필로 키캡 검색")
    public ResponseEntity<Map<String, Object>> searchKeycaps(
            @Parameter(description = "검색 키워드", example = "PBT")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Keycap> results = keycapService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("keycaps", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 키캡 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "키캡 필터링", description = "재질과 프로필로 키캡 필터링")
    public ResponseEntity<Map<String, Object>> filterKeycaps(
            @Parameter(description = "재질", example = "PBT")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "프로필", example = "Cherry")
            @RequestParam(required = false) String profile) {
        
        try {
            List<Keycap> results = keycapService.filterKeycaps(material, profile);
            
            Map<String, Object> response = new HashMap<>();
            response.put("keycaps", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "profile", profile != null ? profile : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 키캡");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "키캡 통계", description = "키캡 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getKeycapStats() {
        try {
            Map<String, Object> stats = keycapService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 키캡 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 