package com.example.klue_sever.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/")
    public String root() {
        return "KLUE Keyboard API is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
} 