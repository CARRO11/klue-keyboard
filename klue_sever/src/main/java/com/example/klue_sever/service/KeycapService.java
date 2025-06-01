package com.example.klue_sever.service;

import com.example.klue_sever.entity.Keycap;
import com.example.klue_sever.repository.KeycapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
} 