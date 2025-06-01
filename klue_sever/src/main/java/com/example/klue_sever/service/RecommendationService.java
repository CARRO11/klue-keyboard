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
} 