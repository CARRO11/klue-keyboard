import React, { useState } from "react";
import styled from "@emotion/styled";

const PageContainer = styled.div`
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
  background-color: #f5f5f5;
  min-height: 100vh;
`;

const FilterContainer = styled.div`
  margin-bottom: 1rem;
  display: flex;
  justify-content: flex-end;
`;

const FilterSelect = styled.select`
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  font-size: 0.9rem;
`;

const TableContainer = styled.div`
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
`;

const TableHeader = styled.thead`
  background-color: #f8f9fa;
`;

const TableRow = styled.tr`
  border-bottom: 1px solid #e9ecef;

  &:hover {
    background-color: #f8f9fa;
  }
`;

const TableHeaderCell = styled.th`
  padding: 1rem;
  text-align: center;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #dee2e6;
`;

const TableCell = styled.td`
  padding: 1rem;
  text-align: center;
  color: #555;
  vertical-align: middle;
`;

const ProductName = styled.span`
  font-weight: 500;
  color: #333;
`;

const CategoryBadge = styled.span`
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.85rem;
  font-weight: 500;
`;

const ProductLink = styled.a`
  color: #1976d2;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;

const RemarksCell = styled.td`
  padding: 1rem;
  text-align: center;
  color: #555;
  vertical-align: middle;
  position: relative;
`;

const HeartButton = styled.div`
  width: 30px;
  height: 30px;
  background-color: #ff4081;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  cursor: pointer;
  color: white;
  font-size: 1rem;

  &:hover {
    background-color: #e91e63;
  }
`;

const NewProductPage = () => {
  const [filter, setFilter] = useState("발매일 순");

  const products = [
    {
      id: 1,
      name: "Meltrix Zoom75 V2",
      releaseDate: "2025-06",
      category: "케이스",
      link: "https://funkeys.co.kr/products/zoom75-v2",
      hasHeart: true,
    },
    {
      id: 2,
      name: "Mode Envoy 2025 Edition",
      releaseDate: "2025-06",
      category: "케이스",
      link: "https://funkeys.co.kr/products/zoom75-v2",
      hasHeart: false,
    },
    {
      id: 3,
      name: "Owlab Link65",
      releaseDate: "2025-06",
      category: "케이스",
      link: "https://owlab.store/products/link65",
      hasHeart: false,
    },
    {
      id: 4,
      name: "Gateron KS-20 Lunar Gray",
      releaseDate: "2025-06",
      category: "스위치",
      link: "https://epomaker.com/products/ks20-lunar",
      hasHeart: false,
    },
    {
      id: 5,
      name: "Aluminum Plate",
      releaseDate: "2025-06",
      category: "보강판",
      link: "https://owlab.store/products/link65-plate",
      hasHeart: false,
    },
    {
      id: 6,
      name: "WT65-G Hotswap PCB",
      releaseDate: "2025-07",
      category: "기판",
      link: "https://geon.works/products/wt65g-pcb",
      hasHeart: false,
    },
    {
      id: 7,
      name: "Owlstabs V3",
      releaseDate: "2025-07",
      category: "스태빌라이저",
      link: "https://owlab.store/products/owlstabs-v3",
      hasHeart: false,
    },
    {
      id: 8,
      name: "PE Foam for Mode Envoy",
      releaseDate: "2025-07",
      category: "완충재",
      link: "https://modedesigns.com/products/envoy-pe-foamh",
      hasHeart: false,
    },
  ];

  return (
    <PageContainer>
      <FilterContainer>
        <FilterSelect
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        >
          <option>발매일 순</option>
          <option>이름 순</option>
          <option>카테고리 순</option>
        </FilterSelect>
      </FilterContainer>

      <TableContainer>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHeaderCell>상품명</TableHeaderCell>
              <TableHeaderCell>발매일</TableHeaderCell>
              <TableHeaderCell>부품 종류</TableHeaderCell>
              <TableHeaderCell>Link</TableHeaderCell>
              <TableHeaderCell>비고란</TableHeaderCell>
            </TableRow>
          </TableHeader>
          <tbody>
            {products.map((product) => (
              <TableRow key={product.id}>
                <TableCell>
                  <ProductName>{product.name}</ProductName>
                </TableCell>
                <TableCell>{product.releaseDate}</TableCell>
                <TableCell>
                  <CategoryBadge>{product.category}</CategoryBadge>
                </TableCell>
                <TableCell>
                  <ProductLink
                    href={product.link}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    {product.link}
                  </ProductLink>
                </TableCell>
                <RemarksCell>
                  {product.hasHeart && <HeartButton>♥</HeartButton>}
                </RemarksCell>
              </TableRow>
            ))}
          </tbody>
        </Table>
      </TableContainer>
    </PageContainer>
  );
};

export default NewProductPage;
