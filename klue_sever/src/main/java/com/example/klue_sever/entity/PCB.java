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
@Table(name = "pcb")
public class PCB {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "layout")
    private String layout;

    private Boolean hotswap;

    private Boolean wireless;

    private String rgb;

    @Column(name = "component_id")
    private Integer componentId;

    @Column(name = "case_Compatibility")
    private Integer caseCompatibility;

    @Column(name = "usb_type")
    private String usbType;

    @Column(name = "firmware_type")
    private String firmwareType;

    @Column(name = "rgb_support")
    private Boolean rgbSupport;

    @Column(name = "qmk_via")
    private Boolean qmkVia;

    private Float flex;

    @Column(name = "price_tier")
    private Float priceTier;

    @Column(name = "build_quality")
    private Float buildQuality;

    private String features;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyboard_case_id")
    @JsonIgnore
    private KeyboardCase keyboardCase;
} 