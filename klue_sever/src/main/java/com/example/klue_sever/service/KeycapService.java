package com.example.klue_sever.service;

import com.example.klue_sever.entity.Keycap;
import com.example.klue_sever.repository.KeycapRepository;
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
public class KeycapService {

    private final KeycapRepository keycapRepository;

    @Autowired
    public KeycapService(KeycapRepository keycapRepository) {
        this.keycapRepository = keycapRepository;
    }

    public Keycap save(Keycap keycap) {
        return keycapRepository.save(keycap);
    }

    public List<Keycap> findAll() {
        return keycapRepository.findAll();
    }

    public Page<Keycap> findAll(Pageable pageable) {
        return keycapRepository.findAll(pageable);
    }

    public Optional<Keycap> findById(Integer id) {
        return keycapRepository.findById(id);
    }

    public void delete(Integer id) {
        keycapRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return keycapRepository.existsById(id);
    }

    public long count() {
        return keycapRepository.count();
    }

    public Keycap update(Integer id, Keycap keycapDetails) {
        if (keycapRepository.existsById(id)) {
            keycapDetails.setId(id);
            return keycapRepository.save(keycapDetails);
        }
        return null;
    }

    public Page<Keycap> searchByKeyword(String keyword, Pageable pageable) {
        return keycapRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrProfileContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
    }

    public List<Keycap> filterKeycaps(String material, String profile) {
        if (material != null && profile != null) {
            return keycapRepository.findByMaterialContainingIgnoreCaseAndProfileContainingIgnoreCase(material, profile);
        } else if (material != null) {
            return keycapRepository.findByMaterialContainingIgnoreCase(material);
        } else if (profile != null) {
            return keycapRepository.findByProfileContainingIgnoreCase(profile);
        } else {
            return keycapRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = keycapRepository.count();
        
        // 기본 통계
        stats.put("totalKeycaps", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = keycapRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 프로필별 분포
        List<Object[]> profileDistribution = keycapRepository.findProfileDistribution();
        Map<String, Long> profileStats = new HashMap<>();
        for (Object[] row : profileDistribution) {
            profileStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("profileDistribution", profileStats);
        
        return stats;
    }
} 