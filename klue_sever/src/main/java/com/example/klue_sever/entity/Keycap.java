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
@Table(name = "Keycap")
public class Keycap {
    
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

    @Column(length = 50)
    private String thickness;

    @Column(length = 100)
    private String profile;

    @Column(name = "component_id")
    private Integer componentId;

    @Column(length = 100, name = "switch_compatibility")
    private String switchCompatibility;

    @Column(name = "sound_profile")
    private Float soundProfile;

    @Column(name = "build_quality")
    private Float buildQuality;

    @Column(name = "price_tier")
    private Float priceTier;

    @Column(name = "rgb_compatible")
    private Boolean rgbCompatible;

    private Float durability;
} 