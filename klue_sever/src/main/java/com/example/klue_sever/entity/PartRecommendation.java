package com.example.klue_sever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "part_recommendation")
public class PartRecommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String purpose; // 사용 목적 (게이밍, 타이핑, 프로그래밍 등)

    @Column(nullable = false)
    private Double budget; // 예산

    @Column(nullable = false)
    private String preferredSound; // 선호하는 타건음 (저소음, 경쾌한, 묵직한 등)

    @Column(nullable = false)
    private String switchType; // 스위치 타입 (리니어, 택타일, 클릭)

    @Column(nullable = false)
    private Boolean backlightRequired; // 백라이트 필요 여부

    @Column(nullable = false)
    private Boolean lubeRequired; // 윤활 필요 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyboard_case_id")
    private KeyboardCase recommendedCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plate_id")
    private Plate recommendedPlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pcb_id")
    private PCB recommendedPCB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "switch_id")
    private Switch recommendedSwitch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keycap_id")
    private Keycap recommendedKeycap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stabilizer_id")
    private Stabilizer recommendedStabilizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cable_id")
    private Cable recommendedCable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foam_id")
    private Foam recommendedFoam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasket_id")
    private Gasket recommendedGasket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sound_dampener_id")
    private SoundDampener recommendedSoundDampener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_connector_id")
    private HardwareConnector recommendedHardwareConnector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_id")
    private Weight recommendedWeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lube_id")
    private Lube recommendedLube;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "extra_id")
    private List<Extra> recommendedExtras;

    private Double recommendationScore; // 추천 점수 (파이썬 AI 모델에서 계산)
    
    @Column(length = 1000)
    private String recommendationReason; // 추천 이유 (파이썬 AI 모델에서 생성)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 추천을 받은 사용자

    @Column(name = "ai_model_version")
    private String aiModelVersion; // 사용된 AI 모델 버전

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt; // 추천 생성 시간

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }
} 