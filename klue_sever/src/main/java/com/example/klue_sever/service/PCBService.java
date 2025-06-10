package com.example.klue_sever.service;

import com.example.klue_sever.entity.PCB;
import com.example.klue_sever.repository.PCBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PCBService {

    private final PCBRepository pcbRepository;

    @Autowired
    public PCBService(PCBRepository pcbRepository) {
        this.pcbRepository = pcbRepository;
    }

    public List<PCB> findAll() {
        return pcbRepository.findAll();
    }

    public Optional<PCB> findById(Integer id) {
        return pcbRepository.findById(id);
    }

    public PCB save(PCB pcb) {
        return pcbRepository.save(pcb);
    }

    public PCB update(Integer id, PCB pcb) {
        if (pcbRepository.existsById(id)) {
            pcb.setId(id);
            return pcbRepository.save(pcb);
        }
        return null;
    }

    public void deleteById(Integer id) {
        pcbRepository.deleteById(id);
    }
} 