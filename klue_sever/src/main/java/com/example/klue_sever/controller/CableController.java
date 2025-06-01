package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Cable;
import com.example.klue_sever.service.CableService;
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
@RequestMapping("/api/cables")
@Tag(name = "🔌 케이블 API", description = "키보드 케이블 CRUD 관리")
public class CableController {

    private final CableService cableService;

    @Autowired
    public CableController(CableService cableService) {
        this.cableService = cableService;
    }

    @GetMapping
    @Operation(summary = "모든 케이블 조회", description = "페이지네이션과 정렬을 지원하는 케이블 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllCables(
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
        Page<Cable> cablePage = cableService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("cables", cablePage.getContent());
        response.put("currentPage", cablePage.getNumber());
        response.put("totalItems", cablePage.getTotalElements());
        response.put("totalPages", cablePage.getTotalPages());
        response.put("pageSize", cablePage.getSize());
        response.put("isFirst", cablePage.isFirst());
        response.put("isLast", cablePage.isLast());
        response.put("message", "✅ 케이블 목록 조회 성공 (총 " + cablePage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "케이블 상세 조회", description = "ID로 특정 케이블의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getCableById(
            @Parameter(description = "케이블 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Cable> cableOpt = cableService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (cableOpt.isPresent()) {
            response.put("cable", cableOpt.get());
            response.put("message", "✅ 케이블 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 케이블을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 케이블 생성", description = "새로운 케이블을 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createCable(
            @Parameter(description = "생성할 케이블 정보")
            @RequestBody Cable cable) {
        
        try {
            Cable savedCable = cableService.save(cable);
            Map<String, Object> response = new HashMap<>();
            response.put("cable", savedCable);
            response.put("message", "✅ 케이블 '" + savedCable.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 케이블 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "케이블 정보 수정", description = "기존 케이블의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateCable(
            @Parameter(description = "수정할 케이블 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 케이블 정보")
            @RequestBody Cable cableDetails) {
        
        try {
            Cable updatedCable = cableService.update(id, cableDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedCable != null) {
                response.put("cable", updatedCable);
                response.put("message", "✅ 케이블 '" + updatedCable.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 케이블을 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 케이블 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "케이블 삭제", description = "ID로 특정 케이블 삭제")
    public ResponseEntity<Map<String, Object>> deleteCable(
            @Parameter(description = "삭제할 케이블 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (cableService.exists(id)) {
            cableService.delete(id);
            response.put("message", "✅ ID " + id + "번 케이블이 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 케이블을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "케이블 검색", description = "이름, 재질로 케이블 검색")
    public ResponseEntity<Map<String, Object>> searchCables(
            @Parameter(description = "검색 키워드", example = "실리콘")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Cable> results = cableService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("cables", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 케이블 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "케이블 필터링", description = "재질과 길이로 케이블 필터링")
    public ResponseEntity<Map<String, Object>> filterCables(
            @Parameter(description = "재질", example = "실리콘")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "길이 (cm)", example = "180")
            @RequestParam(required = false) Integer length) {
        
        try {
            List<Cable> results = cableService.filterCables(material, length);
            
            Map<String, Object> response = new HashMap<>();
            response.put("cables", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "length", length != null ? length + "cm" : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 케이블");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "케이블 통계", description = "케이블 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getCableStats() {
        try {
            Map<String, Object> stats = cableService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 케이블 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 