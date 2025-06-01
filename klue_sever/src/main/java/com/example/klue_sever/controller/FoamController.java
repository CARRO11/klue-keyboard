package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Foam;
import com.example.klue_sever.service.FoamService;
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
@RequestMapping("/api/foams")
@Tag(name = "🟣 폼 API", description = "키보드 폼 CRUD 관리")
public class FoamController {

    private final FoamService foamService;

    @Autowired
    public FoamController(FoamService foamService) {
        this.foamService = foamService;
    }

    @GetMapping
    @Operation(summary = "모든 폼 조회", description = "페이지네이션과 정렬을 지원하는 폼 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllFoams(
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
        Page<Foam> foamPage = foamService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("foams", foamPage.getContent());
        response.put("currentPage", foamPage.getNumber());
        response.put("totalItems", foamPage.getTotalElements());
        response.put("totalPages", foamPage.getTotalPages());
        response.put("pageSize", foamPage.getSize());
        response.put("isFirst", foamPage.isFirst());
        response.put("isLast", foamPage.isLast());
        response.put("message", "✅ 폼 목록 조회 성공 (총 " + foamPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "폼 상세 조회", description = "ID로 특정 폼의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getFoamById(
            @Parameter(description = "폼 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Foam> foamOpt = foamService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (foamOpt.isPresent()) {
            response.put("foam", foamOpt.get());
            response.put("message", "✅ 폼 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 폼을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 폼 생성", description = "새로운 폼을 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createFoam(
            @Parameter(description = "생성할 폼 정보")
            @RequestBody Foam foam) {
        
        try {
            Foam savedFoam = foamService.save(foam);
            Map<String, Object> response = new HashMap<>();
            response.put("foam", savedFoam);
            response.put("message", "✅ 폼 '" + savedFoam.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 폼 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "폼 정보 수정", description = "기존 폼의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateFoam(
            @Parameter(description = "수정할 폼 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 폼 정보")
            @RequestBody Foam foamDetails) {
        
        try {
            Foam updatedFoam = foamService.update(id, foamDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedFoam != null) {
                response.put("foam", updatedFoam);
                response.put("message", "✅ 폼 '" + updatedFoam.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 폼을 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 폼 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "폼 삭제", description = "ID로 특정 폼 삭제")
    public ResponseEntity<Map<String, Object>> deleteFoam(
            @Parameter(description = "삭제할 폼 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (foamService.exists(id)) {
            foamService.delete(id);
            response.put("message", "✅ ID " + id + "번 폼이 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 폼을 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "폼 검색", description = "이름, 재질로 폼 검색")
    public ResponseEntity<Map<String, Object>> searchFoams(
            @Parameter(description = "검색 키워드", example = "폴리우레탄")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Foam> results = foamService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("foams", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 폼 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "폼 필터링", description = "재질로 폼 필터링")
    public ResponseEntity<Map<String, Object>> filterFoams(
            @Parameter(description = "재질", example = "폴리우레탄")
            @RequestParam(required = false) String material) {
        
        try {
            List<Foam> results = foamService.filterFoams(material);
            
            Map<String, Object> response = new HashMap<>();
            response.put("foams", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 폼");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "폼 통계", description = "폼 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getFoamStats() {
        try {
            Map<String, Object> stats = foamService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 폼 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 