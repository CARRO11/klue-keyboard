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
@Table(name = "lube")
public class Lube {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime enddate;

    // 자기 참조 제거 - 순환 참조 문제 해결
    // @OneToOne
    // @JoinColumn(name = "enddate_name_id")
    // private Lube enddateName;
} 