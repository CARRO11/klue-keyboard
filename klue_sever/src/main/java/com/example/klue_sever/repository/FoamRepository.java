package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Foam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoamRepository extends JpaRepository<Foam, Integer> {
    
    // 검색 메서드들 - 실제 필드에 맞게 수정
    Page<Foam> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCase(
        String name, String material, Pageable pageable);
    
    // 필터링 메서드들
    List<Foam> findByMaterialContainingIgnoreCase(String material);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(f.material, 'N/A'), COUNT(f) FROM Foam f GROUP BY f.material")
    List<Object[]> findMaterialDistribution();
    
    // 추가 유용한 쿼리들
    List<Foam> findByMaterial(String material);
} 