package com.example.klue_sever.controller;

import com.example.klue_sever.dto.RecommendationRequest;
import com.example.klue_sever.dto.RecommendationResponse;
import com.example.klue_sever.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);
    
    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/recommendation")
    public ResponseEntity<RecommendationResponse> getRecommendation(@RequestBody RecommendationRequest request) {
        try {
            logger.info("추천 요청 받음: {}", request);
            RecommendationResponse response = recommendationService.getRecommendation(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("추천 처리 중 오류:", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/by-condition")
    public ResponseEntity<Map<String, Object>> getRecommendationByCondition(@RequestBody Map<String, String> request) {
        try {
            String condition = request.get("condition");
            logger.info("조건별 추천 요청: {}", condition);
            
            Map<String, Object> response = recommendationService.getRecommendationByCondition(condition);
            logger.info("추천 응답 생성 완료");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("조건별 추천 처리 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "추천 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/natural")
    public ResponseEntity<Map<String, Object>> getNaturalRecommendation(@RequestBody Map<String, String> request) {
        try {
            String userRequest = request.get("request");
            logger.info("자연어 추천 요청: {}", userRequest);
            
            Map<String, Object> response = recommendationService.getRecommendationByCondition(userRequest);
            logger.info("자연어 추천 응답 생성 완료");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("자연어 추천 처리 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "자연어 추천 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/full")
    public ResponseEntity<Map<String, Object>> getFullRecommendation(@RequestBody Map<String, Object> preferences) {
        try {
            logger.info("전체 선호도 추천 요청: {}", preferences);
            Map<String, Object> response = recommendationService.getFullRecommendation(preferences);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("전체 추천 처리 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "전체 추천 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("service", "추천 서비스");
            status.put("status", "활성");
            status.put("openai_configured", false);  // 임시로 false 설정
            status.put("openai_status", "OpenAI 서비스 비활성화됨");
            
            logger.info("상태 확인 요청 완료");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("상태 확인 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "상태 확인 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/test-basic")
    public ResponseEntity<Map<String, Object>> testBasic(@RequestBody Map<String, String> request) {
        try {
            String testRequest = request.getOrDefault("request", "기본 추천 테스트");
            logger.info("기본 추천 테스트 요청: {}", testRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "기본 추천 서비스가 정상 작동합니다.");
            response.put("request", testRequest);
            response.put("test_success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("기본 테스트 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "기본 테스트 중 오류 발생: " + e.getMessage());
            errorResponse.put("test_success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
} 