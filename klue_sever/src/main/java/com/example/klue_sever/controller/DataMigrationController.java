package com.example.klue_sever.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/migration")
@Tag(name = "🚚 데이터 마이그레이션 API", description = "데이터베이스 마이그레이션 및 시딩")
public class DataMigrationController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/execute-sql")
    @Operation(summary = "SQL 스크립트 실행", description = "업로드된 SQL 파일을 데이터베이스에 실행")
    public ResponseEntity<Map<String, Object>> executeSql(
            @RequestParam("sql") String sqlContent) {
        
        Map<String, Object> response = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // SQL 문들을 분리
            String[] sqlStatements = sqlContent.split(";");
            int executedCount = 0;
            int successCount = 0;
            
            for (String sql : sqlStatements) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--") && !sql.startsWith("/*")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                        successCount++;
                    } catch (Exception e) {
                        System.err.println("SQL 실행 실패: " + sql.substring(0, Math.min(50, sql.length())) + "...");
                        System.err.println("오류: " + e.getMessage());
                    }
                    executedCount++;
                }
            }
            
            response.put("message", "✅ SQL 스크립트 실행 완료");
            response.put("total_statements", executedCount);
            response.put("successful_statements", successCount);
            response.put("failed_statements", executedCount - successCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "❌ SQL 실행 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-tables")
    @Operation(summary = "테이블 상태 확인", description = "현재 데이터베이스의 테이블과 데이터 개수 확인")
    public ResponseEntity<Map<String, Object>> checkTables() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Integer> tableCounts = new HashMap<>();
            
            // 주요 테이블들의 데이터 개수 확인
            String[] tables = {"Switches", "Keycap", "KeyboardCase", "Stabilizer", "PCB", "Cable", "Foam", "HardwareConnector"};
            
            for (String table : tables) {
                try {
                    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                    tableCounts.put(table, count != null ? count : 0);
                } catch (Exception e) {
                    tableCounts.put(table, -1); // 테이블이 존재하지 않음
                }
            }
            
            response.put("table_counts", tableCounts);
            response.put("message", "📊 테이블 상태 확인 완료");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "❌ 테이블 확인 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/clear-tables")
    @Operation(summary = "테이블 데이터 삭제", description = "모든 테이블의 데이터를 삭제 (구조는 유지)")
    public ResponseEntity<Map<String, Object>> clearTables() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // 외래 키 체크 비활성화
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
                
                // 주요 테이블들 비우기
                String[] tables = {"PCB", "Plate", "Switches", "Keycap", "KeyboardCase", "Stabilizer", "Cable", "Foam", "HardwareConnector"};
                int clearedCount = 0;
                
                for (String table : tables) {
                    try {
                        stmt.execute("DELETE FROM " + table);
                        clearedCount++;
                    } catch (Exception e) {
                        System.err.println("테이블 " + table + " 삭제 실패: " + e.getMessage());
                    }
                }
                
                // 외래 키 체크 다시 활성화
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
                
                response.put("message", "✅ 테이블 데이터 삭제 완료");
                response.put("cleared_tables", clearedCount);
                
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            response.put("error", "❌ 테이블 삭제 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 