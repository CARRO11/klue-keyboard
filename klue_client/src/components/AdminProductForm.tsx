import React, { useState, useEffect } from "react";
import {
  AdminProductRequest,
  AdminProductResponse,
  ProductStatus,
  ProductStatusDescription,
  ProductFormState,
  ProductFormErrors,
} from "../types/AdminProduct";
import { adminProductService } from "../services/adminProductService";

interface AdminProductFormProps {
  productId?: number; // 수정 모드일 때 제품 ID
  onSave?: (product: AdminProductResponse) => void;
  onCancel?: () => void;
  adminUser: string; // 현재 관리자 사용자명
}

const AdminProductForm: React.FC<AdminProductFormProps> = ({
  productId,
  onSave,
  onCancel,
  adminUser,
}) => {
  // 폼 상태 관리
  const [formState, setFormState] = useState<ProductFormState>({
    values: {
      productName: "",
      description: "",
      brand: "",
      category: "",
      price: undefined,
      imageUrl: "",
      productUrl: "",
      status: ProductStatus.DRAFT,
      adminUser: adminUser,
      priorityOrder: 0,
      isFeatured: false,
    },
    errors: {},
    isLoading: false,
    isSubmitting: false,
  });

  // 카테고리 및 브랜드 옵션
  const [categories, setCategories] = useState<string[]>([]);
  const [brands, setBrands] = useState<string[]>([]);

  // 수정 모드일 때 기존 데이터 로드
  useEffect(() => {
    if (productId) {
      loadProduct();
    }
  }, [productId]);

  // 옵션 데이터 로드
  useEffect(() => {
    loadOptions();
  }, []);

  // 기존 제품 데이터 로드
  const loadProduct = async () => {
    if (!productId) return;

    setFormState((prev) => ({ ...prev, isLoading: true }));

    try {
      const product = await adminProductService.getProductById(productId);

      setFormState((prev) => ({
        ...prev,
        values: {
          productName: product.productName,
          description: product.description,
          brand: product.brand,
          category: product.category || "",
          price: product.price,
          imageUrl: product.imageUrl || "",
          productUrl: product.productUrl || "",
          status: product.status,
          adminUser: product.adminUser,
          priorityOrder: product.priorityOrder,
          isFeatured: product.isFeatured,
        },
        isLoading: false,
      }));
    } catch (error) {
      console.error("제품 로드 실패:", error);
      alert("제품 정보를 불러오는 중 오류가 발생했습니다.");
      setFormState((prev) => ({ ...prev, isLoading: false }));
    }
  };

  // 옵션 데이터 로드
  const loadOptions = async () => {
    try {
      const [categoriesData, brandsData] = await Promise.all([
        adminProductService.getAllCategories(),
        adminProductService.getAllBrands(),
      ]);

      setCategories(categoriesData);
      setBrands(brandsData);
    } catch (error) {
      console.error("옵션 데이터 로드 실패:", error);
    }
  };

  // 폼 값 변경 핸들러
  const handleInputChange = (field: keyof AdminProductRequest, value: any) => {
    setFormState((prev) => ({
      ...prev,
      values: {
        ...prev.values,
        [field]: value,
      },
      errors: {
        ...prev.errors,
        [field]: undefined, // 입력 시 에러 초기화
      },
    }));
  };

  // 폼 유효성 검사
  const validateForm = (): boolean => {
    const errors: ProductFormErrors = {};
    const { values } = formState;

    // 필수 필드 검사
    if (!values.productName.trim()) {
      errors.productName = "제품명은 필수입니다.";
    } else if (values.productName.length > 200) {
      errors.productName = "제품명은 200자를 초과할 수 없습니다.";
    }

    if (!values.description.trim()) {
      errors.description = "제품 설명은 필수입니다.";
    }

    if (!values.brand.trim()) {
      errors.brand = "브랜드는 필수입니다.";
    } else if (values.brand.length > 100) {
      errors.brand = "브랜드는 100자를 초과할 수 없습니다.";
    }

    if (values.category && values.category.length > 100) {
      errors.category = "카테고리는 100자를 초과할 수 없습니다.";
    }

    if (values.price !== undefined && values.price <= 0) {
      errors.price = "가격은 0보다 커야 합니다.";
    }

    if (values.imageUrl && values.imageUrl.length > 500) {
      errors.imageUrl = "이미지 URL은 500자를 초과할 수 없습니다.";
    }

    if (values.productUrl && values.productUrl.length > 500) {
      errors.productUrl = "제품 URL은 500자를 초과할 수 없습니다.";
    }

    if (!values.adminUser.trim()) {
      errors.adminUser = "관리자 사용자명은 필수입니다.";
    }

    setFormState((prev) => ({ ...prev, errors }));
    return Object.keys(errors).length === 0;
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setFormState((prev) => ({ ...prev, isSubmitting: true }));

    try {
      let savedProduct: AdminProductResponse;

      if (productId) {
        // 수정 모드
        savedProduct = await adminProductService.updateProduct(
          productId,
          formState.values
        );
      } else {
        // 생성 모드
        savedProduct = await adminProductService.createProduct(
          formState.values
        );
      }

      alert(
        productId
          ? "제품이 성공적으로 수정되었습니다."
          : "제품이 성공적으로 등록되었습니다."
      );
      onSave?.(savedProduct);
    } catch (error) {
      console.error("제품 저장 실패:", error);
      alert(
        error instanceof Error
          ? error.message
          : "제품 저장 중 오류가 발생했습니다."
      );
    } finally {
      setFormState((prev) => ({ ...prev, isSubmitting: false }));
    }
  };

  // 이미지 미리보기
  const handleImagePreview = () => {
    if (formState.values.imageUrl) {
      window.open(formState.values.imageUrl, "_blank");
    }
  };

  if (formState.isLoading) {
    return (
      <div className="admin-product-form loading">
        <div className="loading-spinner"></div>
        <p>제품 정보를 불러오는 중...</p>
      </div>
    );
  }

  return (
    <div className="admin-product-form">
      <div className="form-header">
        <h2>{productId ? "제품 수정" : "새 제품 등록"}</h2>
        <button type="button" className="btn-close" onClick={onCancel}>
          ✕
        </button>
      </div>

      <form onSubmit={handleSubmit} className="product-form">
        {/* 기본 정보 섹션 */}
        <div className="form-section">
          <h3>기본 정보</h3>

          <div className="form-group">
            <label htmlFor="productName">
              제품명 <span className="required">*</span>
            </label>
            <input
              type="text"
              id="productName"
              value={formState.values.productName}
              onChange={(e) => handleInputChange("productName", e.target.value)}
              placeholder="제품명을 입력하세요"
              maxLength={200}
              className={formState.errors.productName ? "error" : ""}
            />
            {formState.errors.productName && (
              <span className="error-message">
                {formState.errors.productName}
              </span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="description">
              제품 설명 <span className="required">*</span>
            </label>
            <textarea
              id="description"
              value={formState.values.description}
              onChange={(e) => handleInputChange("description", e.target.value)}
              placeholder="제품에 대한 상세한 설명을 입력하세요"
              rows={5}
              className={formState.errors.description ? "error" : ""}
            />
            {formState.errors.description && (
              <span className="error-message">
                {formState.errors.description}
              </span>
            )}
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="brand">
                브랜드 <span className="required">*</span>
              </label>
              <input
                type="text"
                id="brand"
                value={formState.values.brand}
                onChange={(e) => handleInputChange("brand", e.target.value)}
                placeholder="브랜드명"
                maxLength={100}
                list="brands-list"
                className={formState.errors.brand ? "error" : ""}
              />
              <datalist id="brands-list">
                {brands.map((brand) => (
                  <option key={brand} value={brand} />
                ))}
              </datalist>
              {formState.errors.brand && (
                <span className="error-message">{formState.errors.brand}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="category">카테고리</label>
              <input
                type="text"
                id="category"
                value={formState.values.category}
                onChange={(e) => handleInputChange("category", e.target.value)}
                placeholder="카테고리"
                maxLength={100}
                list="categories-list"
                className={formState.errors.category ? "error" : ""}
              />
              <datalist id="categories-list">
                {categories.map((category) => (
                  <option key={category} value={category} />
                ))}
              </datalist>
              {formState.errors.category && (
                <span className="error-message">
                  {formState.errors.category}
                </span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="price">가격 (원)</label>
            <input
              type="number"
              id="price"
              value={formState.values.price || ""}
              onChange={(e) =>
                handleInputChange(
                  "price",
                  e.target.value ? parseFloat(e.target.value) : undefined
                )
              }
              placeholder="가격을 입력하세요"
              min="0"
              step="0.01"
              className={formState.errors.price ? "error" : ""}
            />
            {formState.errors.price && (
              <span className="error-message">{formState.errors.price}</span>
            )}
          </div>
        </div>

        {/* 미디어 정보 섹션 */}
        <div className="form-section">
          <h3>미디어 정보</h3>

          <div className="form-group">
            <label htmlFor="imageUrl">이미지 URL</label>
            <div className="url-input-group">
              <input
                type="url"
                id="imageUrl"
                value={formState.values.imageUrl}
                onChange={(e) => handleInputChange("imageUrl", e.target.value)}
                placeholder="제품 이미지 URL"
                maxLength={500}
                className={formState.errors.imageUrl ? "error" : ""}
              />
              {formState.values.imageUrl && (
                <button
                  type="button"
                  className="btn-preview"
                  onClick={handleImagePreview}
                >
                  미리보기
                </button>
              )}
            </div>
            {formState.errors.imageUrl && (
              <span className="error-message">{formState.errors.imageUrl}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="productUrl">제품 URL</label>
            <input
              type="url"
              id="productUrl"
              value={formState.values.productUrl}
              onChange={(e) => handleInputChange("productUrl", e.target.value)}
              placeholder="제품 상세 페이지 URL"
              maxLength={500}
              className={formState.errors.productUrl ? "error" : ""}
            />
            {formState.errors.productUrl && (
              <span className="error-message">
                {formState.errors.productUrl}
              </span>
            )}
          </div>
        </div>

        {/* 관리 정보 섹션 */}
        <div className="form-section">
          <h3>관리 정보</h3>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="status">상태</label>
              <select
                id="status"
                value={formState.values.status}
                onChange={(e) =>
                  handleInputChange("status", e.target.value as ProductStatus)
                }
              >
                {Object.entries(ProductStatusDescription).map(
                  ([key, value]) => (
                    <option key={key} value={key}>
                      {value}
                    </option>
                  )
                )}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="priorityOrder">우선순위</label>
              <input
                type="number"
                id="priorityOrder"
                value={formState.values.priorityOrder}
                onChange={(e) =>
                  handleInputChange(
                    "priorityOrder",
                    parseInt(e.target.value) || 0
                  )
                }
                placeholder="0"
                min="0"
              />
              <small>높은 숫자일수록 먼저 표시됩니다</small>
            </div>
          </div>

          <div className="form-group">
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={formState.values.isFeatured}
                onChange={(e) =>
                  handleInputChange("isFeatured", e.target.checked)
                }
              />
              <span className="checkbox-text">특별 추천 제품으로 설정</span>
            </label>
          </div>

          <div className="form-group">
            <label htmlFor="adminUser">
              관리자 <span className="required">*</span>
            </label>
            <input
              type="text"
              id="adminUser"
              value={formState.values.adminUser}
              onChange={(e) => handleInputChange("adminUser", e.target.value)}
              placeholder="관리자 사용자명"
              maxLength={100}
              className={formState.errors.adminUser ? "error" : ""}
            />
            {formState.errors.adminUser && (
              <span className="error-message">
                {formState.errors.adminUser}
              </span>
            )}
          </div>
        </div>

        {/* 폼 액션 */}
        <div className="form-actions">
          <button
            type="button"
            className="btn-cancel"
            onClick={onCancel}
            disabled={formState.isSubmitting}
          >
            취소
          </button>

          <button
            type="submit"
            className="btn-save"
            disabled={formState.isSubmitting}
          >
            {formState.isSubmitting
              ? productId
                ? "수정 중..."
                : "등록 중..."
              : productId
                ? "수정"
                : "등록"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default AdminProductForm;
