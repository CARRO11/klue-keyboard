package com.example.klue_sever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

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

    // 복잡한 관계들을 단순화 - ID로만 저장
    @Column(name = "keyboard_case_id")
    private Integer recommendedCaseId;

    @Column(name = "plate_id")
    private Integer recommendedPlateId;

    @Column(name = "pcb_id")
    private Integer recommendedPCBId;

    @Column(name = "switch_id")
    private Integer recommendedSwitchId;

    @Column(name = "keycap_id")
    private Integer recommendedKeycapId;

    @Column(name = "stabilizer_id")
    private Integer recommendedStabilizerId;

    @Column(name = "cable_id")
    private Integer recommendedCableId;

    @Column(name = "foam_id")
    private Integer recommendedFoamId;

    @Column(name = "gasket_id")
    private Integer recommendedGasketId;

    @Column(name = "sound_dampener_id")
    private Integer recommendedSoundDampenerId;

    @Column(name = "hardware_connector_id")
    private Integer recommendedHardwareConnectorId;

    @Column(name = "weight_id")
    private Integer recommendedWeightId;

    @Column(name = "lube_id")
    private Integer recommendedLubeId;

    private Double recommendationScore; // 추천 점수

    @Column(length = 1000)
    private String recommendationReason; // 추천 이유

    @Column(name = "user_id")
    private Integer userId; // 추천을 받은 사용자 ID

    @Column(name = "ai_model_version")
    private String aiModelVersion; // 사용된 AI 모델 버전

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt; // 추천 생성 시간

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }
} 