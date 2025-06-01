package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Switch;
import com.example.klue_sever.service.SwitchService;
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
@RequestMapping("/api/switches")
@Tag(name = "🔘 스위치 API", description = "키보드 스위치 CRUD 관리")
public class SwitchController {

    private final SwitchService switchService;

    @Autowired
    public SwitchController(SwitchService switchService) {
        this.switchService = switchService;
    }

    @GetMapping
    @Operation(summary = "모든 스위치 조회", description = "페이지네이션과 정렬을 지원하는 스위치 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllSwitches(
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
        Page<Switch> switchPage = switchService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("switches", switchPage.getContent());
        response.put("currentPage", switchPage.getNumber());
        response.put("totalItems", switchPage.getTotalElements());
        response.put("totalPages", switchPage.getTotalPages());
        response.put("pageSize", switchPage.getSize());
        response.put("isFirst", switchPage.isFirst());
        response.put("isLast", switchPage.isLast());
        response.put("message", "✅ 스위치 목록 조회 성공 (총 " + switchPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "스위치 상세 조회", description = "ID로 특정 스위치의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getSwitchById(
            @Parameter(description = "스위치 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<Switch> switchOpt = switchService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (switchOpt.isPresent()) {
            response.put("switch", switchOpt.get());
            response.put("message", "✅ 스위치 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 스위치를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 스위치 생성", description = "새로운 스위치를 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createSwitch(
            @Parameter(description = "생성할 스위치 정보")
            @RequestBody Switch switch_) {
        
        try {
            Switch savedSwitch = switchService.save(switch_);
            Map<String, Object> response = new HashMap<>();
            response.put("switch", savedSwitch);
            response.put("message", "✅ 스위치 '" + savedSwitch.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 스위치 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "스위치 정보 수정", description = "기존 스위치의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateSwitch(
            @Parameter(description = "수정할 스위치 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 스위치 정보")
            @RequestBody Switch switchDetails) {
        
        try {
            Switch updatedSwitch = switchService.update(id, switchDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedSwitch != null) {
                response.put("switch", updatedSwitch);
                response.put("message", "✅ 스위치 '" + updatedSwitch.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 스위치를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 스위치 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스위치 삭제", description = "ID로 특정 스위치 삭제")
    public ResponseEntity<Map<String, Object>> deleteSwitch(
            @Parameter(description = "삭제할 스위치 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (switchService.exists(id)) {
            switchService.delete(id);
            response.put("message", "✅ ID " + id + "번 스위치가 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 스위치를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "스위치 검색", description = "이름, 타입, 재질로 스위치 검색")
    public ResponseEntity<Map<String, Object>> searchSwitches(
            @Parameter(description = "검색 키워드", example = "체리")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Switch> results = switchService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("switches", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 스위치 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "스위치 필터링", description = "점수 범위와 타입으로 스위치 필터링")
    public ResponseEntity<Map<String, Object>> filterSwitches(
            @Parameter(description = "최소 리니어 점수", example = "7.0")
            @RequestParam(required = false) Double minLinearScore,
            
            @Parameter(description = "최소 택타일 점수", example = "7.0")
            @RequestParam(required = false) Double minTactileScore,
            
            @Parameter(description = "최소 사운드 점수", example = "7.0")
            @RequestParam(required = false) Double minSoundScore,
            
            @Parameter(description = "스템 재질", example = "POM")
            @RequestParam(required = false) String stemMaterial) {
        
        try {
            List<Switch> results = switchService.filterSwitches(
                minLinearScore, minTactileScore, minSoundScore, stemMaterial);
            
            Map<String, Object> response = new HashMap<>();
            response.put("switches", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "minLinearScore", minLinearScore != null ? minLinearScore : "제한없음",
                "minTactileScore", minTactileScore != null ? minTactileScore : "제한없음",
                "minSoundScore", minSoundScore != null ? minSoundScore : "제한없음",
                "stemMaterial", stemMaterial != null ? stemMaterial : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 스위치");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "스위치 통계", description = "스위치 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getSwitchStats() {
        try {
            Map<String, Object> stats = switchService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 스위치 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 