package com.example.klue_sever.repository;

import com.example.klue_sever.entity.KeyboardCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KeyboardCaseRepository extends JpaRepository<KeyboardCase, Integer> {
    @Query("SELECT k FROM KeyboardCase k LEFT JOIN FETCH k.plates LEFT JOIN FETCH k.pcbs WHERE k.id = :id")
    Optional<KeyboardCase> findByIdWithDetails(Integer id);
} 