package com.example.klue_sever.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        
        // 기본 상태 정보
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "KLUE Keyboard Backend");
        health.put("version", "1.0.0");
        
        // 데이터베이스 연결 확인
        try (Connection connection = dataSource.getConnection()) {
            health.put("database", "UP");
            health.put("database_url", connection.getMetaData().getURL());
        } catch (Exception e) {
            health.put("database", "DOWN");
            health.put("database_error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        
        info.put("app", "KLUE Keyboard Recommendation System");
        info.put("description", "🎹 키보드 부품 추천 AI 서비스");
        info.put("version", "1.0.0");
        info.put("author", "KLUE Team");
        info.put("features", new String[]{
            "키보드 부품 관리",
            "AI 기반 추천",
            "상세 검색 필터링",
            "실시간 가격 비교"
        });
        
        return ResponseEntity.ok(info);
    }
} 