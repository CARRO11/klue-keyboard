package com.example.klue_sever.dto;

import com.example.klue_sever.entity.*;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class RecommendationDto {

    @Data
    public static class Request {
        @NotNull(message = "사용 목적은 필수입니다")
        private String purpose;

        @NotNull(message = "예산은 필수입니다")
        @Min(value = 0, message = "예산은 0보다 커야 합니다")
        private Double budget;

        @NotNull(message = "선호하는 사운드는 필수입니다")
        private String preferredSound;

        @NotNull(message = "스위치 타입은 필수입니다")
        private String switchType;

        private Boolean backlightRequired;
        private Boolean lubeRequired;
    }

    @Data
    @Builder
    public static class Response {
        private String purpose;
        private Double budget;
        private String preferredSound;
        private String switchType;
        private Boolean backlightRequired;
        private Boolean lubeRequired;
        
        private Switch recommendedSwitch;
        private Keycap recommendedKeycap;
        private Foam recommendedFoam;
        private Gasket recommendedGasket;
        private Stabilizer recommendedStabilizer;
        
        private Double recommendationScore;
        private String recommendationReason;
    }
} 