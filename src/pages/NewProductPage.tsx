import React from "react";
import styled from "@emotion/styled";

const PageContainer = styled.div`
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #333;
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
`;

const ProductCard = styled.div`
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: translateY(-4px);
  }
`;

const ProductImage = styled.div`
  width: 100%;
  height: 200px;
  background-color: #f5f5f5;
  position: relative;
  overflow: hidden;

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

const PriceTag = styled.div`
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
`;

const Badge = styled.span`
  position: absolute;
  top: 1rem;
  right: 1rem;
  background-color: #ff4444;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
`;

const NewProductPage = () => {
  const products = [
    {
      id: 1,
      name: "KLUE K1 Keyboard",
      description: "프리미엄 알루미늄 커스텀 키보드",
      price: "₩259,000",
      image: "https://via.placeholder.com/280x200",
      isNew: true,
    },
    {
      id: 2,
      name: "KLUE Switch Pro",
      description: "부드러운 타격감의 리니어 스위치",
      price: "₩89,000",
      image: "https://via.placeholder.com/280x200",
      isNew: true,
    },
    {
      id: 3,
      name: "KLUE Keycap Set",
      description: "PBT 소재의 프리미엄 키캡",
      price: "₩129,000",
      image: "https://via.placeholder.com/280x200",
      isNew: true,
    },
    {
      id: 4,
      name: "KLUE Stabilizer",
      description: "고급 스테빌라이저 세트",
      price: "₩39,000",
      image: "https://via.placeholder.com/280x200",
      isNew: true,
    },
  ];

  return (
    <PageContainer>
      <Title>New Products</Title>
      <ProductGrid>
        {products.map((product) => (
          <ProductCard key={product.id}>
            <ProductImage>
              <img src={product.image} alt={product.name} />
              {product.isNew && <Badge>NEW</Badge>}
            </ProductImage>
            <ProductInfo>
              <ProductName>{product.name}</ProductName>
              <ProductDescription>{product.description}</ProductDescription>
              <PriceTag>{product.price}</PriceTag>
            </ProductInfo>
          </ProductCard>
        ))}
      </ProductGrid>
    </PageContainer>
  );
};

export default NewProductPage;
