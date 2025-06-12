import React, { useState } from "react";
import {
  AdminProductSummary,
  AdminProductResponse,
} from "../types/AdminProduct";
import AdminProductList from "../components/AdminProductList";
import AdminProductForm from "../components/AdminProductForm";
import "./AdminProductPage.css";

interface AdminProductPageProps {
  adminUser?: string;
}

const AdminProductPage: React.FC<AdminProductPageProps> = ({
  adminUser = "admin", // 기본값 설정
}) => {
  // 현재 모드 상태 관리
  const [currentMode, setCurrentMode] = useState<"list" | "create" | "edit">(
    "list"
  );
  const [selectedProductId, setSelectedProductId] = useState<
    number | undefined
  >();

  // 제품 목록으로 돌아가기
  const handleBackToList = () => {
    setCurrentMode("list");
    setSelectedProductId(undefined);
  };

  // 새 제품 생성 모드
  const handleCreateProduct = () => {
    setCurrentMode("create");
    setSelectedProductId(undefined);
  };

  // 제품 수정 모드
  const handleEditProduct = (productId: number) => {
    setCurrentMode("edit");
    setSelectedProductId(productId);
  };

  // 제품 선택 핸들러
  const handleProductSelect = (product: AdminProductSummary) => {
    console.log("선택된 제품:", product);
    // 필요시 상세 페이지로 이동하거나 모달 표시
  };

  // 제품 삭제 핸들러
  const handleProductDelete = (productId: number) => {
    console.log("삭제된 제품 ID:", productId);
    // 목록 새로고침은 AdminProductList에서 처리
  };

  // 제품 게시 핸들러
  const handleProductPublish = (productId: number) => {
    console.log("게시된 제품 ID:", productId);
    // 목록 새로고침은 AdminProductList에서 처리
  };

  // 제품 숨김 핸들러
  const handleProductHide = (productId: number) => {
    console.log("숨김 처리된 제품 ID:", productId);
    // 목록 새로고침은 AdminProductList에서 처리
  };

  // 제품 저장 완료 핸들러
  const handleProductSave = (product: AdminProductResponse) => {
    console.log("저장된 제품:", product);
    alert(`제품 "${product.productName}"이(가) 성공적으로 저장되었습니다.`);
    handleBackToList();
  };

  return (
    <div className="admin-product-page">
      {/* 헤더 */}
      <div className="admin-header">
        <div className="admin-nav">
          <h1>KLUE 관리자</h1>
          <nav className="admin-breadcrumb">
            <span>제품 관리</span>
            {currentMode === "create" && <span> &gt; 새 제품 등록</span>}
            {currentMode === "edit" && <span> &gt; 제품 수정</span>}
          </nav>
        </div>

        <div className="admin-user-info">
          <span>관리자: {adminUser}</span>
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <div className="admin-content">
        {currentMode === "list" && (
          <AdminProductList
            onProductSelect={handleProductSelect}
            onProductEdit={handleEditProduct}
            onProductDelete={handleProductDelete}
            onProductPublish={handleProductPublish}
            onProductHide={handleProductHide}
            onCreateProduct={handleCreateProduct}
          />
        )}

        {currentMode === "create" && (
          <div className="admin-form-container">
            <div className="form-overlay" onClick={handleBackToList} />
            <div className="form-modal">
              <AdminProductForm
                adminUser={adminUser}
                onSave={handleProductSave}
                onCancel={handleBackToList}
              />
            </div>
          </div>
        )}

        {currentMode === "edit" && selectedProductId && (
          <div className="admin-form-container">
            <div className="form-overlay" onClick={handleBackToList} />
            <div className="form-modal">
              <AdminProductForm
                productId={selectedProductId}
                adminUser={adminUser}
                onSave={handleProductSave}
                onCancel={handleBackToList}
              />
            </div>
          </div>
        )}
      </div>

      {/* 푸터 */}
      <div className="admin-footer">
        <p>&copy; 2024 KLUE 키보드 추천 시스템. All rights reserved.</p>
      </div>
    </div>
  );
};

export default AdminProductPage;
