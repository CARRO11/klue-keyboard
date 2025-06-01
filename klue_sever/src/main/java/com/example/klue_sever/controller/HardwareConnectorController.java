package com.example.klue_sever.controller;

import com.example.klue_sever.entity.HardwareConnector;
import com.example.klue_sever.service.HardwareConnectorService;
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
@RequestMapping("/api/hardware-connectors")
@Tag(name = "🔩 하드웨어 커넥터 API", description = "키보드 하드웨어 커넥터 CRUD 관리")
public class HardwareConnectorController {

    private final HardwareConnectorService hardwareConnectorService;

    @Autowired
    public HardwareConnectorController(HardwareConnectorService hardwareConnectorService) {
        this.hardwareConnectorService = hardwareConnectorService;
    }

    @GetMapping
    @Operation(summary = "모든 하드웨어 커넥터 조회", description = "페이지네이션과 정렬을 지원하는 하드웨어 커넥터 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllHardwareConnectors(
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
        Page<HardwareConnector> connectorPage = hardwareConnectorService.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("hardwareConnectors", connectorPage.getContent());
        response.put("currentPage", connectorPage.getNumber());
        response.put("totalItems", connectorPage.getTotalElements());
        response.put("totalPages", connectorPage.getTotalPages());
        response.put("pageSize", connectorPage.getSize());
        response.put("isFirst", connectorPage.isFirst());
        response.put("isLast", connectorPage.isLast());
        response.put("message", "✅ 하드웨어 커넥터 목록 조회 성공 (총 " + connectorPage.getTotalElements() + "개)");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "하드웨어 커넥터 상세 조회", description = "ID로 특정 하드웨어 커넥터의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getHardwareConnectorById(
            @Parameter(description = "하드웨어 커넥터 ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<HardwareConnector> connectorOpt = hardwareConnectorService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (connectorOpt.isPresent()) {
            response.put("hardwareConnector", connectorOpt.get());
            response.put("message", "✅ 하드웨어 커넥터 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 하드웨어 커넥터를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 하드웨어 커넥터 생성", description = "새로운 하드웨어 커넥터를 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createHardwareConnector(
            @Parameter(description = "생성할 하드웨어 커넥터 정보")
            @RequestBody HardwareConnector hardwareConnector) {
        
        try {
            HardwareConnector savedConnector = hardwareConnectorService.save(hardwareConnector);
            Map<String, Object> response = new HashMap<>();
            response.put("hardwareConnector", savedConnector);
            response.put("message", "✅ 하드웨어 커넥터 '" + savedConnector.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 하드웨어 커넥터 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "하드웨어 커넥터 정보 수정", description = "기존 하드웨어 커넥터의 정보를 수정")
    public ResponseEntity<Map<String, Object>> updateHardwareConnector(
            @Parameter(description = "수정할 하드웨어 커넥터 ID", example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "수정할 하드웨어 커넥터 정보")
            @RequestBody HardwareConnector connectorDetails) {
        
        try {
            HardwareConnector updatedConnector = hardwareConnectorService.update(id, connectorDetails);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedConnector != null) {
                response.put("hardwareConnector", updatedConnector);
                response.put("message", "✅ 하드웨어 커넥터 '" + updatedConnector.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 하드웨어 커넥터를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 하드웨어 커넥터 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "하드웨어 커넥터 삭제", description = "ID로 특정 하드웨어 커넥터 삭제")
    public ResponseEntity<Map<String, Object>> deleteHardwareConnector(
            @Parameter(description = "삭제할 하드웨어 커넥터 ID", example = "1")
            @PathVariable Integer id) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (hardwareConnectorService.exists(id)) {
            hardwareConnectorService.delete(id);
            response.put("message", "✅ ID " + id + "번 하드웨어 커넥터가 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 하드웨어 커넥터를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "하드웨어 커넥터 검색", description = "이름, 재질, 크기로 하드웨어 커넥터 검색")
    public ResponseEntity<Map<String, Object>> searchHardwareConnectors(
            @Parameter(description = "검색 키워드", example = "나사")
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HardwareConnector> results = hardwareConnectorService.searchByKeyword(keyword, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("hardwareConnectors", results.getContent());
            response.put("currentPage", results.getNumber());
            response.put("totalItems", results.getTotalElements());
            response.put("totalPages", results.getTotalPages());
            response.put("keyword", keyword);
            response.put("message", "🔍 검색 결과: " + results.getTotalElements() + "개 하드웨어 커넥터 발견");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "하드웨어 커넥터 필터링", description = "재질과 크기로 하드웨어 커넥터 필터링")
    public ResponseEntity<Map<String, Object>> filterHardwareConnectors(
            @Parameter(description = "재질", example = "스테인리스 스틸")
            @RequestParam(required = false) String material,
            
            @Parameter(description = "크기", example = "M2")
            @RequestParam(required = false) String size) {
        
        try {
            List<HardwareConnector> results = hardwareConnectorService.filterHardwareConnectors(material, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("hardwareConnectors", results);
            response.put("totalCount", results.size());
            response.put("filters", Map.of(
                "material", material != null ? material : "제한없음",
                "size", size != null ? size : "제한없음"
            ));
            response.put("message", "🎯 필터링 결과: " + results.size() + "개 하드웨어 커넥터");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 필터링 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "하드웨어 커넥터 통계", description = "하드웨어 커넥터 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getHardwareConnectorStats() {
        try {
            Map<String, Object> stats = hardwareConnectorService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 하드웨어 커넥터 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 