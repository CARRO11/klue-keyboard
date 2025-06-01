package com.example.klue_sever.service;

import com.example.klue_sever.entity.Switch;
import com.example.klue_sever.repository.SwitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SwitchService {

    private final SwitchRepository switchRepository;

    @Autowired
    public SwitchService(SwitchRepository switchRepository) {
        this.switchRepository = switchRepository;
    }

    public Switch save(Switch switch_) {
        return switchRepository.save(switch_);
    }

    public List<Switch> findAll() {
        return switchRepository.findAll();
    }

    public Page<Switch> findAll(Pageable pageable) {
        return switchRepository.findAll(pageable);
    }

    public Optional<Switch> findById(Integer id) {
        return switchRepository.findById(id);
    }

    public void delete(Integer id) {
        switchRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return switchRepository.existsById(id);
    }

    public long count() {
        return switchRepository.count();
    }

    public Switch update(Integer id, Switch switchDetails) {
        if (switchRepository.existsById(id)) {
            switchDetails.setId(id);
            return switchRepository.save(switchDetails);
        }
        return null;
    }

    public Page<Switch> searchByKeyword(String keyword, Pageable pageable) {
        return switchRepository.findByNameContainingIgnoreCaseOrStemMaterialContainingIgnoreCase(
            keyword, keyword, pageable);
    }

    public List<Switch> filterSwitches(Double minLinearScore, Double minTactileScore, 
                                      Double minSoundScore, String stemMaterial) {
        if (minLinearScore != null && minTactileScore != null && 
            minSoundScore != null && stemMaterial != null) {
            return switchRepository.findByLinearScoreGreaterThanEqualAndTactileScoreGreaterThanEqualAndSoundScoreGreaterThanEqualAndStemMaterialContainingIgnoreCase(
                minLinearScore, minTactileScore, minSoundScore, stemMaterial);
        } else if (minLinearScore != null && minTactileScore != null && minSoundScore != null) {
            return switchRepository.findByLinearScoreGreaterThanEqualAndTactileScoreGreaterThanEqualAndSoundScoreGreaterThanEqual(
                minLinearScore, minTactileScore, minSoundScore);
        } else if (stemMaterial != null) {
            return switchRepository.findByStemMaterialContainingIgnoreCase(stemMaterial);
        } else {
            return switchRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = switchRepository.count();
        
        // 기본 통계
        stats.put("totalSwitches", totalCount);
        
        // 평균 점수들
        Double avgLinearScore = switchRepository.findAverageLinearScore();
        Double avgTactileScore = switchRepository.findAverageTactileScore();
        Double avgSoundScore = switchRepository.findAverageSoundScore();
        
        stats.put("averageScores", Map.of(
            "linear", avgLinearScore != null ? Math.round(avgLinearScore * 100.0) / 100.0 : 0.0,
            "tactile", avgTactileScore != null ? Math.round(avgTactileScore * 100.0) / 100.0 : 0.0,
            "sound", avgSoundScore != null ? Math.round(avgSoundScore * 100.0) / 100.0 : 0.0
        ));
        
        // 최고 점수 스위치들
        List<Switch> topLinearSwitches = switchRepository.findTop3ByOrderByLinearScoreDesc();
        List<Switch> topTactileSwitches = switchRepository.findTop3ByOrderByTactileScoreDesc();
        List<Switch> topSoundSwitches = switchRepository.findTop3ByOrderBySoundScoreDesc();
        
        stats.put("topSwitches", Map.of(
            "linear", topLinearSwitches,
            "tactile", topTactileSwitches,
            "sound", topSoundSwitches
        ));
        
        // 스템 재질별 분포
        List<Object[]> stemMaterialDistribution = switchRepository.findStemMaterialDistribution();
        Map<String, Long> stemMaterialStats = new HashMap<>();
        for (Object[] row : stemMaterialDistribution) {
            stemMaterialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("stemMaterialDistribution", stemMaterialStats);
        
        return stats;
    }
} 