import React, { useState, useEffect, useCallback } from "react";
import {
  AdminProductSummary,
  ProductSearchParams,
  ProductStatus,
  ProductStatusDescription,
  PageResponse,
  ProductListFilter,
} from "../types/AdminProduct";
import { adminProductService } from "../services/adminProductService";

interface AdminProductListProps {
  onProductSelect?: (product: AdminProductSummary) => void;
  onProductEdit?: (productId: number) => void;
  onProductDelete?: (productId: number) => void;
  onProductPublish?: (productId: number) => void;
  onProductHide?: (productId: number) => void;
  onCreateProduct?: () => void;
}

const AdminProductList: React.FC<AdminProductListProps> = ({
  onProductSelect,
  onProductEdit,
  onProductDelete,
  onProductPublish,
  onProductHide,
  onCreateProduct,
}) => {
  // 상태 관리
  const [products, setProducts] = useState<AdminProductSummary[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // 필터 및 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(10);
  const [filter, setFilter] = useState<ProductListFilter>({
    searchKeyword: "",
    selectedCategory: "",
    selectedBrand: "",
    selectedStatus: "",
    selectedAdmin: "",
    sortBy: "newest",
  });

  // 카테고리 및 브랜드 옵션
  const [categories, setCategories] = useState<string[]>([]);
  const [brands, setBrands] = useState<string[]>([]);

  // 제품 목록 조회
  const fetchProducts = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const searchParams: ProductSearchParams = {
        page: currentPage,
        size: pageSize,
      };

      // 필터 조건 추가
      if (filter.searchKeyword.trim()) {
        searchParams.keyword = filter.searchKeyword.trim();
      }

      let response: PageResponse<AdminProductSummary>;

      // 필터에 따른 API 호출
      if (
        filter.selectedStatus &&
        filter.selectedStatus !== "" &&
        Object.values(ProductStatus).includes(
          filter.selectedStatus as ProductStatus
        )
      ) {
        response = await adminProductService.getProductsByStatus(
          filter.selectedStatus as ProductStatus,
          searchParams
        );
      } else if (filter.selectedCategory) {
        response = await adminProductService.getProductsByCategory(
          filter.selectedCategory,
          searchParams
        );
      } else if (filter.selectedBrand) {
        response = await adminProductService.getProductsByBrand(
          filter.selectedBrand,
          searchParams
        );
      } else if (filter.selectedAdmin) {
        response = await adminProductService.getProductsByAdmin(
          filter.selectedAdmin,
          searchParams
        );
      } else if (filter.searchKeyword.trim()) {
        response = await adminProductService.searchProducts(
          filter.searchKeyword.trim(),
          searchParams
        );
      } else {
        response = await adminProductService.getAllProducts(searchParams);
      }

      setProducts(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "제품 목록을 불러오는 중 오류가 발생했습니다."
      );
      setProducts([]);
    } finally {
      setLoading(false);
    }
  }, [currentPage, pageSize, filter]);

  // 카테고리 및 브랜드 목록 조회
  const fetchOptions = useCallback(async () => {
    try {
      const [categoriesData, brandsData] = await Promise.all([
        adminProductService.getAllCategories(),
        adminProductService.getAllBrands(),
      ]);

      setCategories(categoriesData);
      setBrands(brandsData);
    } catch (err) {
      console.error("옵션 데이터를 불러오는 중 오류:", err);
    }
  }, []);

  // 초기 데이터 로드
  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  useEffect(() => {
    fetchOptions();
  }, [fetchOptions]);

  // 필터 변경 핸들러
  const handleFilterChange = (newFilter: Partial<ProductListFilter>) => {
    setFilter((prev) => ({ ...prev, ...newFilter }));
    setCurrentPage(0); // 필터 변경 시 첫 페이지로
  };

  // 검색 핸들러
  const handleSearch = (keyword: string) => {
    handleFilterChange({ searchKeyword: keyword });
  };

  // 페이지 변경 핸들러
  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  // 제품 액션 핸들러들
  const handleProductAction = async (action: string, productId: number) => {
    try {
      switch (action) {
        case "publish":
          await adminProductService.publishProduct(productId);
          onProductPublish?.(productId);
          break;
        case "hide":
          await adminProductService.hideProduct(productId);
          onProductHide?.(productId);
          break;
        case "delete":
          if (window.confirm("정말로 이 제품을 삭제하시겠습니까?")) {
            await adminProductService.deleteProduct(productId);
            onProductDelete?.(productId);
          }
          return;
        default:
          return;
      }

      // 액션 후 목록 새로고침
      fetchProducts();
    } catch (err) {
      alert(
        err instanceof Error ? err.message : "작업 중 오류가 발생했습니다."
      );
    }
  };

  // 상태 배지 스타일
  const getStatusBadgeClass = (status: ProductStatus) => {
    switch (status) {
      case ProductStatus.PUBLISHED:
        return "admin-product-status published";
      case ProductStatus.DRAFT:
        return "admin-product-status draft";
      case ProductStatus.HIDDEN:
        return "admin-product-status hidden";
      case ProductStatus.DISCONTINUED:
        return "admin-product-status discontinued";
      default:
        return "admin-product-status";
    }
  };

  return (
    <div className="admin-product-list">
      {/* 헤더 및 액션 바 */}
      <div className="admin-product-header">
        <div className="admin-product-title">
          <h2>제품 관리</h2>
          <span className="product-count">총 {totalElements}개</span>
        </div>

        <button className="btn-create-product" onClick={onCreateProduct}>
          + 새 제품 등록
        </button>
      </div>

      {/* 검색 및 필터 */}
      <div className="admin-product-filters">
        <div className="filter-row">
          <div className="search-box">
            <input
              type="text"
              placeholder="제품명 또는 설명으로 검색..."
              value={filter.searchKeyword}
              onChange={(e) =>
                handleFilterChange({ searchKeyword: e.target.value })
              }
              onKeyPress={(e) =>
                e.key === "Enter" && handleSearch(filter.searchKeyword)
              }
            />
            <button
              className="btn-search"
              onClick={() => handleSearch(filter.searchKeyword)}
            >
              검색
            </button>
          </div>
        </div>

        <div className="filter-row">
          <select
            value={filter.selectedStatus}
            onChange={(e) =>
              handleFilterChange({
                selectedStatus: e.target.value as ProductStatus | "",
              })
            }
          >
            <option value="">모든 상태</option>
            {Object.entries(ProductStatusDescription).map(([key, value]) => (
              <option key={key} value={key}>
                {value}
              </option>
            ))}
          </select>

          <select
            value={filter.selectedCategory}
            onChange={(e) =>
              handleFilterChange({ selectedCategory: e.target.value })
            }
          >
            <option value="">모든 카테고리</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>

          <select
            value={filter.selectedBrand}
            onChange={(e) =>
              handleFilterChange({ selectedBrand: e.target.value })
            }
          >
            <option value="">모든 브랜드</option>
            {brands.map((brand) => (
              <option key={brand} value={brand}>
                {brand}
              </option>
            ))}
          </select>

          <button
            className="btn-reset-filter"
            onClick={() =>
              setFilter({
                searchKeyword: "",
                selectedCategory: "",
                selectedBrand: "",
                selectedStatus: "",
                selectedAdmin: "",
                sortBy: "newest",
              })
            }
          >
            필터 초기화
          </button>
        </div>
      </div>

      {/* 로딩 상태 */}
      {loading && (
        <div className="admin-product-loading">
          <div className="loading-spinner"></div>
          <p>제품 목록을 불러오는 중...</p>
        </div>
      )}

      {/* 에러 상태 */}
      {error && (
        <div className="admin-product-error">
          <p>❌ {error}</p>
          <button onClick={fetchProducts}>다시 시도</button>
        </div>
      )}

      {/* 제품 목록 */}
      {!loading && !error && (
        <>
          <div className="admin-product-table">
            <table>
              <thead>
                <tr>
                  <th>이미지</th>
                  <th>제품 정보</th>
                  <th>브랜드</th>
                  <th>카테고리</th>
                  <th>가격</th>
                  <th>상태</th>
                  <th>우선순위</th>
                  <th>관리자</th>
                  <th>생성일</th>
                  <th>액션</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr
                    key={product.id}
                    onClick={() => onProductSelect?.(product)}
                  >
                    <td>
                      <div className="product-image">
                        {product.imageUrl ? (
                          <img
                            src={product.imageUrl}
                            alt={product.productName}
                          />
                        ) : (
                          <div className="no-image">No Image</div>
                        )}
                        {product.isFeatured && (
                          <span className="featured-badge">추천</span>
                        )}
                      </div>
                    </td>
                    <td>
                      <div className="product-info">
                        <h4>{product.productName}</h4>
                        <p>{product.preview}</p>
                      </div>
                    </td>
                    <td>{product.brand}</td>
                    <td>{product.category || "-"}</td>
                    <td>
                      {product.price
                        ? `₩${product.price.toLocaleString()}`
                        : "-"}
                    </td>
                    <td>
                      <span className={getStatusBadgeClass(product.status)}>
                        {product.statusDescription}
                      </span>
                    </td>
                    <td>
                      <span className="priority-badge">
                        {product.priorityOrder}
                      </span>
                    </td>
                    <td>{product.adminUser}</td>
                    <td>{new Date(product.createdAt).toLocaleDateString()}</td>
                    <td>
                      <div
                        className="product-actions"
                        onClick={(e) => e.stopPropagation()}
                      >
                        <button
                          className="btn-action edit"
                          onClick={() => onProductEdit?.(product.id)}
                          title="수정"
                        >
                          ✏️
                        </button>

                        {product.status !== ProductStatus.PUBLISHED && (
                          <button
                            className="btn-action publish"
                            onClick={() =>
                              handleProductAction("publish", product.id)
                            }
                            title="게시"
                          >
                            📤
                          </button>
                        )}

                        {product.status === ProductStatus.PUBLISHED && (
                          <button
                            className="btn-action hide"
                            onClick={() =>
                              handleProductAction("hide", product.id)
                            }
                            title="숨김"
                          >
                            👁️‍🗨️
                          </button>
                        )}

                        <button
                          className="btn-action delete"
                          onClick={() =>
                            handleProductAction("delete", product.id)
                          }
                          title="삭제"
                        >
                          🗑️
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* 페이지네이션 */}
          {totalPages > 1 && (
            <div className="admin-product-pagination">
              <button
                onClick={() => handlePageChange(0)}
                disabled={currentPage === 0}
              >
                처음
              </button>

              <button
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage === 0}
              >
                이전
              </button>

              <span className="page-info">
                {currentPage + 1} / {totalPages} 페이지
              </span>

              <button
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
              >
                다음
              </button>

              <button
                onClick={() => handlePageChange(totalPages - 1)}
                disabled={currentPage >= totalPages - 1}
              >
                마지막
              </button>
            </div>
          )}

          {/* 빈 상태 */}
          {products.length === 0 && (
            <div className="admin-product-empty">
              <p>등록된 제품이 없습니다.</p>
              <button onClick={onCreateProduct}>첫 제품을 등록해보세요</button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default AdminProductList;
