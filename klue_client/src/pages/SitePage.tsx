import React from "react";
import styled from "@emotion/styled";

const Container = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
`;

const Title = styled.h1`
  text-align: center;
  margin-bottom: 3rem;
  color: #333;
  font-size: 2.5rem;
`;

const SiteGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
`;

const SiteCard = styled.div`
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
`;

const SiteName = styled.h3`
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.5rem;
`;

const SiteDescription = styled.p`
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.6;
`;

const SiteSpecs = styled.div`
  margin-bottom: 1.5rem;
`;

const SpecItem = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  padding: 0.25rem 0;
  border-bottom: 1px solid #eee;

  &:last-child {
    border-bottom: none;
  }
`;

const SpecLabel = styled.span`
  font-weight: 600;
  color: #555;
`;

const SpecValue = styled.span`
  color: #777;
`;

const SiteLink = styled.a`
  display: inline-block;
  background: #3498db;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 600;
  transition: background 0.2s ease;

  &:hover {
    background: #2980b9;
  }
`;

const CategoryTitle = styled.h2`
  color: #2c3e50;
  margin: 3rem 0 2rem 0;
  font-size: 2rem;
  border-bottom: 3px solid #3498db;
  padding-bottom: 0.5rem;
`;

const SitePage: React.FC = () => {
  const keyboardSites = [
    {
      id: 1,
      name: "KBDfans",
      description:
        "중국 기반의 가장 유명한 커스텀 키보드 전문 쇼핑몰. 다양한 브랜드의 키보드 부품과 완제품을 판매합니다.",
      specialty: "종합 키보드 쇼핑몰",
      shipping: "한국 배송 가능",
      priceRange: "중급~고급",
      strongPoints: "다양한 제품군, 합리적 가격",
      link: "https://kbdfans.com",
    },
    {
      id: 2,
      name: "Drop (Massdrop)",
      description:
        "커뮤니티 기반 그룹바이 플랫폼. 한정판 키캡과 키보드를 주기적으로 판매합니다.",
      specialty: "그룹바이 전문",
      shipping: "전 세계 배송",
      priceRange: "중급~프리미엄",
      strongPoints: "독점 제품, GMK 키캡",
      link: "https://drop.com",
    },
    {
      id: 3,
      name: "NovelKeys",
      description:
        "미국의 프리미엄 커스텀 키보드 브랜드. 고품질 스위치와 키보드로 유명합니다.",
      specialty: "프리미엄 스위치 & 키보드",
      shipping: "국제 배송",
      priceRange: "고급~프리미엄",
      strongPoints: "NK 스위치, 고품질",
      link: "https://novelkeys.com",
    },
    {
      id: 4,
      name: "ZealPC",
      description:
        "Zealios 스위치로 유명한 캐나다의 프리미엄 키보드 부품 전문업체입니다.",
      specialty: "프리미엄 스위치",
      shipping: "전 세계 배송",
      priceRange: "프리미엄",
      strongPoints: "Zealios 스위치, 고급 스테빌라이저",
      link: "https://zealpc.net",
    },
    {
      id: 5,
      name: "Keychron",
      description:
        "무선 키보드 전문 브랜드. 매력적인 디자인과 합리적인 가격의 완제품을 제공합니다.",
      specialty: "무선 키보드",
      shipping: "한국 공식 판매",
      priceRange: "엔트리~중급",
      strongPoints: "무선 기능, 완제품",
      link: "https://www.keychron.com",
    },
  ];

  const koreanSites = [
    {
      id: 1,
      name: "레오폴드",
      description:
        "한국의 프리미엄 키보드 브랜드. 견고한 품질과 깔끔한 디자인으로 유명합니다.",
      specialty: "프리미엄 완제품",
      shipping: "국내 브랜드",
      priceRange: "중급~고급",
      strongPoints: "뛰어난 품질, A/S",
      link: "https://www.leopold.co.kr",
    },
    {
      id: 2,
      name: "아이락스",
      description: "다양한 키보드와 마우스를 판매하는 국내 브랜드입니다.",
      specialty: "게이밍 주변기기",
      shipping: "국내 브랜드",
      priceRange: "엔트리~중급",
      strongPoints: "합리적 가격",
      link: "https://www.abko.co.kr",
    },
    {
      id: 3,
      name: "큐센",
      description: "커스텀 키보드 부품과 완제품을 판매하는 한국 쇼핑몰입니다.",
      specialty: "커스텀 키보드",
      shipping: "국내 배송",
      priceRange: "중급~고급",
      strongPoints: "국내 A/S, 커스텀 지원",
      link: "https://qsenn.com",
    },
    {
      id: 4,
      name: "스웨그키",
      description:
        "한국의 커스텀 키보드 전문 브랜드. 고품질 키캡과 스위치를 제작합니다.",
      specialty: "커스텀 키캡 & 스위치",
      shipping: "국내 브랜드",
      priceRange: "중급~고급",
      strongPoints: "독창적 디자인, 한정판 제품",
      link: "https://swagkeys.com",
    },
  ];

  return (
    <Container>
      <Title>Sites</Title>

      <CategoryTitle>🌍 해외 브랜드</CategoryTitle>
      <SiteGrid>
        {keyboardSites.map((site) => (
          <SiteCard key={site.id}>
            <SiteName>{site.name}</SiteName>
            <SiteDescription>{site.description}</SiteDescription>
            <SiteSpecs>
              <SpecItem>
                <SpecLabel>전문 분야:</SpecLabel>
                <SpecValue>{site.specialty}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>배송:</SpecLabel>
                <SpecValue>{site.shipping}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>가격대:</SpecLabel>
                <SpecValue>{site.priceRange}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>강점:</SpecLabel>
                <SpecValue>{site.strongPoints}</SpecValue>
              </SpecItem>
            </SiteSpecs>
            <SiteLink
              href={site.link}
              target="_blank"
              rel="noopener noreferrer"
            >
              사이트 방문하기
            </SiteLink>
          </SiteCard>
        ))}
      </SiteGrid>

      <CategoryTitle>🇰🇷 국내 브랜드</CategoryTitle>
      <SiteGrid>
        {koreanSites.map((site) => (
          <SiteCard key={site.id}>
            <SiteName>{site.name}</SiteName>
            <SiteDescription>{site.description}</SiteDescription>
            <SiteSpecs>
              <SpecItem>
                <SpecLabel>전문 분야:</SpecLabel>
                <SpecValue>{site.specialty}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>배송:</SpecLabel>
                <SpecValue>{site.shipping}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>가격대:</SpecLabel>
                <SpecValue>{site.priceRange}</SpecValue>
              </SpecItem>
              <SpecItem>
                <SpecLabel>강점:</SpecLabel>
                <SpecValue>{site.strongPoints}</SpecValue>
              </SpecItem>
            </SiteSpecs>
            <SiteLink
              href={site.link}
              target="_blank"
              rel="noopener noreferrer"
            >
              사이트 방문하기
            </SiteLink>
          </SiteCard>
        ))}
      </SiteGrid>
    </Container>
  );
};

export default SitePage;
