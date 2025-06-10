package com.example.klue_sever.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class OpenAIService {

    @Value("${OPENAI_API_KEY:}")
    private String openaiApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAIService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String generateKeyboardRecommendation(String userRequest, Map<String, Object> availableComponents) {
        if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            return "OpenAI API 키가 설정되지 않았습니다. 기본 추천을 제공합니다.";
        }

        try {
            String prompt = buildPrompt(userRequest, availableComponents);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "당신은 키보드 전문가입니다. 사용자의 요청에 따라 적절한 키보드 부품을 추천해주세요.");
            messages.add(systemMessage);
            
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 500);
            requestBody.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", 
                entity, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                JsonNode choices = jsonResponse.get("choices");
                if (choices != null && choices.size() > 0) {
                    JsonNode message = choices.get(0).get("message");
                    if (message != null) {
                        return message.get("content").asText();
                    }
                }
            }
            
            return "AI 추천 생성 중 오류가 발생했습니다.";
            
        } catch (Exception e) {
            return "AI 추천 생성 실패: " + e.getMessage();
        }
    }

    private String buildPrompt(String userRequest, Map<String, Object> availableComponents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("사용자 요청: ").append(userRequest).append("\n\n");
        prompt.append("사용 가능한 부품들:\n");
        
        if (availableComponents.containsKey("switches")) {
            List<?> switches = (List<?>) availableComponents.get("switches");
            prompt.append("- 스위치: ").append(switches.size()).append("개\n");
        }
        if (availableComponents.containsKey("keycaps")) {
            List<?> keycaps = (List<?>) availableComponents.get("keycaps");
            prompt.append("- 키캡: ").append(keycaps.size()).append("개\n");
        }
        if (availableComponents.containsKey("stabilizers")) {
            List<?> stabilizers = (List<?>) availableComponents.get("stabilizers");
            prompt.append("- 스테빌라이저: ").append(stabilizers.size()).append("개\n");
        }
        
        prompt.append("\n사용자의 요청에 맞는 키보드 구성을 추천해주세요. ");
        prompt.append("추천 이유와 함께 구체적으로 설명해주세요.");
        
        return prompt.toString();
    }

    public boolean isOpenAIConfigured() {
        return openaiApiKey != null && !openaiApiKey.trim().isEmpty();
    }
} 