package com.example.klue_sever.controller;

import com.example.klue_sever.entity.Plate;
import com.example.klue_sever.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plates")
public class PlateController {

    private final PlateService plateService;

    @Autowired
    public PlateController(PlateService plateService) {
        this.plateService = plateService;
    }

    @PostMapping
    public ResponseEntity<Plate> createPlate(@RequestBody Plate plate) {
        Plate savedPlate = plateService.save(plate);
        return ResponseEntity.ok(savedPlate);
    }

    @GetMapping
    public ResponseEntity<List<Plate>> getAllPlates() {
        List<Plate> plates = plateService.findAll();
        return ResponseEntity.ok(plates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plate> getPlateById(@PathVariable Integer id) {
        return plateService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plate> updatePlate(@PathVariable Integer id, @RequestBody Plate plate) {
        Plate updatedPlate = plateService.update(id, plate);
        if (updatedPlate != null) {
            return ResponseEntity.ok(updatedPlate);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlate(@PathVariable Integer id) {
        plateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 