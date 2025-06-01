package com.example.klue_sever.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "extra")
@Getter
@Setter
@NoArgsConstructor
public class Extra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime startdate;

    @Column
    private LocalDateTime enddate;

    @Column
    private String name;

    @Column
    private String link;

    @Column
    private String explanization;

    @Column(name = "component_id")
    private Integer componentId;
} 