package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Gasket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasketRepository extends JpaRepository<Gasket, Integer> {
    
    // 검색 메서드들
    Page<Gasket> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrTypingContainingIgnoreCase(
        String name, String material, String typing, Pageable pageable);
    
    // 필터링 메서드들
    List<Gasket> findByMaterialContainingIgnoreCase(String material);
    List<Gasket> findByTypingContainingIgnoreCase(String typing);
    List<Gasket> findByMaterialContainingIgnoreCaseAndTypingContainingIgnoreCase(String material, String typing);
    
    // 통계를 위한 메서드들
    @Query("SELECT COALESCE(g.material, 'N/A'), COUNT(g) FROM Gasket g GROUP BY g.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT COALESCE(g.typing, 'N/A'), COUNT(g) FROM Gasket g GROUP BY g.typing")
    List<Object[]> findTypingDistribution();
    
    // 추가 유용한 쿼리들
    List<Gasket> findByMaterial(String material);
    List<Gasket> findByTyping(String typing);
} 