package com.example.klue_sever.service;

import com.example.klue_sever.entity.Foam;
import com.example.klue_sever.repository.FoamRepository;
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
public class FoamService {

    private final FoamRepository foamRepository;

    @Autowired
    public FoamService(FoamRepository foamRepository) {
        this.foamRepository = foamRepository;
    }

    public Foam save(Foam foam) {
        return foamRepository.save(foam);
    }

    public List<Foam> findAll() {
        return foamRepository.findAll();
    }

    public Page<Foam> findAll(Pageable pageable) {
        return foamRepository.findAll(pageable);
    }

    public Optional<Foam> findById(Integer id) {
        return foamRepository.findById(id);
    }

    public void delete(Integer id) {
        foamRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return foamRepository.existsById(id);
    }

    public long count() {
        return foamRepository.count();
    }

    public Foam update(Integer id, Foam foamDetails) {
        if (foamRepository.existsById(id)) {
            foamDetails.setId(id);
            return foamRepository.save(foamDetails);
        }
        return null;
    }

    public Page<Foam> searchByKeyword(String keyword, Pageable pageable) {
        return foamRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCase(
            keyword, keyword, pageable);
    }

    public List<Foam> filterFoams(String material) {
        if (material != null) {
            return foamRepository.findByMaterialContainingIgnoreCase(material);
        } else {
            return foamRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = foamRepository.count();
        
        // 기본 통계
        stats.put("totalFoams", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = foamRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        return stats;
    }
} 