package com.example.klue_sever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cable")
public class Cable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime startdate;

    @Column(nullable = false)
    private LocalDateTime enddate;

    @Column(nullable = false)
    private String name;

    private String link;

    @Column(length = 100)
    private String material;

    private Integer length;

    @Column(name = "component_id")
    private Integer componentId;
} 