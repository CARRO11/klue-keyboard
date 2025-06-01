package com.example.klue_sever.service;

import com.example.klue_sever.entity.KeyboardCase;
import com.example.klue_sever.repository.KeyboardCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KeyboardCaseService {
    
    private final KeyboardCaseRepository keyboardCaseRepository;

    @Autowired
    public KeyboardCaseService(KeyboardCaseRepository keyboardCaseRepository) {
        this.keyboardCaseRepository = keyboardCaseRepository;
    }

    public KeyboardCase save(KeyboardCase keyboardCase) {
        return keyboardCaseRepository.save(keyboardCase);
    }

    public List<KeyboardCase> findAll() {
        return keyboardCaseRepository.findAll();
    }

    public Optional<KeyboardCase> findById(Integer id) {
        return keyboardCaseRepository.findById(id);
    }

    public Optional<KeyboardCase> findByIdWithDetails(Integer id) {
        return keyboardCaseRepository.findByIdWithDetails(id);
    }

    public void deleteById(Integer id) {
        keyboardCaseRepository.deleteById(id);
    }

    public KeyboardCase update(Integer id, KeyboardCase keyboardCase) {
        if (keyboardCaseRepository.existsById(id)) {
            keyboardCase.setId(id);
            return keyboardCaseRepository.save(keyboardCase);
        }
        return null;
    }
} 