package com.example.klue_sever.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String components;

    @Column
    private String name;

    @Column
    private String link;

    @Column
    private String explanization;

    @Column(name = "component_id")
    private Integer componentId;
} 