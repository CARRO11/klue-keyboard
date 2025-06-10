package com.example.klue_sever.controller;

import com.example.klue_sever.entity.PCB;
import com.example.klue_sever.service.PCBService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pcbs")
@Tag(name = "🔌 PCB API", description = "키보드 PCB CRUD 관리")
public class PCBController {

    private final PCBService pcbService;

    @Autowired
    public PCBController(PCBService pcbService) {
        this.pcbService = pcbService;
    }

    @GetMapping
    @Operation(summary = "모든 PCB 조회", description = "모든 PCB 목록 조회")
    public ResponseEntity<Map<String, Object>> getAllPCBs() {
        try {
            List<PCB> pcbs = pcbService.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("pcbs", pcbs);
            response.put("totalCount", pcbs.size());
            response.put("message", "✅ PCB 목록 조회 성공 (총 " + pcbs.size() + "개)");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ PCB 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "PCB 상세 조회", description = "ID로 특정 PCB의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getPCBById(
            @Parameter(description = "PCB ID", example = "1")
            @PathVariable Integer id) {
        
        Optional<PCB> pcbOpt = pcbService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (pcbOpt.isPresent()) {
            response.put("pcb", pcbOpt.get());
            response.put("message", "✅ PCB 조회 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "❌ ID " + id + "번 PCB를 찾을 수 없습니다");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새 PCB 생성", description = "새로운 PCB를 데이터베이스에 추가")
    public ResponseEntity<Map<String, Object>> createPCB(
            @Parameter(description = "생성할 PCB 정보")
            @RequestBody PCB pcb) {
        
        try {
            PCB savedPCB = pcbService.save(pcb);
            Map<String, Object> response = new HashMap<>();
            response.put("pcb", savedPCB);
            response.put("message", "✅ PCB '" + savedPCB.getName() + "' 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ PCB 생성 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "PCB 수정", description = "기존 PCB 정보를 수정")
    public ResponseEntity<Map<String, Object>> updatePCB(
            @Parameter(description = "PCB ID", example = "1")
            @PathVariable Integer id, 
            @Parameter(description = "수정할 PCB 정보")
            @RequestBody PCB pcb) {
        
        try {
            PCB updatedPCB = pcbService.update(id, pcb);
            Map<String, Object> response = new HashMap<>();
            
            if (updatedPCB != null) {
                response.put("pcb", updatedPCB);
                response.put("message", "✅ PCB '" + updatedPCB.getName() + "' 수정 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 PCB를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ PCB 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "PCB 삭제", description = "특정 PCB를 데이터베이스에서 삭제")
    public ResponseEntity<Map<String, Object>> deletePCB(
            @Parameter(description = "PCB ID", example = "1")
            @PathVariable Integer id) {
        
        try {
            Optional<PCB> pcbOpt = pcbService.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (pcbOpt.isPresent()) {
                String pcbName = pcbOpt.get().getName();
                pcbService.deleteById(id);
                response.put("message", "✅ PCB '" + pcbName + "' 삭제 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "❌ ID " + id + "번 PCB를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ PCB 삭제 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "PCB 통계", description = "PCB 데이터 통계 정보")
    public ResponseEntity<Map<String, Object>> getPCBStats() {
        try {
            List<PCB> allPCBs = pcbService.findAll();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_count", allPCBs.size());
            
            Map<String, Object> response = new HashMap<>();
            response.put("statistics", stats);
            response.put("message", "📊 PCB 통계 조회 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "❌ 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 