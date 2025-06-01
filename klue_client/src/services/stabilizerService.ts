import { api, ApiResponse, PaginationParams } from "./api";
import { Stabilizer } from "../types/entities";

// Stabilizer 목록 응답 타입
export interface StabilizerListResponse extends ApiResponse<Stabilizer[]> {
  stabilizers: Stabilizer[];
}

// Stabilizer 단일 응답 타입
export interface StabilizerResponse extends ApiResponse<Stabilizer> {
  stabilizer: Stabilizer;
}

// 통계 응답 타입
export interface StabilizerStatsResponse extends ApiResponse<any> {
  statistics: {
    totalStabilizers: number;
    materialDistribution: Record<string, number>;
    sizeDistribution: Record<string, number>;
  };
}

export const stabilizerService = {
  // 모든 스테빌라이저 조회
  getAll: async (
    params: PaginationParams = {}
  ): Promise<StabilizerListResponse> => {
    const response = await api.get("/stabilizers", { params });
    return response.data;
  },

  // ID로 스테빌라이저 조회
  getById: async (id: number): Promise<StabilizerResponse> => {
    const response = await api.get(`/stabilizers/${id}`);
    return response.data;
  },

  // 스테빌라이저 생성
  create: async (
    stabilizer: Omit<Stabilizer, "id">
  ): Promise<StabilizerResponse> => {
    const response = await api.post("/stabilizers", stabilizer);
    return response.data;
  },

  // 스테빌라이저 수정
  update: async (
    id: number,
    stabilizer: Partial<Stabilizer>
  ): Promise<StabilizerResponse> => {
    const response = await api.put(`/stabilizers/${id}`, stabilizer);
    return response.data;
  },

  // 스테빌라이저 삭제
  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete(`/stabilizers/${id}`);
    return response.data;
  },

  // 스테빌라이저 검색
  search: async (
    keyword: string,
    params: PaginationParams = {}
  ): Promise<StabilizerListResponse> => {
    const response = await api.get("/stabilizers/search", {
      params: { keyword, ...params },
    });
    return response.data;
  },

  // 스테빌라이저 필터링
  filter: async (
    material?: string,
    size?: string
  ): Promise<StabilizerListResponse> => {
    const response = await api.get("/stabilizers/filter", {
      params: { material, size },
    });
    return response.data;
  },

  // 스테빌라이저 통계
  getStats: async (): Promise<StabilizerStatsResponse> => {
    const response = await api.get("/stabilizers/stats");
    return response.data;
  },
};
