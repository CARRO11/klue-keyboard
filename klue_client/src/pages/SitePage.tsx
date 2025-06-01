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
  text-align: center;
`;

const SiteList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const SiteRow = styled.div`
  display: grid;
  grid-template-columns: 2fr 2fr 3fr;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
`;

const Cell = styled.div`
  padding: 1.5rem;
  display: flex;
  align-items: center;
  border-right: 1px solid #e9ecef;
  min-height: 70px;

  &:last-child {
    border-right: none;
  }
`;

const SiteName = styled(Cell)`
  font-weight: 600;
  color: #333;
  font-size: 1.1rem;
`;

const Products = styled(Cell)`
  color: #666;
  line-height: 1.5;
`;

const SiteUrl = styled(Cell)`
  color: #228be6;
  text-decoration: none;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`;

const HeaderRow = styled.div`
  display: grid;
  grid-template-columns: 2fr 2fr 3fr;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-weight: 600;
  color: #495057;
`;

const HeaderCell = styled.div`
  padding: 1rem 1.5rem;
  border-right: 1px solid #dee2e6;

  &:last-child {
    border-right: none;
  }
`;

interface Site {
  id: number;
  name: string;
  products: string;
  url: string;
}

const SitePage = () => {
  const sites: Site[] = [
    {
      id: 1,
      name: "키보드매니아",
      products: "키보드, 키캡, 스위치, PCB, 플레이트",
      url: "https://kbdmania.net",
    },
    {
      id: 2,
      name: "Drop",
      products: "프리미엄 키보드, 키캡, 스위치, 악세서리",
      url: "https://drop.com",
    },
    {
      id: 3,
      name: "Keycaps.info",
      products: "키캡 프로파일, 키캡 정보, 소재 정보",
      url: "https://keycaps.info",
    },
    {
      id: 4,
      name: "KBDfans",
      products: "키보드 키트, 키캡, 스위치, 하우징",
      url: "https://kbdfans.com",
    },
    {
      id: 5,
      name: "NovelKeys",
      products: "스위치, 키캡, 키보드 키트, 악세서리",
      url: "https://novelkeys.com",
    },
    {
      id: 6,
      name: "Dixie Mech",
      products: "키보드 키트, 키캡, 데스크매트",
      url: "https://dixiemech.com",
    },
    {
      id: 7,
      name: "Cannon Keys",
      products: "키보드 키트, PCB, 플레이트, 스위치",
      url: "https://cannonkeys.com",
    },
  ];

  const handleUrlClick = (url: string) => {
    window.open(url, "_blank", "noopener noreferrer");
  };

  return (
    <PageContainer>
      <Title>Keyboard Sites</Title>
      <HeaderRow>
        <HeaderCell>사이트 이름</HeaderCell>
        <HeaderCell>취급 품목</HeaderCell>
        <HeaderCell>웹사이트 주소</HeaderCell>
      </HeaderRow>
      <SiteList>
        {sites.map((site) => (
          <SiteRow key={site.id}>
            <SiteName>{site.name}</SiteName>
            <Products>{site.products}</Products>
            <SiteUrl onClick={() => handleUrlClick(site.url)}>
              {site.url}
            </SiteUrl>
          </SiteRow>
        ))}
      </SiteList>
    </PageContainer>
  );
};

export default SitePage;
