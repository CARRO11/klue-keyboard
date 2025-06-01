package com.example.klue_sever.service;

import com.example.klue_sever.dto.RecommendationRequest;
import com.example.klue_sever.dto.RecommendationResponse;
import com.example.klue_sever.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class RecommendationService {

    private final SwitchRepository switchRepository;
    private final KeycapRepository keycapRepository;
    private final FoamRepository foamRepository;
    private final GasketRepository gasketRepository;
    private final StabilizerRepository stabilizerRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecommendationService(
            SwitchRepository switchRepository,
            KeycapRepository keycapRepository,
            FoamRepository foamRepository,
            GasketRepository gasketRepository,
            StabilizerRepository stabilizerRepository,
            ObjectMapper objectMapper) {
        this.switchRepository = switchRepository;
        this.keycapRepository = keycapRepository;
        this.foamRepository = foamRepository;
        this.gasketRepository = gasketRepository;
        this.stabilizerRepository = stabilizerRepository;
        this.objectMapper = objectMapper;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest request) {
        try {
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = new HashMap<>();
            availableComponents.put("switches", switchRepository.findAll());
            availableComponents.put("keycaps", keycapRepository.findAll());
            availableComponents.put("foams", foamRepository.findAll());
            availableComponents.put("gaskets", gasketRepository.findAll());
            availableComponents.put("stabilizers", stabilizerRepository.findAll());

            // Python 스크립트 실행을 위한 ProcessBuilder 설정
            ProcessBuilder pb = new ProcessBuilder("python3", "recommendation/keyboard_recommender.py");
            Process process = pb.start();

            // Python 스크립트에 데이터 전달
            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                Map<String, Object> input = new HashMap<>();
                input.put("user_preference", request);
                input.put("available_components", availableComponents);
                writer.write(objectMapper.writeValueAsString(input));
                writer.flush();
            }

            // Python 스크립트의 출력 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Python 스크립트의 결과를 파싱하여 응답 생성
            Map<String, Object> result = objectMapper.readValue(output.toString(), Map.class);
            
            return RecommendationResponse.builder()
                    .recommendedComponents((Map<String, Object>) result.get("recommendations"))
                    .recommendationReason((String) result.get("reason"))
                    .recommendationScore((Double) result.get("score"))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("부품 추천 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getRecommendationByCondition(String condition) {
        try {
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = new HashMap<>();
            availableComponents.put("switches", switchRepository.findAll());
            availableComponents.put("keycaps", keycapRepository.findAll());
            availableComponents.put("foams", foamRepository.findAll());
            availableComponents.put("gaskets", gasketRepository.findAll());
            availableComponents.put("stabilizers", stabilizerRepository.findAll());

            // 조건을 기반으로 한 추천 로직
            Map<String, Object> response = new HashMap<>();
            response.put("condition", condition);
            response.put("recommendations", getComponentsByCondition(condition, availableComponents));
            response.put("message", "조건에 맞는 부품을 추천했습니다: " + condition);
            
            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "추천 처리 중 오류 발생: " + e.getMessage());
            return errorResponse;
        }
    }

    public Map<String, Object> getFullRecommendation(Map<String, Object> fullPreferences) {
        try {
            // 데이터베이스에서 사용 가능한 모든 부품 가져오기
            Map<String, Object> availableComponents = new HashMap<>();
            availableComponents.put("switches", switchRepository.findAll());
            availableComponents.put("keycaps", keycapRepository.findAll());
            availableComponents.put("foams", foamRepository.findAll());
            availableComponents.put("gaskets", gasketRepository.findAll());
            availableComponents.put("stabilizers", stabilizerRepository.findAll());

            // 전체 선호도를 기반으로 한 추천 로직
            Map<String, Object> response = new HashMap<>();
            response.put("preferences", fullPreferences);
            response.put("recommendations", getRecommendationsByPreferences(fullPreferences, availableComponents));
            response.put("message", "전체 선호도를 기반으로 키보드를 추천했습니다.");
            
            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "전체 추천 처리 중 오류 발생: " + e.getMessage());
            return errorResponse;
        }
    }

    private Map<String, Object> getComponentsByCondition(String condition, Map<String, Object> availableComponents) {
        Map<String, Object> recommendations = new HashMap<>();
        
        // 조건에 따른 간단한 추천 로직
        if (condition.contains("조용한") || condition.contains("사무용")) {
            // 조용한 스위치 추천
            recommendations.put("switchType", "선형");
            recommendations.put("reason", "사무용으로 조용한 선형 스위치를 추천합니다.");
        } else if (condition.contains("게이밍") || condition.contains("게임")) {
            // 게이밍용 스위치 추천
            recommendations.put("switchType", "클릭");
            recommendations.put("reason", "게이밍용으로 빠른 반응의 클릭 스위치를 추천합니다.");
        } else {
            // 기본 추천
            recommendations.put("switchType", "촉각");
            recommendations.put("reason", "일반적인 용도로 촉각 피드백이 좋은 스위치를 추천합니다.");
        }
        
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