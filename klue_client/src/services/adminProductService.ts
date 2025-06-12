import {
  AdminProductRequest,
  AdminProductResponse,
  AdminProductSummary,
  PublicProduct,
  PageResponse,
  ApiResponse,
  StatusStatistics,
  ProductSearchParams,
  ProductStatus,
} from "../types/AdminProduct";

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";
const ADMIN_ENDPOINT = `${API_BASE_URL}/api/admin/products`;

// API 기본 설정
const createHeaders = (
  contentType: string = "application/json"
): HeadersInit => ({
  "Content-Type": contentType,
  // TODO: 실제 인증 토큰 추가
  // 'Authorization': `Bearer ${getAuthToken()}`
});

// 에러 처리 함수
const handleApiError = async (response: Response): Promise<never> => {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(
      errorData.message || `HTTP ${response.status}: ${response.statusText}`
    );
  }
  throw new Error("Unknown API error");
};

// URL 파라미터 생성 함수
const createSearchParams = (params: ProductSearchParams): string => {
  const searchParams = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      searchParams.append(key, value.toString());
    }
  });

  return searchParams.toString();
};

// ========== 관리자 전용 API ==========

export const adminProductService = {
  // 제품 목록 조회 (전체)
  async getAllProducts(
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}?${queryString}`
      : ADMIN_ENDPOINT;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 상태별 제품 조회
  async getProductsByStatus(
    status: ProductStatus,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/status/${status}?${queryString}`
      : `${ADMIN_ENDPOINT}/status/${status}`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 카테고리별 제품 조회
  async getProductsByCategory(
    category: string,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/category/${encodeURIComponent(category)}?${queryString}`
      : `${ADMIN_ENDPOINT}/category/${encodeURIComponent(category)}`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 브랜드별 제품 조회
  async getProductsByBrand(
    brand: string,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/brand/${encodeURIComponent(brand)}?${queryString}`
      : `${ADMIN_ENDPOINT}/brand/${encodeURIComponent(brand)}`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 검색
  async searchProducts(
    keyword: string,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const searchParams = { ...params, keyword };
    const queryString = createSearchParams(searchParams);

    const response = await fetch(`${ADMIN_ENDPOINT}/search?${queryString}`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 관리자별 제품 조회
  async getProductsByAdmin(
    adminUser: string,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<AdminProductSummary>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/admin/${encodeURIComponent(adminUser)}?${queryString}`
      : `${ADMIN_ENDPOINT}/admin/${encodeURIComponent(adminUser)}`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 상세 조회
  async getProductById(id: number): Promise<AdminProductResponse> {
    const response = await fetch(`${ADMIN_ENDPOINT}/${id}`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 생성
  async createProduct(
    productData: AdminProductRequest
  ): Promise<AdminProductResponse> {
    const response = await fetch(ADMIN_ENDPOINT, {
      method: "POST",
      headers: createHeaders(),
      body: JSON.stringify(productData),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 수정
  async updateProduct(
    id: number,
    productData: AdminProductRequest
  ): Promise<AdminProductResponse> {
    const response = await fetch(`${ADMIN_ENDPOINT}/${id}`, {
      method: "PUT",
      headers: createHeaders(),
      body: JSON.stringify(productData),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 삭제
  async deleteProduct(id: number): Promise<ApiResponse> {
    const response = await fetch(`${ADMIN_ENDPOINT}/${id}`, {
      method: "DELETE",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 게시
  async publishProduct(id: number): Promise<ApiResponse> {
    const response = await fetch(`${ADMIN_ENDPOINT}/${id}/publish`, {
      method: "PATCH",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 숨김
  async hideProduct(id: number): Promise<ApiResponse> {
    const response = await fetch(`${ADMIN_ENDPOINT}/${id}/hide`, {
      method: "PATCH",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 제품 우선순위 변경
  async updatePriority(id: number, priority: number): Promise<ApiResponse> {
    const response = await fetch(
      `${ADMIN_ENDPOINT}/${id}/priority?priority=${priority}`,
      {
        method: "PATCH",
        headers: createHeaders(),
      }
    );

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 상태별 통계
  async getStatusStatistics(): Promise<StatusStatistics> {
    const response = await fetch(`${ADMIN_ENDPOINT}/statistics`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 전체 카테고리 목록
  async getAllCategories(): Promise<string[]> {
    const response = await fetch(`${ADMIN_ENDPOINT}/categories`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 전체 브랜드 목록
  async getAllBrands(): Promise<string[]> {
    const response = await fetch(`${ADMIN_ENDPOINT}/brands`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },
};

// ========== 공개 API (일반 사용자용) ==========

export const publicProductService = {
  // 게시된 제품 목록 조회
  async getPublishedProducts(
    params: ProductSearchParams = {}
  ): Promise<PageResponse<PublicProduct>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/public?${queryString}`
      : `${ADMIN_ENDPOINT}/public`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 게시된 제품 중 카테고리별 조회
  async getPublishedProductsByCategory(
    category: string,
    params: ProductSearchParams = {}
  ): Promise<PageResponse<PublicProduct>> {
    const queryString = createSearchParams(params);
    const url = queryString
      ? `${ADMIN_ENDPOINT}/public/category/${encodeURIComponent(category)}?${queryString}`
      : `${ADMIN_ENDPOINT}/public/category/${encodeURIComponent(category)}`;

    const response = await fetch(url, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 특별 추천 제품 조회
  async getFeaturedProducts(limit: number = 5): Promise<PublicProduct[]> {
    const response = await fetch(
      `${ADMIN_ENDPOINT}/public/featured?limit=${limit}`,
      {
        method: "GET",
        headers: createHeaders(),
      }
    );

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 게시된 제품 상세 조회
  async getPublishedProductById(id: number): Promise<PublicProduct> {
    const response = await fetch(`${ADMIN_ENDPOINT}/public/${id}`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 게시된 카테고리 목록 조회
  async getPublishedCategories(): Promise<string[]> {
    const response = await fetch(`${ADMIN_ENDPOINT}/public/categories`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },

  // 게시된 브랜드 목록 조회
  async getPublishedBrands(): Promise<string[]> {
    const response = await fetch(`${ADMIN_ENDPOINT}/public/brands`, {
      method: "GET",
      headers: createHeaders(),
    });

    if (!response.ok) {
      await handleApiError(response);
    }

    return response.json();
  },
};

// 통합 서비스 객체 (편의를 위한)
export const productService = {
  admin: adminProductService,
  public: publicProductService,
};
