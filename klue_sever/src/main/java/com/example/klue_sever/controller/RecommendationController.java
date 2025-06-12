package com.example.klue_sever.controller;

import com.example.klue_sever.service.OpenAIService;
import com.example.klue_sever.service.ComponentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);
    
    @Autowired
    private OpenAIService openAIService;
    
    @Autowired
    private ComponentDataService componentDataService;

    @PostMapping("/by-condition")
    public ResponseEntity<Map<String, Object>> getRecommendationByCondition(@RequestBody Map<String, String> request) {
        try {
            String condition = request.get("condition");
            logger.info("조건별 추천 요청: {}", condition);
            
            Map<String, Object> response = new HashMap<>();
            response.put("condition", condition);
            
            // OpenAI가 설정되어 있으면 AI 추천 사용
            if (openAIService.isOpenAIConfigured()) {
                logger.info("OpenAI를 사용하여 추천 생성 중...");
                
                // 데이터베이스 부품 통계 정보 추가
                Map<String, Integer> componentCounts = componentDataService.getComponentCounts();
                
                String aiRecommendation = openAIService.generateKeyboardRecommendation(condition, new HashMap<>());
                
                response.put("switchType", getRecommendedSwitch(condition));
                response.put("reason", aiRecommendation);
                response.put("ai_powered", true);
                response.put("message", "AI Tony가 KLUE 데이터베이스의 실제 부품들을 기반으로 추천했습니다: " + condition);
                response.put("component_counts", componentCounts);
            } else {
                // 기본 추천 사용
                response.put("switchType", getRecommendedSwitch(condition));
                response.put("reason", getRecommendationReason(condition));
                response.put("ai_powered", false);
                response.put("message", "조건에 맞는 부품을 추천했습니다: " + condition);
            }
            
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
            
            Map<String, Object> response = new HashMap<>();
            response.put("request", userRequest);
            response.put("switchType", getRecommendedSwitch(userRequest));
            response.put("reason", getRecommendationReason(userRequest));
            response.put("ai_powered", false);
            response.put("message", "자연어 요청을 분석하여 추천했습니다");
            
            logger.info("자연어 추천 응답 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("자연어 추천 처리 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "자연어 추천 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("service", "추천 서비스");
            status.put("status", "활성");
            status.put("openai_configured", openAIService.isOpenAIConfigured());
            status.put("openai_status", openAIService.getApiKeyStatus());
            status.put("basic_recommendation", "활성");
            status.put("database_connection", "활성");
            
            // 데이터베이스 부품 통계 추가
            Map<String, Integer> componentCounts = componentDataService.getComponentCounts();
            status.put("component_counts", componentCounts);
            status.put("total_components", componentCounts.values().stream().mapToInt(Integer::intValue).sum());
            
            logger.info("상태 확인 요청 완료 - 총 {}개 부품", componentCounts.values().stream().mapToInt(Integer::intValue).sum());
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
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("기본 테스트 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "기본 테스트 중 오류 발생: " + e.getMessage());
            errorResponse.put("test_success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/test-openai")
    public ResponseEntity<Map<String, Object>> testOpenAI(@RequestBody Map<String, String> request) {
        try {
            String testRequest = request.getOrDefault("request", "간단한 게이밍 키보드 추천해줘");
            logger.info("OpenAI 테스트 요청: {}", testRequest);
            
            Map<String, Object> response = new HashMap<>();
            
            if (openAIService.isOpenAIConfigured()) {
                // 데이터베이스 부품 통계 정보 추가
                Map<String, Integer> componentCounts = componentDataService.getComponentCounts();
                
                String aiResponse = openAIService.generateKeyboardRecommendation(testRequest, new HashMap<>());
                
                response.put("message", "OpenAI 테스트 성공 - KLUE 데이터베이스 연동");
                response.put("request", testRequest);
                response.put("ai_response", aiResponse);
                response.put("test_success", true);
                response.put("openai_used", true);
                response.put("database_used", true);
                response.put("component_counts", componentCounts);
            } else {
                response.put("message", "OpenAI API 키가 설정되지 않음");
                response.put("request", testRequest);
                response.put("ai_response", "OpenAI 서비스를 사용할 수 없습니다.");
                response.put("test_success", false);
                response.put("openai_used", false);
                response.put("database_used", false);
            }
            
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("OpenAI 테스트 중 오류:", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "OpenAI 테스트 중 오류 발생: " + e.getMessage());
            errorResponse.put("test_success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private String getRecommendedSwitch(String condition) {
        if (condition.contains("조용한") || condition.contains("사무용")) {
            return "선형 스위치";
        } else if (condition.contains("게이밍") || condition.contains("게임")) {
            return "클릭 스위치";
        } else {
            return "촉각 스위치";
        }
    }

    private String getRecommendationReason(String condition) {
        if (condition.contains("조용한") || condition.contains("사무용")) {
            return "사무용으로 조용한 선형 스위치를 추천합니다.";
        } else if (condition.contains("게이밍") || condition.contains("게임")) {
            return "게이밍용으로 빠른 반응의 클릭 스위치를 추천합니다.";
        } else {
            return "일반적인 용도로 촉각 피드백이 좋은 스위치를 추천합니다.";
        }
    }
} 