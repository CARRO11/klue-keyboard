import React from "react";
import styled from "@emotion/styled";
import { Link } from "react-router-dom";
import MainHeader from "../components/MainHeader";

const PageContainer = styled.div`
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
`;

const CategoryContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 2rem;
`;

const CategoryCard = styled(Link)`
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-decoration: none;
  color: inherit;
  transition: transform 0.2s ease-in-out;
  border: 1px solid #e9ecef;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
`;

const CategoryTitle = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: #333;
`;

const CategoryDescription = styled.p`
  color: #666;
  font-size: 1rem;
  line-height: 1.5;
`;

interface Category {
  id: string;
  title: string;
  description: string;
  path: string;
}

const categories: Category[] = [
  {
    id: "case",
    title: "Case",
    description:
      "키보드의 기본 틀이 되는 케이스입니다. 다양한 재질과 디자인의 케이스를 확인해보세요.",
    path: "/list/case",
  },
  {
    id: "plate",
    title: "Plate",
    description:
      "스위치를 고정하고 타건감에 영향을 주는 플레이트입니다. 알루미늄, 폴리카보네이트 등 다양한 소재가 준비되어 있습니다.",
    path: "/list/plate",
  },
  {
    id: "pcb",
    title: "PCB",
    description:
      "키보드의 두뇌 역할을 하는 PCB입니다. 핫스왑, 솔더링 등 다양한 옵션이 있습니다.",
    path: "/list/pcb",
  },
  {
    id: "switch",
    title: "Switch",
    description:
      "키보드의 촉감과 소리를 결정하는 스위치입니다. 리니어, 택타일, 클릭 등 다양한 타입이 있습니다.",
    path: "/list/switch",
  },
  {
    id: "keycap",
    title: "Keycap",
    description:
      "키보드의 완성도를 높여주는 키캡입니다. PBT, ABS 등 다양한 소재와 프로파일이 준비되어 있습니다.",
    path: "/list/keycap",
  },
  {
    id: "stabilizer",
    title: "Stabilizer",
    description:
      "긴 키의 안정성을 담당하는 스테빌라이저입니다. 정교한 튜닝으로 더 나은 타건감을 제공합니다.",
    path: "/list/stabilizer",
  },
];

const ListPage = () => {
  return (
    <PageContainer>
      <MainHeader />
      <CategoryContainer>
        {categories.map((category) => (
          <CategoryCard key={category.id} to={category.path}>
            <CategoryTitle>{category.title}</CategoryTitle>
            <CategoryDescription>{category.description}</CategoryDescription>
          </CategoryCard>
        ))}
      </CategoryContainer>
    </PageContainer>
  );
};

export default ListPage;
