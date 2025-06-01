// 공통 필드
export interface BaseEntity {
  id: number;
  startdate: string;
  enddate: string;
  name: string;
  link?: string;
  componentId?: number;
}

// Switch 엔티티
export interface Switch extends BaseEntity {
  type: string;
  pressure?: number;
  lubrication?: string;
  stemMaterial: string;
  linearScore: number;
  tactileScore: number;
  soundScore: number;
  weightScore: number;
  smoothnessScore: number;
}

// Keycap 엔티티
export interface Keycap extends BaseEntity {
  profile: string;
  material: string;
  printingMethod: string;
  language: string;
  includedKeysCount: number;
}

// PCB 엔티티
export interface PCB extends BaseEntity {
  size: string;
  layout: string;
  switchSupport: string;
  connectivityOptions: string;
  rgbLighting: string;
  rotaryEncoderSupport: string;
}

// Plate 엔티티
export interface Plate extends BaseEntity {
  material: string;
  size: string;
  mountingStyle: string;
  switchSupport: string;
  flexibility: string;
}

// Stabilizer 엔티티
export interface Stabilizer extends BaseEntity {
  material?: string;
  size?: string;
}

// Gasket 엔티티
export interface Gasket extends BaseEntity {
  material: string;
  typing?: string;
}

// Foam 엔티티
export interface Foam extends BaseEntity {
  material: string;
}

// Cable 엔티티
export interface Cable extends BaseEntity {
  material: string;
  length?: number;
}

// SoundDampener 엔티티
export interface SoundDampener extends BaseEntity {
  material: string;
  size?: string;
}

// HardwareConnector 엔티티
export interface HardwareConnector extends BaseEntity {
  material: string;
  size?: string;
}
