import {
  Switch,
  Keycap,
  PCB,
  Plate,
  Stabilizer,
  Gasket,
  Foam,
  Cable,
  SoundDampener,
  HardwareConnector,
} from "./entities";

// 키보드 빌드 구성 요소
export interface KeyboardBuild {
  id?: string;
  name: string;
  description?: string;
  totalPrice: number;
  compatibility: CompatibilityStatus;
  components: BuildComponents;
  createdAt?: string;
  updatedAt?: string;
  isPublic: boolean;
  author?: string;
  tags?: string[];
  rating?: number;
  views?: number;
  likes?: number;
}

// 빌드 구성 요소들
export interface BuildComponents {
  switch?: Switch;
  keycap?: Keycap;
  pcb?: PCB;
  plate?: Plate;
  stabilizer?: Stabilizer;
  gasket?: Gasket;
  foam?: Foam;
  cable?: Cable;
  soundDampener?: SoundDampener;
  hardwareConnector?: HardwareConnector;
  case?: KeyboardCase; // 추가 케이스 타입
}

// 키보드 케이스 (entities.ts에 없어서 추가)
export interface KeyboardCase {
  id: number;
  name: string;
  material: string;
  size: string;
  mountingStyle: string;
  color: string;
  brand: string;
  price: number;
  weight?: number;
  dimensions?: {
    length: number;
    width: number;
    height: number;
  };
  compatibility: string[];
}

// 호환성 상태
export interface CompatibilityStatus {
  isCompatible: boolean;
  warnings: CompatibilityWarning[];
  errors: CompatibilityError[];
  suggestions: CompatibilitySuggestion[];
}

export interface CompatibilityWarning {
  component: ComponentType;
  message: string;
  severity: "low" | "medium" | "high";
}

export interface CompatibilityError {
  component: ComponentType;
  message: string;
  blocking: boolean;
}

export interface CompatibilitySuggestion {
  component: ComponentType;
  message: string;
  suggestedItems?: any[];
}

// 컴포넌트 타입
export type ComponentType =
  | "switch"
  | "keycap"
  | "pcb"
  | "plate"
  | "stabilizer"
  | "gasket"
  | "foam"
  | "cable"
  | "soundDampener"
  | "hardwareConnector"
  | "case";

// 빌드 단계
export enum BuildStep {
  PCB = "pcb",
  CASE = "case",
  PLATE = "plate",
  SWITCH = "switch",
  STABILIZER = "stabilizer",
  KEYCAP = "keycap",
  ACCESSORIES = "accessories",
  REVIEW = "review",
}

// 컴포넌트 선택 상태
export interface ComponentSelectionState {
  selectedComponent: ComponentType | null;
  availableComponents: Record<ComponentType, any[]>;
  filteredComponents: Record<ComponentType, any[]>;
  searchQuery: string;
  filters: ComponentFilters;
  sortBy: SortOption;
}

// 필터 옵션
export interface ComponentFilters {
  priceRange: {
    min: number;
    max: number;
  };
  brands: string[];
  inStock: boolean;
  rating: number;
  [key: string]: any; // 컴포넌트별 특수 필터
}

// 정렬 옵션
export type SortOption =
  | "name"
  | "price-low"
  | "price-high"
  | "rating"
  | "popularity"
  | "newest";

// 빌드 프리셋
export interface BuildPreset {
  id: string;
  name: string;
  description: string;
  category: BuildCategory;
  components: BuildComponents;
  totalPrice: number;
  difficulty: "beginner" | "intermediate" | "advanced";
  estimatedTime: number; // 조립 예상 시간 (분)
  tags: string[];
  author: string;
  rating: number;
  reviews: number;
  imageUrl?: string;
}

// 빌드 카테고리
export enum BuildCategory {
  GAMING = "gaming",
  OFFICE = "office",
  PROGRAMMING = "programming",
  CUSTOM = "custom",
  BUDGET = "budget",
  PREMIUM = "premium",
  SILENT = "silent",
  TACTILE = "tactile",
  LINEAR = "linear",
}

// 빌드 프로세스 상태
export interface BuildProcessState {
  currentStep: BuildStep;
  completedSteps: BuildStep[];
  build: KeyboardBuild;
  selectionState: ComponentSelectionState;
  isLoading: boolean;
  error: string | null;
  isDirty: boolean; // 변경사항 있음
}

// 컴포넌트 비교
export interface ComponentComparison {
  items: any[];
  criteria: ComparisonCriteria[];
  scores: Record<string, number>;
}

export interface ComparisonCriteria {
  name: string;
  weight: number;
  getValue: (item: any) => number;
  format?: (value: number) => string;
}

// 빌드 통계
export interface BuildStats {
  totalComponents: number;
  totalPrice: number;
  estimatedWeight: number;
  typingExperience: {
    tactility: number;
    sound: number;
    smoothness: number;
  };
  overallRating: number;
}

// 빌드 공유
export interface SharedBuild {
  id: string;
  name: string;
  author: string;
  description: string;
  components: BuildComponents;
  stats: BuildStats;
  createdAt: string;
  likes: number;
  views: number;
  comments: BuildComment[];
  tags: string[];
  isPublic: boolean;
}

export interface BuildComment {
  id: string;
  author: string;
  content: string;
  rating?: number;
  createdAt: string;
  replies?: BuildComment[];
}

// 빌드 저장 요청
export interface SaveBuildRequest {
  name: string;
  description?: string;
  components: BuildComponents;
  isPublic: boolean;
  tags?: string[];
}

// API 응답
export interface BuildResponse {
  success: boolean;
  data?: KeyboardBuild;
  error?: string;
}

export interface BuildListResponse {
  success: boolean;
  data?: {
    builds: KeyboardBuild[];
    total: number;
    page: number;
    limit: number;
  };
  error?: string;
}
