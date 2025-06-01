package com.example.klue_sever.repository;

import com.example.klue_sever.entity.HardwareConnector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareConnectorRepository extends JpaRepository<HardwareConnector, Integer> {
    
    // 검색 메서드들
    Page<HardwareConnector> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
        String name, String material, String size, Pageable pageable);
    
    // 필터링 메서드들
    List<HardwareConnector> findByMaterialContainingIgnoreCase(String material);
    List<HardwareConnector> findBySizeContainingIgnoreCase(String size);
    List<HardwareConnector> findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(String material, String size);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(hc.material, 'N/A'), COUNT(hc) FROM HardwareConnector hc GROUP BY hc.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT COALESCE(hc.size, 'N/A'), COUNT(hc) FROM HardwareConnector hc GROUP BY hc.size")
    List<Object[]> findSizeDistribution();
    
    // 추가 유용한 쿼리들
    List<HardwareConnector> findByMaterial(String material);
    List<HardwareConnector> findBySize(String size);
} 