package com.example.klue_sever.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class RecommendationResponse {
    private Map<String, Object> recommendedComponents;
    private String recommendationReason;
    private Double recommendationScore;
} 