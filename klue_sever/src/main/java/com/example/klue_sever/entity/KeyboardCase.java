package com.example.klue_sever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KeyboardCase")
public class KeyboardCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "keyboardCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Plate> plates = new ArrayList<>();

    @OneToMany(mappedBy = "keyboardCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PCB> pcbs = new ArrayList<>();
} 