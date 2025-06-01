import axios from "axios";

// API 기본 설정
const BASE_URL = process.env.REACT_APP_API_URL || "http://127.0.0.1:8080";

export const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 응답 인터셉터 (에러 처리)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error);
    return Promise.reject(error);
  }
);

// 공통 API 응답 타입
export interface ApiResponse<T> {
  message: string;
  currentPage?: number;
  totalItems?: number;
  totalPages?: number;
  pageSize?: number;
  isFirst?: boolean;
  isLast?: boolean;
}

// 페이지네이션 파라미터
export interface PaginationParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: "asc" | "desc";
}
