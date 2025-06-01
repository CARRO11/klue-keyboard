package com.example.klue_sever.repository;

import com.example.klue_sever.entity.SoundDampener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoundDampenerRepository extends JpaRepository<SoundDampener, Integer> {
    
    // 검색 메서드들
    Page<SoundDampener> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
        String name, String material, String size, Pageable pageable);
    
    // 필터링 메서드들
    List<SoundDampener> findByMaterialContainingIgnoreCase(String material);
    List<SoundDampener> findBySizeContainingIgnoreCase(String size);
    List<SoundDampener> findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(String material, String size);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(sd.material, 'N/A'), COUNT(sd) FROM SoundDampener sd GROUP BY sd.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT COALESCE(sd.size, 'N/A'), COUNT(sd) FROM SoundDampener sd GROUP BY sd.size")
    List<Object[]> findSizeDistribution();
    
    // 추가 유용한 쿼리들
    List<SoundDampener> findByMaterial(String material);
    List<SoundDampener> findBySize(String size);
} 