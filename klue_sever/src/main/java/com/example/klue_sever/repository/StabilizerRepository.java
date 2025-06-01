package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Stabilizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StabilizerRepository extends JpaRepository<Stabilizer, Integer> {
    
    // 검색 메서드들
    Page<Stabilizer> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
        String name, String material, String size, Pageable pageable);
    
    // 필터링 메서드들
    List<Stabilizer> findByMaterialContainingIgnoreCase(String material);
    List<Stabilizer> findBySizeContainingIgnoreCase(String size);
    List<Stabilizer> findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(String material, String size);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(s.material, 'N/A'), COUNT(s) FROM Stabilizer s GROUP BY s.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT COALESCE(s.size, 'N/A'), COUNT(s) FROM Stabilizer s GROUP BY s.size")
    List<Object[]> findSizeDistribution();
    
    // 추가 유용한 쿼리들
    List<Stabilizer> findByMaterial(String material);
    List<Stabilizer> findBySize(String size);
} 