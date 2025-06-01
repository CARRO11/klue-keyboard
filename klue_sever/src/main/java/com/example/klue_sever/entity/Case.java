package com.example.klue_sever.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "case")
@Getter
@Setter
@NoArgsConstructor
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "case", cascade = CascadeType.ALL)
    private List<Plate> plates;

    @OneToMany(mappedBy = "case", cascade = CascadeType.ALL)
    private List<PCB> pcbs;
} 