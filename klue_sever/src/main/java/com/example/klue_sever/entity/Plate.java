package com.example.klue_sever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plate")
public class Plate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String material;

    private Integer size;

    @Column(name = "Compatibility")
    private String compatibility;

    @Column(name = "component_id")
    private Integer componentId;

    private Float thickness;

    private String layout;

    @Column(name = "typing_feel")
    private Float typingFeel;

    @Column(name = "flex_level")
    private Float flexLevel;

    @Column(name = "sound_profile")
    private Float soundProfile;

    @Column(name = "price_tier")
    private Float priceTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyboard_case_id")
    @JsonIgnore
    private KeyboardCase keyboardCase;
} 