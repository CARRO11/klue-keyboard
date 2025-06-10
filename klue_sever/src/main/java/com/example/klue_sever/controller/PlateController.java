package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Plate;
import com.example.klue_sever.entity.KeyboardCase;
import com.example.klue_sever.service.PlateService;
import com.example.klue_sever.service.KeyboardCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/plates")
public class PlateController {

    private static final Logger logger = LoggerFactory.getLogger(PlateController.class);

    private final PlateService plateService;
    private final KeyboardCaseService keyboardCaseService;

    @Autowired
    public PlateController(PlateService plateService, KeyboardCaseService keyboardCaseService) {
        this.plateService = plateService;
        this.keyboardCaseService = keyboardCaseService;
    }

    @PostMapping
    public ResponseEntity<?> createPlate(@RequestBody Map<String, Object> plateData) {
        try {
            logger.info("Plate 생성 요청: {}", plateData);
            
            String name = (String) plateData.get("name");
            String description = (String) plateData.get("description");
            
            // KeyboardCase 처리
            Map<String, Object> keyboardCaseData = (Map<String, Object>) plateData.get("keyboardCase");
            Integer keyboardCaseId = null;
            
            if (keyboardCaseData != null && keyboardCaseData.get("id") != null) {
                keyboardCaseId = (Integer) keyboardCaseData.get("id");
            }
            
            if (keyboardCaseId == null) {
                logger.error("KeyboardCase ID가 제공되지 않았습니다.");
                return ResponseEntity.badRequest().body("KeyboardCase ID가 필요합니다.");
            }
            
            // KeyboardCase 존재 확인
            KeyboardCase keyboardCase = keyboardCaseService.findById(keyboardCaseId)
                    .orElse(null);
            
            if (keyboardCase == null) {
                logger.error("KeyboardCase를 찾을 수 없습니다: {}", keyboardCaseId);
                return ResponseEntity.badRequest().body("유효하지 않은 KeyboardCase ID입니다.");
            }
            
            // Plate 생성
            Plate plate = Plate.builder()
                    .name(name)
                    .description(description)
                    .keyboardCase(keyboardCase)
                    .build();
            
            Plate savedPlate = plateService.save(plate);
            logger.info("Plate 생성 성공: {}", savedPlate.getName());
            
            return ResponseEntity.ok(savedPlate);
            
        } catch (Exception e) {
            logger.error("Plate 생성 중 오류 발생: ", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Plate 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPlates() {
        try {
            List<Plate> plates = plateService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("plates", plates);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Plate 목록 조회 중 오류 발생: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Plate 목록 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plate> getPlateById(@PathVariable Integer id) {
        return plateService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plate> updatePlate(@PathVariable Integer id, @RequestBody Plate plate) {
        Plate updatedPlate = plateService.update(id, plate);
        if (updatedPlate != null) {
            return ResponseEntity.ok(updatedPlate);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlate(@PathVariable Integer id) {
        plateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 