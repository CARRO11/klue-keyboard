package com.example.klue_sever.controller;

import com.example.klue_sever.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/by-condition")
    public ResponseEntity<Map<String, Object>> getRecommendationByCondition(
            @RequestBody String condition) {
        Map<String, Object> response = recommendationService.getRecommendationByCondition(condition);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/full")
    public ResponseEntity<Map<String, Object>> getFullRecommendation(
            @RequestBody Map<String, Object> fullPreferences) {
        Map<String, Object> response = recommendationService.getFullRecommendation(fullPreferences);
        return ResponseEntity.ok(response);
    }
} 