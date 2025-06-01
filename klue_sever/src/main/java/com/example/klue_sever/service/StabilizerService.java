package com.example.klue_sever.service;

import com.example.klue_sever.entity.Stabilizer;
import com.example.klue_sever.repository.StabilizerRepository;
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
public class StabilizerService {

    private final StabilizerRepository stabilizerRepository;

    @Autowired
    public StabilizerService(StabilizerRepository stabilizerRepository) {
        this.stabilizerRepository = stabilizerRepository;
    }

    public Stabilizer save(Stabilizer stabilizer) {
        return stabilizerRepository.save(stabilizer);
    }

    public List<Stabilizer> findAll() {
        return stabilizerRepository.findAll();
    }

    public Page<Stabilizer> findAll(Pageable pageable) {
        return stabilizerRepository.findAll(pageable);
    }

    public Optional<Stabilizer> findById(Integer id) {
        return stabilizerRepository.findById(id);
    }

    public void delete(Integer id) {
        stabilizerRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return stabilizerRepository.existsById(id);
    }

    public long count() {
        return stabilizerRepository.count();
    }

    public Stabilizer update(Integer id, Stabilizer stabilizerDetails) {
        if (stabilizerRepository.existsById(id)) {
            stabilizerDetails.setId(id);
            return stabilizerRepository.save(stabilizerDetails);
        }
        return null;
    }

    public Page<Stabilizer> searchByKeyword(String keyword, Pageable pageable) {
        return stabilizerRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
    }

    public List<Stabilizer> filterStabilizers(String material, String size) {
        if (material != null && size != null) {
            return stabilizerRepository.findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(material, size);
        } else if (material != null) {
            return stabilizerRepository.findByMaterialContainingIgnoreCase(material);
        } else if (size != null) {
            return stabilizerRepository.findBySizeContainingIgnoreCase(size);
        } else {
            return stabilizerRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = stabilizerRepository.count();
        
        // 기본 통계
        stats.put("totalStabilizers", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = stabilizerRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 크기별 분포
        List<Object[]> sizeDistribution = stabilizerRepository.findSizeDistribution();
        Map<String, Long> sizeStats = new HashMap<>();
        for (Object[] row : sizeDistribution) {
            sizeStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("sizeDistribution", sizeStats);
        
        return stats;
    }
} 