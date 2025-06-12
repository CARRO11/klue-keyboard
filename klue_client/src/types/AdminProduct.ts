// 제품 상태 열거형
export enum ProductStatus {
  DRAFT = "DRAFT",
  PUBLISHED = "PUBLISHED",
  HIDDEN = "HIDDEN",
  DISCONTINUED = "DISCONTINUED",
}

// 제품 상태 설명 매핑
export const ProductStatusDescription: Record<ProductStatus, string> = {
  [ProductStatus.DRAFT]: "임시저장",
  [ProductStatus.PUBLISHED]: "게시됨",
  [ProductStatus.HIDDEN]: "숨김",
  [ProductStatus.DISCONTINUED]: "단종",
};

// 관리자 제품 생성/수정 요청 인터페이스
export interface AdminProductRequest {
  productName: string;
  description: string;
  brand: string;
  category?: string;
  price?: number;
  imageUrl?: string;
  productUrl?: string;
  status: ProductStatus;
  adminUser: string;
  priorityOrder?: number;
  isFeatured?: boolean;
}

// 관리자 제품 상세 응답 인터페이스
export interface AdminProductResponse {
  id: number;
  productName: string;
  description: string;
  brand: string;
  category?: string;
  price?: number;
  imageUrl?: string;
  productUrl?: string;
  status: ProductStatus;
  statusDescription: string;
  adminUser: string;
  priorityOrder: number;
  isFeatured: boolean;
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
}

// 관리자 제품 목록용 요약 인터페이스
export interface AdminProductSummary {
  id: number;
  productName: string;
  brand: string;
  category?: string;
  price?: number;
  imageUrl?: string;
  status: ProductStatus;
  statusDescription: string;
  adminUser: string;
  priorityOrder: number;
  isFeatured: boolean;
  createdAt: string;
  publishedAt?: string;
  preview: string; // 설명 미리보기
}

// 공개용 제품 인터페이스 (일반 사용자용)
export interface PublicProduct {
  id: number;
  productName: string;
  description: string;
  brand: string;
  category?: string;
  price?: number;
  imageUrl?: string;
  productUrl?: string;
  isFeatured: boolean;
  publishedAt: string;
}

// 페이지네이션 응답 인터페이스
export interface PageResponse<T> {
  content: T[];
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

// API 응답 메시지 인터페이스
export interface ApiResponse {
  message: string;
  productId?: string;
  deletedId?: string;
  priority?: string;
}

// 상태별 통계 인터페이스
export interface StatusStatistics {
  [key: string]: number;
}

// 검색 및 필터 파라미터 인터페이스
export interface ProductSearchParams {
  page?: number;
  size?: number;
  keyword?: string;
  category?: string;
  brand?: string;
  status?: ProductStatus;
  adminUser?: string;
}

// 제품 폼 유효성 검사 인터페이스
export interface ProductFormErrors {
  productName?: string;
  description?: string;
  brand?: string;
  category?: string;
  price?: string;
  imageUrl?: string;
  productUrl?: string;
  adminUser?: string;
}

// 제품 폼 상태 인터페이스
export interface ProductFormState {
  values: AdminProductRequest;
  errors: ProductFormErrors;
  isLoading: boolean;
  isSubmitting: boolean;
}

// 제품 목록 필터 상태 인터페이스
export interface ProductListFilter {
  searchKeyword: string;
  selectedCategory: string;
  selectedBrand: string;
  selectedStatus: ProductStatus | string;
  selectedAdmin: string;
  sortBy: "newest" | "oldest" | "priority" | "name";
}

// 카테고리 옵션 인터페이스
export interface CategoryOption {
  value: string;
  label: string;
  count?: number;
}

// 브랜드 옵션 인터페이스
export interface BrandOption {
  value: string;
  label: string;
  count?: number;
}

// 제품 액션 타입
export type ProductAction =
  | "create"
  | "edit"
  | "delete"
  | "publish"
  | "hide"
  | "feature"
  | "unfeature"
  | "priority";

// 제품 목록 모드
export type ProductListMode = "admin" | "public";

// 제품 뷰 모드
export type ProductViewMode = "grid" | "list" | "table";
