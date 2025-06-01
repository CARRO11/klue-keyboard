package com.example.klue_sever.controller;

import com.example.klue_sever.entity.KeyboardCase;
import com.example.klue_sever.service.KeyboardCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class KeyboardCaseController {

    private final KeyboardCaseService keyboardCaseService;

    @Autowired
    public KeyboardCaseController(KeyboardCaseService keyboardCaseService) {
        this.keyboardCaseService = keyboardCaseService;
    }

    @PostMapping
    public ResponseEntity<KeyboardCase> createCase(@RequestBody KeyboardCase keyboardCase) {
        KeyboardCase savedCase = keyboardCaseService.save(keyboardCase);
        return ResponseEntity.ok(savedCase);
    }

    @GetMapping
    public ResponseEntity<List<KeyboardCase>> getAllCases() {
        List<KeyboardCase> cases = keyboardCaseService.findAll();
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardCase> getCaseById(@PathVariable Integer id) {
        return keyboardCaseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<KeyboardCase> getCaseWithDetails(@PathVariable Integer id) {
        return keyboardCaseService.findByIdWithDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyboardCase> updateCase(@PathVariable Integer id, @RequestBody KeyboardCase keyboardCase) {
        KeyboardCase updatedCase = keyboardCaseService.update(id, keyboardCase);
        if (updatedCase != null) {
            return ResponseEntity.ok(updatedCase);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Integer id) {
        keyboardCaseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 