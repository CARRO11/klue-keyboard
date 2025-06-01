package com.example.klue_sever.service;

import com.example.klue_sever.entity.Gasket;
import com.example.klue_sever.repository.GasketRepository;
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
public class GasketService {

    private final GasketRepository gasketRepository;

    @Autowired
    public GasketService(GasketRepository gasketRepository) {
        this.gasketRepository = gasketRepository;
    }

    public Gasket save(Gasket gasket) {
        return gasketRepository.save(gasket);
    }

    public List<Gasket> findAll() {
        return gasketRepository.findAll();
    }

    public Page<Gasket> findAll(Pageable pageable) {
        return gasketRepository.findAll(pageable);
    }

    public Optional<Gasket> findById(Integer id) {
        return gasketRepository.findById(id);
    }

    public void delete(Integer id) {
        gasketRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return gasketRepository.existsById(id);
    }

    public long count() {
        return gasketRepository.count();
    }

    public Gasket update(Integer id, Gasket gasketDetails) {
        if (gasketRepository.existsById(id)) {
            gasketDetails.setId(id);
            return gasketRepository.save(gasketDetails);
        }
        return null;
    }

    public Page<Gasket> searchByKeyword(String keyword, Pageable pageable) {
        return gasketRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrTypingContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
    }

    public List<Gasket> filterGaskets(String material, String typing) {
        if (material != null && typing != null) {
            return gasketRepository.findByMaterialContainingIgnoreCaseAndTypingContainingIgnoreCase(material, typing);
        } else if (material != null) {
            return gasketRepository.findByMaterialContainingIgnoreCase(material);
        } else if (typing != null) {
            return gasketRepository.findByTypingContainingIgnoreCase(typing);
        } else {
            return gasketRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = gasketRepository.count();
        
        // 기본 통계
        stats.put("totalGaskets", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = gasketRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 타입별 분포
        List<Object[]> typingDistribution = gasketRepository.findTypingDistribution();
        Map<String, Long> typingStats = new HashMap<>();
        for (Object[] row : typingDistribution) {
            typingStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("typingDistribution", typingStats);
        
        return stats;
    }
} 