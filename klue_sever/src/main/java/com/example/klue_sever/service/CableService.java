package com.example.klue_sever.service;

import com.example.klue_sever.entity.Cable;
import com.example.klue_sever.repository.CableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
} 