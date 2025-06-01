package com.example.klue_sever.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@RestController
@RequestMapping("/api/search")
@Tag(name = "🔍 검색 API", description = "키보드 부품 통합 검색 서비스")
public class SearchController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/components")
    @Operation(summary = "통합 부품 검색", description = "키워드를 사용해 모든 키보드 부품을 검색합니다")
    public ResponseEntity<Map<String, Object>> searchComponents(
            @Parameter(description = "검색 키워드", example = "체리") 
            @RequestParam String keyword,
            
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지당 항목 수", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "정렬 기준 (name, startdate)", example = "name") 
            @RequestParam(defaultValue = "name") String sortBy,
            
            @Parameter(description = "정렬 방향 (asc, desc)", example = "asc") 
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> allResults = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            
            // 스위치 검색
            String switchQuery = """
                SELECT 'switch' as type, id, name, pressure, lubrication, stem_material, 
                       linear_score, tactile_score, sound_score, link
                FROM Switches 
                WHERE name LIKE ? OR stem_material LIKE ?
                ORDER BY %s %s
                LIMIT ? OFFSET ?
                """.formatted(getSafeColumnName(sortBy, "name"), sortDirection.toUpperCase());
            
            try (PreparedStatement stmt = conn.prepareStatement(switchQuery)) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setInt(3, size);
                stmt.setInt(4, page * size);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("type", "🔘 스위치");
                    item.put("id", rs.getInt("id"));
                    item.put("name", rs.getString("name"));
                    item.put("pressure", rs.getString("pressure"));
                    item.put("lubrication", rs.getString("lubrication"));
                    item.put("stem_material", rs.getString("stem_material"));
                    item.put("linear_score", rs.getDouble("linear_score"));
                    item.put("tactile_score", rs.getDouble("tactile_score"));
                    item.put("sound_score", rs.getDouble("sound_score"));
                    item.put("link", rs.getString("link"));
                    allResults.add(item);
                }
            }
            
            // 키캡 검색
            String keycapQuery = """
                SELECT 'keycap' as type, id, name, material, profile, link
                FROM Keycap 
                WHERE name LIKE ? OR material LIKE ? OR profile LIKE ?
                LIMIT ? OFFSET ?
                """;
            
            try (PreparedStatement stmt = conn.prepareStatement(keycapQuery)) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setInt(4, size);
                stmt.setInt(5, page * size);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("type", "⌨️ 키캡");
                    item.put("id", rs.getInt("id"));
                    item.put("name", rs.getString("name"));
                    item.put("material", rs.getString("material"));
                    item.put("profile", rs.getString("profile"));
                    item.put("link", rs.getString("link"));
                    allResults.add(item);
                }
            }
            
            // 총 개수 조회
            String countQuery = """
                SELECT 
                    (SELECT COUNT(*) FROM Switches WHERE name LIKE ? OR stem_material LIKE ?) +
                    (SELECT COUNT(*) FROM Keycap WHERE name LIKE ? OR material LIKE ? OR profile LIKE ?) as total
                """;
            
            int totalCount = 0;
            try (PreparedStatement stmt = conn.prepareStatement(countQuery)) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setString(4, searchPattern);
                stmt.setString(5, searchPattern);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalCount = rs.getInt("total");
                }
            }
            
            result.put("results", allResults);
            result.put("totalCount", totalCount);
            result.put("currentPage", page);
            result.put("pageSize", size);
            result.put("totalPages", (int) Math.ceil((double) totalCount / size));
            result.put("keyword", keyword);
            result.put("message", "🔍 검색 결과: " + allResults.size() + "개 항목 (총 " + totalCount + "개)");
            
        } catch (Exception e) {
            result.put("error", "검색 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 부품 조회", description = "가장 인기 있는 키보드 부품들을 조회합니다")
    public ResponseEntity<Map<String, Object>> getPopularComponents() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // 높은 점수의 스위치들
            String popularSwitchesQuery = """
                SELECT name, linear_score, tactile_score, sound_score, 
                       (linear_score + tactile_score + sound_score) / 3 as avg_score
                FROM Switches 
                WHERE linear_score > 8 AND tactile_score > 8 AND sound_score > 8
                ORDER BY avg_score DESC 
                LIMIT 5
                """;
            
            List<Map<String, Object>> popularSwitches = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(popularSwitchesQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", rs.getString("name"));
                    item.put("average_score", Math.round(rs.getDouble("avg_score") * 10) / 10.0);
                    item.put("type", "🔘 고품질 스위치");
                    popularSwitches.add(item);
                }
            }
            
            result.put("popular_switches", popularSwitches);
            result.put("message", "⭐ 인기 부품 " + popularSwitches.size() + "개 조회 완료");
            
        } catch (Exception e) {
            result.put("error", "인기 부품 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    @Operation(summary = "필터링 검색", description = "다양한 조건으로 부품을 필터링합니다")
    public ResponseEntity<Map<String, Object>> filterComponents(
            @Parameter(description = "스위치 타입", example = "linear") 
            @RequestParam(required = false) String switchType,
            
            @Parameter(description = "최소 점수", example = "7.0") 
            @RequestParam(required = false, defaultValue = "0") Double minScore,
            
            @Parameter(description = "재질", example = "PBT") 
            @RequestParam(required = false) String material) {

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> filteredResults = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("""
                SELECT name, pressure, lubrication, stem_material, 
                       linear_score, tactile_score, sound_score
                FROM Switches WHERE 1=1
                """);
            
            List<Object> params = new ArrayList<>();
            
            if (minScore != null && minScore > 0) {
                queryBuilder.append(" AND (linear_score >= ? AND tactile_score >= ? AND sound_score >= ?)");
                params.add(minScore);
                params.add(minScore);
                params.add(minScore);
            }
            
            if (switchType != null && !switchType.isEmpty()) {
                if (switchType.equalsIgnoreCase("linear")) {
                    queryBuilder.append(" AND linear_score > tactile_score");
                } else if (switchType.equalsIgnoreCase("tactile")) {
                    queryBuilder.append(" AND tactile_score > linear_score");
                }
            }
            
            queryBuilder.append(" ORDER BY (linear_score + tactile_score + sound_score) DESC LIMIT 20");
            
            try (PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", rs.getString("name"));
                    item.put("pressure", rs.getString("pressure"));
                    item.put("lubrication", rs.getString("lubrication"));
                    item.put("stem_material", rs.getString("stem_material"));
                    item.put("linear_score", rs.getDouble("linear_score"));
                    item.put("tactile_score", rs.getDouble("tactile_score"));
                    item.put("sound_score", rs.getDouble("sound_score"));
                    filteredResults.add(item);
                }
            }
            
            result.put("results", filteredResults);
            result.put("count", filteredResults.size());
            result.put("filters", Map.of(
                "switchType", switchType != null ? switchType : "전체",
                "minScore", minScore,
                "material", material != null ? material : "전체"
            ));
            result.put("message", "🎯 필터링된 결과: " + filteredResults.size() + "개 부품");
            
        } catch (Exception e) {
            result.put("error", "필터링 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    private String getSafeColumnName(String sortBy, String defaultColumn) {
        Set<String> allowedColumns = Set.of("name", "startdate", "id");
        return allowedColumns.contains(sortBy.toLowerCase()) ? sortBy : defaultColumn;
    }
} 