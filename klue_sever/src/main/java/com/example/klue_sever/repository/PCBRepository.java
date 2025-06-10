package com.example.klue_sever.repository;

import com.example.klue_sever.entity.PCB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCBRepository extends JpaRepository<PCB, Integer> {
} 