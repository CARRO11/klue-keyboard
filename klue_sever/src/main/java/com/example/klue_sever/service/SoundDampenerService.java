package com.example.klue_sever.service;

import com.example.klue_sever.entity.SoundDampener;
import com.example.klue_sever.repository.SoundDampenerRepository;
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
public class SoundDampenerService {

    private final SoundDampenerRepository soundDampenerRepository;

    @Autowired
    public SoundDampenerService(SoundDampenerRepository soundDampenerRepository) {
        this.soundDampenerRepository = soundDampenerRepository;
    }

    public SoundDampener save(SoundDampener soundDampener) {
        return soundDampenerRepository.save(soundDampener);
    }

    public List<SoundDampener> findAll() {
        return soundDampenerRepository.findAll();
    }

    public Page<SoundDampener> findAll(Pageable pageable) {
        return soundDampenerRepository.findAll(pageable);
    }

    public Optional<SoundDampener> findById(Integer id) {
        return soundDampenerRepository.findById(id);
    }

    public void delete(Integer id) {
        soundDampenerRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return soundDampenerRepository.existsById(id);
    }

    public long count() {
        return soundDampenerRepository.count();
    }

    public SoundDampener update(Integer id, SoundDampener dampenerDetails) {
        if (soundDampenerRepository.existsById(id)) {
            dampenerDetails.setId(id);
            return soundDampenerRepository.save(dampenerDetails);
        }
        return null;
    }

    public Page<SoundDampener> searchByKeyword(String keyword, Pageable pageable) {
        return soundDampenerRepository.findByNameContainingIgnoreCaseOrMaterialContainingIgnoreCaseOrSizeContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
    }

    public List<SoundDampener> filterSoundDampeners(String material, String size) {
        if (material != null && size != null) {
            return soundDampenerRepository.findByMaterialContainingIgnoreCaseAndSizeContainingIgnoreCase(material, size);
        } else if (material != null) {
            return soundDampenerRepository.findByMaterialContainingIgnoreCase(material);
        } else if (size != null) {
            return soundDampenerRepository.findBySizeContainingIgnoreCase(size);
        } else {
            return soundDampenerRepository.findAll();
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = soundDampenerRepository.count();
        
        // 기본 통계
        stats.put("totalSoundDampeners", totalCount);
        
        // 재질별 분포
        List<Object[]> materialDistribution = soundDampenerRepository.findMaterialDistribution();
        Map<String, Long> materialStats = new HashMap<>();
        for (Object[] row : materialDistribution) {
            materialStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("materialDistribution", materialStats);
        
        // 크기별 분포
        List<Object[]> sizeDistribution = soundDampenerRepository.findSizeDistribution();
        Map<String, Long> sizeStats = new HashMap<>();
        for (Object[] row : sizeDistribution) {
            sizeStats.put((String) row[0], (Long) row[1]);
        }
        stats.put("sizeDistribution", sizeStats);
        
        return stats;
    }
} 