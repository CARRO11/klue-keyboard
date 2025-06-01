package com.example.klue_sever.dto;

import lombok.Data;

@Data
public class RecommendationRequest {
    private String purpose;  // gaming, typing, programming
    private Double budget;
    private String preferredSound;  // silent, thocky, clacky, poppy
    private String switchType;  // linear, tactile, clicky
    private Boolean backlightRequired;
    private Boolean lubeRequired;
} 