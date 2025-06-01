package com.example.klue_sever.service;

import com.example.klue_sever.entity.Foam;
import com.example.klue_sever.repository.FoamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
} 