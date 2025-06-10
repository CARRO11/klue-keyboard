package com.example.klue_sever.service;

import com.example.klue_sever.dto.RecommendationRequest;
import com.example.klue_sever.dto.RecommendationResponse;
import com.example.klue_sever.repository.SwitchRepository;
import com.example.klue_sever.repository.KeycapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    private final SwitchRepository switchRepository;
    private final KeycapRepository keycapRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecommendationService(
            SwitchRepository switchRepository,
            KeycapRepository keycapRepository,
            ObjectMapper objectMapper) {
        this.switchRepository = switchRepository;
        this.keycapRepository = keycapRepository;
        this.objectMapper = objectMapper;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest request) {
        try {
            logger.info("추천 요청 받음: {}", request);
            
            // 간단한 추천 로직
            Map<String, Object> recommendations = new HashMap<>();
            recommendations.put("switches", "추천된 스위치");
            recommendations.put("keycaps", "추천된 키캡");
            
            return RecommendationResponse.builder()
                    .recommendedComponents(recommendations)
                    .recommendationReason("요청에 맞는 부품을 추천했습니다.")
                    .recommendationScore(0.85)
                    .build();

        } catch (Exception e) {
            logger.error("추천 처리 중 오류: ", e);
            throw new RuntimeException("부품 추천 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getRecommendationByCondition(String condition) {
        try {
            logger.info("조건별 추천 요청: {}", condition);
            
            // 기본 부품 개수 조회
            int switchCount = 0;
            int keycapCount = 0;
            
            try {
                switchCount = (int) switchRepository.count();
                keycapCount = (int) keycapRepository.count();
            } catch (Exception e) {
                logger.warn("부품 개수 조회 중 오류: {}", e.getMessage());
            }

            // 조건을 기반으로 한 추천 로직
            Map<String, Object> response = new HashMap<>();
            response.put("condition", condition);
            response.put("recommendations", getComponentsByCondition(condition, switchCount, keycapCount));
            response.put("message", "조건에 맞는 부품을 추천했습니다: " + condition);
            
            logger.info("조건별 추천 응답 생성 완료");
            return response;
        } catch (Exception e) {
            logger.error("조건별 추천 처리 중 오류: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "추천 처리 중 오류 발생: " + e.getMessage());
            return errorResponse;
        }
    }

    public Map<String, Object> getFullRecommendation(Map<String, Object> fullPreferences) {
        try {
            logger.info("전체 추천 요청: {}", fullPreferences);
            
            Map<String, Object> response = new HashMap<>();
            response.put("preferences", fullPreferences);
            response.put("recommendations", getBasicRecommendations());
            response.put("message", "전체 선호도를 기반으로 키보드를 추천했습니다.");
            
            return response;
        } catch (Exception e) {
            logger.error("전체 추천 처리 중 오류: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "전체 추천 처리 중 오류 발생: " + e.getMessage());
            return errorResponse;
        }
    }

    private Map<String, Object> getComponentsByCondition(String condition, int switchCount, int keycapCount) {
        Map<String, Object> recommendations = new HashMap<>();
        
        // 기본 추천 로직만 사용
        logger.info("기본 추천 로직 사용");
        if (condition.contains("조용한") || condition.contains("사무용")) {
            recommendations.put("switchType", "선형 스위치");
            recommendations.put("reason", "사무용으로 조용한 선형 스위치를 추천합니다.");
        } else if (condition.contains("게이밍") || condition.contains("게임")) {
            recommendations.put("switchType", "클릭 스위치");
            recommendations.put("reason", "게이밍용으로 빠른 반응의 클릭 스위치를 추천합니다.");
        } else {
            recommendations.put("switchType", "촉각 스위치");
            recommendations.put("reason", "일반적인 용도로 촉각 피드백이 좋은 스위치를 추천합니다.");
        }
        recommendations.put("ai_powered", false);
        
        // 사용 가능한 부품 정보 추가
        recommendations.put("available_switches", switchCount);
        recommendations.put("available_keycaps", keycapCount);
        
        return recommendations;
    }

    private Map<String, Object> getBasicRecommendations() {
        Map<String, Object> recommendations = new HashMap<>();
        
        recommendations.put("switchType", "추천 스위치");
        recommendations.put("keycapType", "추천 키캡");
        recommendations.put("reason", "기본 추천 로직을 사용했습니다.");
        
        return recommendations;
    }
} 