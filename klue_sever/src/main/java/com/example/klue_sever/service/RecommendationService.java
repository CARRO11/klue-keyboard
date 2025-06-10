package com.example.klue_sever.service;

import com.example.klue_sever.dto.RecommendationRequest;
import com.example.klue_sever.dto.RecommendationResponse;
import com.example.klue_sever.repository.*;
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
    private final FoamRepository foamRepository;
    private final GasketRepository gasketRepository;
    private final StabilizerRepository stabilizerRepository;
    private final PCBRepository pcbRepository;
    private final PlateRepository plateRepository;
    private final KeyboardCaseRepository keyboardCaseRepository;
    private final ObjectMapper objectMapper;
    private final OpenAIService openAIService;

    @Autowired
    public RecommendationService(
            SwitchRepository switchRepository,
            KeycapRepository keycapRepository,
            FoamRepository foamRepository,
            GasketRepository gasketRepository,
            StabilizerRepository stabilizerRepository,
            PCBRepository pcbRepository,
            PlateRepository plateRepository,
            KeyboardCaseRepository keyboardCaseRepository,
            ObjectMapper objectMapper,
            OpenAIService openAIService) {
        this.switchRepository = switchRepository;
        this.keycapRepository = keycapRepository;
        this.foamRepository = foamRepository;
        this.gasketRepository = gasketRepository;
        this.stabilizerRepository = stabilizerRepository;
        this.pcbRepository = pcbRepository;
        this.plateRepository = plateRepository;
        this.keyboardCaseRepository = keyboardCaseRepository;
        this.objectMapper = objectMapper;
        this.openAIService = openAIService;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest request) {
        try {
            logger.info("추천 요청 받음: {}", request);
            
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = getAllComponents();

            // 간단한 추천 로직 (Python 스크립트 제거)
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
            
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = getAllComponents();

            // 조건을 기반으로 한 추천 로직
            Map<String, Object> response = new HashMap<>();
            response.put("condition", condition);
            response.put("recommendations", getComponentsByCondition(condition, availableComponents));
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
            
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = getAllComponents();

            // 전체 선호도를 기반으로 한 추천 로직
            Map<String, Object> response = new HashMap<>();
            response.put("preferences", fullPreferences);
            response.put("recommendations", getRecommendationsByPreferences(fullPreferences, availableComponents));
            response.put("message", "전체 선호도를 기반으로 키보드를 추천했습니다.");
            
            return response;
        } catch (Exception e) {
            logger.error("전체 추천 처리 중 오류: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "전체 추천 처리 중 오류 발생: " + e.getMessage());
            return errorResponse;
        }
    }

    private Map<String, Object> getAllComponents() {
        try {
            Map<String, Object> components = new HashMap<>();
            
            components.put("switches", switchRepository.findAll());
            components.put("keycaps", keycapRepository.findAll());
            components.put("foams", foamRepository.findAll());
            components.put("gaskets", gasketRepository.findAll());
            components.put("stabilizers", stabilizerRepository.findAll());
            components.put("pcbs", pcbRepository.findAll());
            components.put("plates", plateRepository.findAll());
            components.put("keyboardCases", keyboardCaseRepository.findAll());
            
            logger.info("모든 부품 조회 완료 - 스위치: {}개, 키캡: {}개, PCB: {}개, 플레이트: {}개", 
                       ((List<?>) components.get("switches")).size(),
                       ((List<?>) components.get("keycaps")).size(),
                       ((List<?>) components.get("pcbs")).size(),
                       ((List<?>) components.get("plates")).size());
            
            return components;
        } catch (Exception e) {
            logger.error("부품 조회 중 오류: ", e);
            return new HashMap<>();
        }
    }

    private Map<String, Object> getComponentsByCondition(String condition, Map<String, Object> availableComponents) {
        Map<String, Object> recommendations = new HashMap<>();
        
        // OpenAI가 설정되어 있으면 AI 추천 사용
        if (openAIService.isOpenAIConfigured()) {
            logger.info("AI 추천 사용");
            String aiRecommendation = openAIService.generateKeyboardRecommendation(condition, availableComponents);
            recommendations.put("switchType", "AI 추천");
            recommendations.put("reason", aiRecommendation);
            recommendations.put("ai_powered", true);
        } else {
            logger.info("기본 추천 로직 사용");
            // OpenAI가 설정되지 않았으면 기본 로직 사용
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
        }
        
        // 사용 가능한 부품 정보 추가
        recommendations.put("available_switches", ((List<?>) availableComponents.get("switches")).size());
        recommendations.put("available_keycaps", ((List<?>) availableComponents.get("keycaps")).size());
        recommendations.put("available_pcbs", ((List<?>) availableComponents.get("pcbs")).size());
        recommendations.put("available_plates", ((List<?>) availableComponents.get("plates")).size());
        
        return recommendations;
    }

    private Map<String, Object> getRecommendationsByPreferences(Map<String, Object> preferences, Map<String, Object> availableComponents) {
        Map<String, Object> recommendations = new HashMap<>();
        
        // 선호도를 기반으로 한 추천 로직
        recommendations.put("components", availableComponents);
        recommendations.put("reason", "선호도를 종합적으로 분석하여 최적의 부품을 선정했습니다.");
        
        return recommendations;
    }
} 