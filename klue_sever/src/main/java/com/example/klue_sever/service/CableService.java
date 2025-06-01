package com.example.klue_sever.service;

import com.example.klue_sever.entity.Cable;
import com.example.klue_sever.repository.CableRepository;
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
public class CableService {

    private final CableRepository cableRepository;

    @Autowired
    public CableService(CableRepository cableRepository) {
        this.cableRepository = cableRepository;
    }

    public Cable save(Cable cable) {
        return cableRepository.save(cable);
    }

    public List<Cable> findAll() {
        return cableRepository.findAll();
    }

    public Page<Cable> findAll(Pageable pageable) {
        return cableRepository.findAll(pageable);
    }

    public Optional<Cable> findById(Integer id) {
        return cableRepository.findById(id);
    }

    public void delete(Integer id) {
        cableRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return cableRepository.existsById(id);
    }

    public long count() {
        return cableRepository.count();
    }

    public Cable update(Integer id, Cable cableDetails) {
        if (cableRepository.existsById(id)) {
            cableDetails.setId(id);
            return cableRepository.save(cableDetails);
        }
        return null;
    }

    public Page<Cable> searchByKeyword(String keyword, Pageable pageable) {
        return cableRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCase(
            keyword, keyword, pageable);
    }

    public List<Cable> filterCables(String material, Integer length) {
        if (material != null && length != null) {
            return cableRepository.findByMaterialContainingIgnoreCaseAndLength(material, length);
        } else if (material != null) {
            return cableRepository.findByMaterialContainingIgnoreCase(material);
        } else if (length != null) {
            return cableRepository.findByLength(length);
        } else {
            return cableRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = cableRepository.count();
        
        // 기본 통계
        stats.put("totalCables", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = cableRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 길이별 분포
        List<Object[]> lengthDistribution = cableRepository.findLengthDistribution();
        Map<String, Long> lengthStats = new HashMap<>();
        for (Object[] row : lengthDistribution) {
            lengthStats.put(String.valueOf(row[0]), (Long) row[1]);
        }
        stats.put("lengthDistribution", lengthStats);
        
        return stats;
    }
} 