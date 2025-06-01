import { api, ApiResponse, PaginationParams } from "./api";
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
} from "../types/entities";

// 부품 타입 정의
export type PartType =
  | "switches"
  | "keycaps"
  | "pcbs"
  | "plates"
  | "stabilizers"
  | "gaskets"
  | "foams"
  | "cables"
  | "sound-dampeners"
  | "hardware-connectors";

export type Part =
  | Switch
  | Keycap
  | PCB
  | Plate
  | Stabilizer
  | Gasket
  | Foam
  | Cable
  | SoundDampener
  | HardwareConnector;

// 통합 응답 타입
export interface PartsListResponse extends ApiResponse<Part[]> {
  parts: Part[];
  switches?: Switch[];
  keycaps?: Keycap[];
  pcbs?: PCB[];
  plates?: Plate[];
  stabilizers?: Stabilizer[];
  gaskets?: Gasket[];
  foams?: Foam[];
  cables?: Cable[];
  soundDampeners?: SoundDampener[];
  hardwareConnectors?: HardwareConnector[];
}

// 부품 타입 한국어 매핑
export const PART_TYPE_LABELS: Record<PartType, string> = {
  switches: "스위치",
  keycaps: "키캡",
  pcbs: "PCB",
  plates: "플레이트",
  stabilizers: "스테빌라이저",
  gaskets: "가스켓",
  foams: "폼",
  cables: "케이블",
  "sound-dampeners": "사운드 댐퍼",
  "hardware-connectors": "하드웨어 커넥터",
};

// 부품 타입별 API 엔드포인트 매핑
export const PART_TYPE_ENDPOINTS: Record<PartType, string> = {
  switches: "/switches",
  keycaps: "/keycaps",
  pcbs: "/pcbs",
  plates: "/plates",
  stabilizers: "/stabilizers",
  gaskets: "/gaskets",
  foams: "/foams",
  cables: "/cables",
  "sound-dampeners": "/sound-dampeners",
  "hardware-connectors": "/hardware-connectors",
};

// 부품 타입별 응답 필드 매핑
export const PART_RESPONSE_FIELDS: Record<PartType, string> = {
  switches: "switches",
  keycaps: "keycaps",
  pcbs: "pcbs",
  plates: "plates",
  stabilizers: "stabilizers",
  gaskets: "gaskets",
  foams: "foams",
  cables: "cables",
  "sound-dampeners": "soundDampeners",
  "hardware-connectors": "hardwareConnectors",
};

export const allPartsService = {
  // 모든 부품 조회
  getAll: async (
    partType: PartType,
    params: PaginationParams = {}
  ): Promise<PartsListResponse> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.get(endpoint, { params });
    return response.data;
  },

  // ID로 부품 조회
  getById: async (partType: PartType, id: number): Promise<any> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.get(`${endpoint}/${id}`);
    return response.data;
  },

  // 부품 생성
  create: async (partType: PartType, part: Omit<Part, "id">): Promise<any> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.post(endpoint, part);
    return response.data;
  },

  // 부품 수정
  update: async (
    partType: PartType,
    id: number,
    part: Partial<Part>
  ): Promise<any> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.put(`${endpoint}/${id}`, part);
    return response.data;
  },

  // 부품 삭제
  delete: async (
    partType: PartType,
    id: number
  ): Promise<ApiResponse<void>> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.delete(`${endpoint}/${id}`);
    return response.data;
  },

  // 부품 검색
  search: async (
    partType: PartType,
    keyword: string,
    params: PaginationParams = {}
  ): Promise<PartsListResponse> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.get(`${endpoint}/search`, {
      params: { keyword, ...params },
    });
    return response.data;
  },

  // 부품 필터링
  filter: async (
    partType: PartType,
    filters: Record<string, any>
  ): Promise<PartsListResponse> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.get(`${endpoint}/filter`, {
      params: filters,
    });
    return response.data;
  },

  // 부품 통계
  getStats: async (partType: PartType): Promise<any> => {
    const endpoint = PART_TYPE_ENDPOINTS[partType];
    const response = await api.get(`${endpoint}/stats`);
    return response.data;
  },

  // 모든 부품 타입 목록 가져오기
  getAllPartTypes: (): Array<{ type: PartType; label: string }> => {
    return Object.entries(PART_TYPE_LABELS).map(([type, label]) => ({
      type: type as PartType,
      label,
    }));
  },
};
