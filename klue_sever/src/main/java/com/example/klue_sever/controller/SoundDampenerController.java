package com.example.klue_sever.controller;

import com.example.klue_sever.entity.SoundDampener;
import com.example.klue_sever.service.SoundDampenerService;
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
@RequestMapping("/api/sound-dampeners")
@Tag(name = "🔇 사운드 댐퍼 API", description = "키보드 사운드 댐퍼 CRUD 관리")
public class SoundDampenerController {

    private final SoundDampenerService soundDampenerService;

    @Autowired
    public SoundDampenerController(SoundDampenerService soundDampenerService) {
        this.soundDampenerService = soundDampenerService;
    }

    @GetMapping
    @Operation(summary = "모든 사운드 댐퍼 조회", description = "페이지네이션과 정렬을 지원하는 사운드 댐퍼 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllSoundDampeners(
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
        Page<SoundDampener> dampenerPage = soundDampenerService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("soundDampeners", dampenerPage.getContent());
        response.put("currentPage", dampenerPage.getNumber());
        response.put("totalItems", dampenerPage.getTotalElements());
        response.put("totalPages", dampenerPage.getTotalPages());
        response.put("pageSize", dampenerPage.getSize());
        response.put("isFirst", dampenerPage.isFirst());
        response.put("isLast", dampenerPage.isLast());
        response.put("message", "✅ 사운드 댐퍼 목록 조회 성공 (총 " + dampenerPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사운드 댐퍼 상세 조회", description = "ID로 특정 사운드 댐퍼의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getSoundDampenerById(
            @Parameter(description = "사운드 댐퍼 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<SoundDampener> dampenerOpt = soundDampenerService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (dampenerOpt.isPresent()) {
            response.put("soundDampener", dampenerOpt.get());
            response.put("message", "✅ 사운드 댐퍼 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 사운드 댐퍼를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 사운드 댐퍼 생성", description = "새로운 사운드 댐퍼를 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createSoundDampener(
            @Parameter(description = "생성할 사운드 댐퍼 정보")
            @RequestBody SoundDampener soundDampener) {
        
        try {
            SoundDampener savedDampener = soundDampenerService.save(soundDampener);
            Map<String, Object> response = new HashMap<>();
            response.put("soundDampener", savedDampener);
            response.put("message", "✅ 사운드 댐퍼 '" + savedDampener.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 사운드 댐퍼 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "사운드 댐퍼 정보 수정", description = "기존 사운드 댐퍼의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateSoundDampener(
            @Parameter(description = "수정할 사운드 댐퍼 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 사운드 댐퍼 정보")
            @RequestBody SoundDampener dampenerDetails) {
        
        try {
            SoundDampener updatedDampener = soundDampenerService.update(id, dampenerDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedDampener != null) {
                response.put("soundDampener", updatedDampener);
                response.put("message", "✅ 사운드 댐퍼 '" + updatedDampener.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 사운드 댐퍼를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 사운드 댐퍼 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사운드 댐퍼 삭제", description = "ID로 특정 사운드 댐퍼 삭제")
    public ResponseEntity<Map<String, Object>> deleteSoundDampener(
            @Parameter(description = "삭제할 사운드 댐퍼 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (soundDampenerService.exists(id)) {
            soundDampenerService.delete(id);
            response.put("message", "✅ ID " + id + "번 사운드 댐퍼가 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 사운드 댐퍼를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "사운드 댐퍼 검색", description = "이름, 재질, 크기로 사운드 댐퍼 검색")
    public ResponseEntity<Map<String, Object>> searchSoundDampeners(
            @Parameter(description = "검색 키워드", example = "소음 차단")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SoundDampener> results = soundDampenerService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("soundDampeners", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 사운드 댐퍼 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "사운드 댐퍼 필터링", description = "재질과 크기로 사운드 댐퍼 필터링")
    public ResponseEntity<Map<String, Object>> filterSoundDampeners(
            @Parameter(description = "재질", example = "발포고무")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "크기", example = "60%")
            @RequestParam(required = false) String size) {
        
        try {
            List<SoundDampener> results = soundDampenerService.filterSoundDampeners(material, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("soundDampeners", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "size", size != null ? size : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 사운드 댐퍼");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "사운드 댐퍼 통계", description = "사운드 댐퍼 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getSoundDampenerStats() {
        try {
            Map<String, Object> stats = soundDampenerService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 사운드 댐퍼 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 