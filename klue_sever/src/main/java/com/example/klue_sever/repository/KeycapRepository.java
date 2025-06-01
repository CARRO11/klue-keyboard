package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Keycap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeycapRepository extends JpaRepository<Keycap, Integer> {
    
    // 검색 메서드들
    Page<Keycap> findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrProfileContainingIgnoreCase(
        String name, String material, String profile, Pageable pageable);
    
    // 필터링 메서드들
    List<Keycap> findByMaterialContainingIgnoreCase(String material);
    List<Keycap> findByProfileContainingIgnoreCase(String profile);
    List<Keycap> findByMaterialContainingIgnoreCaseAndProfileContainingIgnoreCase(String material, String profile);
    
    // 통계를 위한 메서드들
    @Query("SELECT k.material, COUNT(k) FROM Keycap k GROUP BY k.material")
    List<Object[]> findMaterialDistribution();
    
    @Query("SELECT k.profile, COUNT(k) FROM Keycap k GROUP BY k.profile")
    List<Object[]> findProfileDistribution();
    
    // 추가 유용한 쿼리들
    List<Keycap> findByMaterial(String material);
    List<Keycap> findByProfile(String profile);
} 