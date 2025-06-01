package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Cable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CableRepository extends JpaRepository<Cable, Integer> {
    
    // 검색 메서드들 - 실제 필드명에 맞게 수정
    Page<Cable> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCase(
        String name, String material, Pageable pageable);
    
    // 필터링 메서드들
    List<Cable> findByMaterialContainingIgnoreCase(String material);
    List<Cable> findByLength(Integer length);
    List<Cable> findByMaterialContainingIgnoreCaseAndLength(String material, Integer length);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(c.material, 'N/A'), COUNT(c) FROM Cable c GROUP BY c.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT COALESCE(CAST(c.length AS string), 'N/A'), COUNT(c) FROM Cable c GROUP BY c.length")
    List<Object[]> findLengthDistribution();
    
    // 추가 유용한 쿼리들
    List<Cable> findByMaterial(String material);
} 