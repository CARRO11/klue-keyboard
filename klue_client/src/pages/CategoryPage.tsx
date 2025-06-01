import React from "react";
import styled from "@emotion/styled";
import { useParams } from "react-router-dom";
import MainHeader from "../components/MainHeader";

const PageContainer = styled.div`
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
`;

const CategoryTitle = styled.h2`
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #333;
  text-align: center;
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
`;

const ProductCard = styled.div`
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
`;

const ProductImage = styled.div`
  width: 100%;
  height: 200px;
  background-color: #f8f9fa;
  position: relative;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ProductInfo = styled.div`
  padding: 1.5rem;
`;

const ProductName = styled.h3`
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  color: #333;
`;

const ProductDescription = styled.p`
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 1rem;
`;

const ProductPrice = styled.div`
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
`;

interface Product {
  id: number;
  name: string;
  description: string;
  price: string;
  image: string;
}

// 임시 데이터
const categoryProducts: Record<string, Product[]> = {
  case: [
    {
      id: 1,
      name: "KLUE 알루미늄 케이스",
      description: "프리미엄 CNC 가공 알루미늄 케이스",
      price: "₩159,000",
      image: "https://via.placeholder.com/280x200",
    },
    {
      id: 2,
      name: "KLUE 폴리카보네이트 케이스",
      description: "투명한 폴리카보네이트 소재의 케이스",
      price: "₩89,000",
      image: "https://via.placeholder.com/280x200",
    },
  ],
  plate: [
    {
      id: 1,
      name: "KLUE 알루미늄 플레이트",
      description: "단단한 타건감의 알루미늄 플레이트",
      price: "₩39,000",
      image: "https://via.placeholder.com/280x200",
    },
    {
      id: 2,
      name: "KLUE FR4 플레이트",
      description: "부드러운 타건감의 FR4 플레이트",
      price: "₩29,000",
      image: "https://via.placeholder.com/280x200",
    },
  ],
  // 다른 카테고리의 제품들도 비슷한 형식으로 추가할 수 있습니다
};

const CategoryPage = () => {
  const { categoryId } = useParams<{ categoryId: string }>();
  const products = categoryProducts[categoryId || ""] || [];

  const getCategoryTitle = (id: string) => {
    switch (id) {
      case "case":
        return "케이스";
      case "plate":
        return "플레이트";
      case "pcb":
        return "PCB";
      case "switch":
        return "스위치";
      case "keycap":
        return "키캡";
      case "stabilizer":
        return "스테빌라이저";
      default:
        return "";
    }
  };

  return (
    <PageContainer>
      <MainHeader />
      <CategoryTitle>
        {getCategoryTitle(categoryId || "")} 제품 목록
      </CategoryTitle>
      <ProductGrid>
        {products.map((product) => (
          <ProductCard key={product.id}>
            <ProductImage>
              <img src={product.image} alt={product.name} />
            </ProductImage>
            <ProductInfo>
              <ProductName>{product.name}</ProductName>
              <ProductDescription>{product.description}</ProductDescription>
              <ProductPrice>{product.price}</ProductPrice>
            </ProductInfo>
          </ProductCard>
        ))}
      </ProductGrid>
    </PageContainer>
  );
};

export default CategoryPage;
