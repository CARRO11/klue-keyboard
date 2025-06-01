package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Switch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwitchRepository extends JpaRepository<Switch, Integer> {
    
    // 검색 메서드들
    Page<Switch> findByNameContainingIgnoreCaseOrStemMaterialContainingIgnoreCase(
        String name, String stemMaterial, Pageable pageable);
    
    // 필터링 메서드들
    List<Switch> findByStemMaterialContainingIgnoreCase(String stemMaterial);
    
    List<Switch> findByLinearScoreGreaterThanEqual(Double minLinearScore);
    
    List<Switch> findByLinearScoreGreaterThanEqualAndTactileScoreGreaterThanEqualAndSoundScoreGreaterThanEqual(
        Double minLinearScore, Double minTactileScore, Double minSoundScore);
    
    List<Switch> findByLinearScoreGreaterThanEqualAndTactileScoreGreaterThanEqualAndSoundScoreGreaterThanEqualAndStemMaterialContainingIgnoreCase(
        Double minLinearScore, Double minTactileScore, Double minSoundScore, String stemMaterial);
    
    // 통계를 위한 메서드들
    @Query("SELECT AVG(s.linearScore) FROM Switch s")
    Double findAverageLinearScore();
    
    @Query("SELECT AVG(s.tactileScore) FROM Switch s")
    Double findAverageTactileScore();
    
    @Query("SELECT AVG(s.soundScore) FROM Switch s")
    Double findAverageSoundScore();
    
    // 상위 스위치들
    List<Switch> findTop3ByOrderByLinearScoreDesc();
    List<Switch> findTop3ByOrderByTactileScoreDesc();
    List<Switch> findTop3ByOrderBySoundScoreDesc();
    
    // 스템 재질별 분포
    @Query("SELECT s.stemMaterial, COUNT(s) FROM Switch s GROUP BY s.stemMaterial")
    List<Object[]> findStemMaterialDistribution();
    
    // 추가 유용한 쿼리들
    @Query("SELECT s FROM Switch s WHERE s.linearScore >= ?1 OR s.tactileScore >= ?1 OR s.soundScore >= ?1")
    List<Switch> findHighQualitySwitches(Double minScore);
    
    @Query("SELECT s FROM Switch s WHERE s.lubrication = ?1")
    List<Switch> findByLubrication(String lubrication);
} 