package com.example.klue_sever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${openai.api.key:${OPENAI_API_KEY:}}")
    private String openaiApiKey;

    @Autowired
    private ComponentDataService componentDataService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAIService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String generateKeyboardRecommendation(String userRequest, Map<String, Object> availableComponents) {
        logger.info("OpenAI API 키 상태: {}", openaiApiKey != null && !openaiApiKey.trim().isEmpty() ? "설정됨" : "설정안됨");
        
        if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            logger.warn("OpenAI API 키가 설정되지 않았습니다.");
            return "OpenAI API 키가 설정되지 않았습니다. 기본 추천을 제공합니다.";
        }

        try {
            logger.info("OpenAI API 호출 시작 - 사용자 요청: {}", userRequest);
            
            // 실제 데이터베이스에서 부품 정보 가져오기
            Map<String, Object> dbComponents = componentDataService.getAllComponentsForAI();
            logger.info("데이터베이스에서 부품 정보 수집 완료: {}개 카테고리", dbComponents.size());
            
            String prompt = buildPrompt(userRequest, dbComponents);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "당신은 키보드 전문가입니다. 사용자의 요청에 따라 적절한 키보드 부품을 추천해주세요. 응답은 한국어로 해주세요.");
            messages.add(systemMessage);
            
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 800);
            requestBody.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            logger.info("OpenAI API 요청 전송 중...");
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", 
                entity, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("OpenAI API 호출 성공");
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                JsonNode choices = jsonResponse.get("choices");
                if (choices != null && choices.size() > 0) {
                    JsonNode message = choices.get(0).get("message");
                    if (message != null) {
                        String result = message.get("content").asText();
                        logger.info("OpenAI 응답 생성 완료");
                        return result;
                    }
                }
            } else {
                logger.error("OpenAI API 호출 실패 - Status: {}", response.getStatusCode());
            }
            
            return "AI 추천 생성 중 오류가 발생했습니다.";
            
        } catch (Exception e) {
            logger.error("OpenAI API 호출 중 예외 발생: ", e);
            return "AI 추천 생성 실패: " + e.getMessage();
        }
    }

    private String buildPrompt(String userRequest, Map<String, Object> availableComponents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("사용자 요청: ").append(userRequest).append("\n\n");
        
        prompt.append("KLUE 데이터베이스에서 실제 사용 가능한 키보드 부품들:\n\n");
        
        // 스위치 정보
        if (availableComponents.containsKey("switches")) {
            List<Map<String, Object>> switches = (List<Map<String, Object>>) availableComponents.get("switches");
            prompt.append("🔘 스위치 (").append(switches.size()).append("개):\n");
            switches.stream().limit(5).forEach(s -> {
                prompt.append("  - ").append(s.get("name"))
                      .append(" (").append(s.get("type")).append(")")
                      .append(" - 압력: ").append(s.get("pressure")).append("g\n");
            });
            if (switches.size() > 5) {
                prompt.append("  ... 및 ").append(switches.size() - 5).append("개 더\n");
            }
            prompt.append("\n");
        }
        
        // 키캡 정보
        if (availableComponents.containsKey("keycaps")) {
            List<Map<String, Object>> keycaps = (List<Map<String, Object>>) availableComponents.get("keycaps");
            prompt.append("🎩 키캡 (").append(keycaps.size()).append("개):\n");
            keycaps.stream().limit(5).forEach(k -> {
                prompt.append("  - ").append(k.get("name"))
                      .append(" (").append(k.get("material")).append(", ")
                      .append(k.get("profile")).append(")\n");
            });
            if (keycaps.size() > 5) {
                prompt.append("  ... 및 ").append(keycaps.size() - 5).append("개 더\n");
            }
            prompt.append("\n");
        }
        
        // PCB 정보
        if (availableComponents.containsKey("pcbs")) {
            List<Map<String, Object>> pcbs = (List<Map<String, Object>>) availableComponents.get("pcbs");
            prompt.append("🔌 PCB (").append(pcbs.size()).append("개):\n");
            pcbs.stream().limit(3).forEach(p -> {
                prompt.append("  - ").append(p.get("name"))
                      .append(" (").append(p.get("layout")).append(")")
                      .append(Boolean.TRUE.equals(p.get("hotswap")) ? " - 핫스왑 지원" : "")
                      .append(Boolean.TRUE.equals(p.get("wireless")) ? " - 무선" : "")
                      .append("\n");
            });
            if (pcbs.size() > 3) {
                prompt.append("  ... 및 ").append(pcbs.size() - 3).append("개 더\n");
            }
            prompt.append("\n");
        }
        
        // 플레이트 정보
        if (availableComponents.containsKey("plates")) {
            List<Map<String, Object>> plates = (List<Map<String, Object>>) availableComponents.get("plates");
            prompt.append("📏 플레이트 (").append(plates.size()).append("개):\n");
            plates.stream().limit(3).forEach(p -> {
                prompt.append("  - ").append(p.get("name"))
                      .append(" (").append(p.get("material")).append(")\n");
            });
            if (plates.size() > 3) {
                prompt.append("  ... 및 ").append(plates.size() - 3).append("개 더\n");
            }
            prompt.append("\n");
        }
        
        // 기타 부품들
        if (availableComponents.containsKey("stabilizers")) {
            List<Map<String, Object>> stabilizers = (List<Map<String, Object>>) availableComponents.get("stabilizers");
            prompt.append("⚖️ 스테빌라이저: ").append(stabilizers.size()).append("개\n");
        }
        if (availableComponents.containsKey("gaskets")) {
            List<Map<String, Object>> gaskets = (List<Map<String, Object>>) availableComponents.get("gaskets");
            prompt.append("🔧 가스켓: ").append(gaskets.size()).append("개\n");
        }
        if (availableComponents.containsKey("foams")) {
            List<Map<String, Object>> foams = (List<Map<String, Object>>) availableComponents.get("foams");
            prompt.append("🧽 폼: ").append(foams.size()).append("개\n");
        }
        if (availableComponents.containsKey("cables")) {
            List<Map<String, Object>> cables = (List<Map<String, Object>>) availableComponents.get("cables");
            prompt.append("🔌 케이블: ").append(cables.size()).append("개\n");
        }
        
        prompt.append("\n위 실제 KLUE 데이터베이스의 부품들 중에서 사용자의 요청에 가장 적합한 조합을 추천해주세요. ");
        prompt.append("구체적인 부품명을 포함하여 추천 이유와 함께 설명해주세요. ");
        prompt.append("응답은 한국어로 작성하며, 400자 이내로 간결하고 전문적으로 작성해주세요.");
        
        return prompt.toString();
    }

    public boolean isOpenAIConfigured() {
        boolean configured = openaiApiKey != null && !openaiApiKey.trim().isEmpty();
        logger.info("OpenAI 설정 상태: {}", configured ? "활성화됨" : "비활성화됨");
        return configured;
    }

    public String getApiKeyStatus() {
        if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            return "OpenAI API 키가 설정되지 않았습니다.";
        }
        return "OpenAI API 키가 설정되어 있습니다. (마지막 4자리: ****" + 
               openaiApiKey.substring(Math.max(0, openaiApiKey.length() - 4)) + ")";
    }
} 