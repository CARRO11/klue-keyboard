package com.example.klue_sever.service;

import com.example.klue_sever.entity.Stabilizer;
import com.example.klue_sever.repository.StabilizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
} 