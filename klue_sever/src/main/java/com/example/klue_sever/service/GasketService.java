package com.example.klue_sever.service;

import com.example.klue_sever.entity.Gasket;
import com.example.klue_sever.repository.GasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
} 