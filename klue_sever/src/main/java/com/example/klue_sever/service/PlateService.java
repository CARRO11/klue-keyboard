package com.example.klue_sever.service;

import com.example.klue_sever.entity.Plate;
import com.example.klue_sever.repository.PlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlateService {
    
    private final PlateRepository plateRepository;

    @Autowired
    public PlateService(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public Plate save(Plate plate) {
        return plateRepository.save(plate);
    }

    public List<Plate> findAll() {
        return plateRepository.findAll();
    }

    public Optional<Plate> findById(Integer id) {
        return plateRepository.findById(id);
    }

    public void deleteById(Integer id) {
        plateRepository.deleteById(id);
    }

    public Plate update(Integer id, Plate plate) {
        if (plateRepository.existsById(id)) {
            plate.setId(id);
            return plateRepository.save(plate);
        }
        return null;
    }
} 