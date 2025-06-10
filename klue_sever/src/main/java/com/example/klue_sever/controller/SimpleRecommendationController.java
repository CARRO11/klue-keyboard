package com.example.klue_sever.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simple-recommendations")
@CrossOrigin(origins = "*")
public class SimpleRecommendationController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "간단한 추천 서비스");
        status.put("status", "활성");
        status.put("message", "정상 작동 중");
        return ResponseEntity.ok(status);
    }

    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> getRecommendation(@RequestBody Map<String, String> request) {
        String condition = request.getOrDefault("condition", "기본");
        
        Map<String, Object> response = new HashMap<>();
        response.put("condition", condition);
        
        if (condition.contains("게이밍")) {
            response.put("switchType", "클릭 스위치");
            response.put("reason", "게이밍용으로 빠른 반응 스위치를 추천합니다.");
        } else if (condition.contains("사무")) {
            response.put("switchType", "선형 스위치");
            response.put("reason", "사무용으로 조용한 스위치를 추천합니다.");
        } else {
            response.put("switchType", "촉각 스위치");
            response.put("reason", "일반적인 용도의 스위치를 추천합니다.");
        }
        
        response.put("ai_powered", false);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
} 