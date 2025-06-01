package com.example.klue_sever.service;

import com.example.klue_sever.entity.HardwareConnector;
import com.example.klue_sever.repository.HardwareConnectorRepository;
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
public class HardwareConnectorService {

    private final HardwareConnectorRepository hardwareConnectorRepository;

    @Autowired
    public HardwareConnectorService(HardwareConnectorRepository hardwareConnectorRepository) {
        this.hardwareConnectorRepository = hardwareConnectorRepository;
    }

    public HardwareConnector save(HardwareConnector hardwareConnector) {
        return hardwareConnectorRepository.save(hardwareConnector);
    }

    public List<HardwareConnector> findAll() {
        return hardwareConnectorRepository.findAll();
    }

    public Page<HardwareConnector> findAll(Pageable pageable) {
        return hardwareConnectorRepository.findAll(pageable);
    }

    public Optional<HardwareConnector> findById(Integer id) {
        return hardwareConnectorRepository.findById(id);
    }

    public void delete(Integer id) {
        hardwareConnectorRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return hardwareConnectorRepository.existsById(id);
    }

    public long count() {
        return hardwareConnectorRepository.count();
    }

    public HardwareConnector update(Integer id, HardwareConnector connectorDetails) {
        if (hardwareConnectorRepository.existsById(id)) {
            connectorDetails.setId(id);
            return hardwareConnectorRepository.save(connectorDetails);
        }
        return null;
    }

    public Page<HardwareConnector> searchByKeyword(String keyword, Pageable pageable) {
        return hardwareConnectorRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
    }

    public List<HardwareConnector> filterHardwareConnectors(String material, String size) {
        if (material != null && size != null) {
            return hardwareConnectorRepository.findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(material, size);
        } else if (material != null) {
            return hardwareConnectorRepository.findByMaterialContainingIgnoreCase(material);
        } else if (size != null) {
            return hardwareConnectorRepository.findBySizeContainingIgnoreCase(size);
        } else {
            return hardwareConnectorRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = hardwareConnectorRepository.count();
        
        // 기본 통계
        stats.put("totalHardwareConnectors", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = hardwareConnectorRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 크기별 분포
        List<Object[]> sizeDistribution = hardwareConnectorRepository.findSizeDistribution();
        Map<String, Long> sizeStats = new HashMap<>();
        for (Object[] row : sizeDistribution) {
            sizeStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("sizeDistribution", sizeStats);
        
        return stats;
    }
} 