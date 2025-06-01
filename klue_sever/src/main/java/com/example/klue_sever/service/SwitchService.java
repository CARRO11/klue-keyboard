package com.example.klue_sever.service;

import com.example.klue_sever.entity.Switch;
import com.example.klue_sever.repository.SwitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SwitchService {

    private final SwitchRepository switchRepository;

    @Autowired
    public SwitchService(SwitchRepository switchRepository) {
        this.switchRepository = switchRepository;
    }

    public Switch save(Switch switch_) {
        return switchRepository.save(switch_);
    }

    public List<Switch> findAll() {
        return switchRepository.findAll();
    }

    public Optional<Switch> findById(Integer id) {
        return switchRepository.findById(id);
    }

    public void delete(Integer id) {
        switchRepository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return switchRepository.existsById(id);
    }

    public long count() {
        return switchRepository.count();
    }
} 