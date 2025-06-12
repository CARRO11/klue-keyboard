import React, { useState, useEffect } from "react";
import styled from "@emotion/styled";
import {
  allPartsService,
  PartType,
  Part,
  PART_TYPE_LABELS,
  PART_RESPONSE_FIELDS,
} from "../services/allPartsService";

const Container = styled.div`
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
`;

const Title = styled.h1`
  color: #333;
  margin-bottom: 2rem;
  font-size: 2.5rem;
  text-align: center;
`;

const PartTypeSection = styled.div`
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
`;

const PartTypeButton = styled.button<{ active?: boolean }>`
  padding: 0.75rem 1.5rem;
  border: 2px solid ${(props) => (props.active ? "#000" : "#ddd")};
  background: ${(props) => (props.active ? "#000" : "white")};
  color: ${(props) => (props.active ? "white" : "#333")};
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
  white-space: nowrap;

  &:hover {
    border-color: #000;
    background: ${(props) => (props.active ? "#333" : "#f8f9ff")};
  }
`;

const SearchSection = styled.div`
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const SearchInput = styled.input`
  flex: 1;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
`;

const SearchButton = styled.button`
  padding: 0.75rem 1.5rem;
  background: #000;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.2s;

  &:hover {
    background: #333;
  }
`;

const StatsSection = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
`;

const StatCard = styled.div`
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
`;

const StatNumber = styled.div`
  font-size: 2rem;
  font-weight: bold;
  color: #ffd700;
  margin-bottom: 0.5rem;
`;

const StatLabel = styled.div`
  color: #666;
  font-size: 0.9rem;
`;

const PartsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
`;

const PartCard = styled.div`
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    box-shadow 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }
`;

const PartName = styled.h3`
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.25rem;
`;

const PartDetail = styled.div`
  margin: 0.5rem 0;
  color: #666;
  font-size: 0.9rem;
`;

const PartBadge = styled.span`
  display: inline-block;
  padding: 0.25rem 0.5rem;
  background: #000;
  color: white;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 1rem;
`;

const LoadingDiv = styled.div`
  text-align: center;
  padding: 3rem;
  font-size: 1.2rem;
  color: #666;
`;

const ErrorDiv = styled.div`
  text-align: center;
  padding: 2rem;
  color: #dc3545;
  background: #f8d7da;
  border-radius: 8px;
  margin: 1rem 0;
`;

const PaginationDiv = styled.div`
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
`;

const PageButton = styled.button<{ active?: boolean }>`
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background: ${(props) => (props.active ? "#007bff" : "white")};
  color: ${(props) => (props.active ? "white" : "#333")};
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background: ${(props) => (props.active ? "#0056b3" : "#f8f9fa")};
  }
`;

const PartsManagement: React.FC = () => {
  const [currentPartType, setCurrentPartType] = useState<PartType>("switches");
  const [parts, setParts] = useState<Part[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [stats, setStats] = useState<any>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // 부품 목록 로드
  const loadParts = async (partType: PartType, page = 0) => {
    try {
      setLoading(true);
      setError(null);
      console.log(`Loading ${partType} parts`); // 디버깅용 로그

      const response = await allPartsService.getAll(partType, {
        page: 0,
        size: 1000, // 모든 데이터를 가져오기 위해 큰 사이즈 설정
      });

      console.log("API Response:", response); // 디버깅용 로그

      // 응답에서 해당 부품 타입의 데이터 추출
      const fieldName = PART_RESPONSE_FIELDS[partType];
      const partsData = (response as any)[fieldName] || [];

      console.log(`Found ${partsData.length} ${partType} items`); // 디버깅용 로그

      setParts(partsData);
      setCurrentPage(0);
      setTotalPages(1); // 페이징이 없으므로 1페이지로 설정
    } catch (err: any) {
      console.error("Load parts error:", err);
      setError(
        `${PART_TYPE_LABELS[partType]} 목록을 불러오는데 실패했습니다. (${err.message || "알 수 없는 오류"})`
      );
    } finally {
      setLoading(false);
    }
  };

  // 통계 로드
  const loadStats = async (partType: PartType) => {
    try {
      const response = await allPartsService.getStats(partType);
      setStats(response.statistics);
    } catch (err) {
      console.error("통계 로드 실패:", err);
    }
  };

  // 부품 타입 변경
  const handlePartTypeChange = (partType: PartType) => {
    setCurrentPartType(partType);
    setCurrentPage(0);
    setSearchKeyword("");
    loadParts(partType);
    loadStats(partType);
  };

  // 검색
  const handleSearch = async () => {
    if (!searchKeyword.trim()) {
      loadParts(currentPartType);
      return;
    }

    try {
      setLoading(true);
      // search API 대신 일반 API를 사용하고 클라이언트에서 필터링
      const response = await allPartsService.getAll(currentPartType, {
        page: 0,
        size: 1000, // 검색을 위해 더 많은 데이터 가져오기
      });

      const fieldName = PART_RESPONSE_FIELDS[currentPartType];
      const allPartsData = (response as any)[fieldName] || [];

      // 클라이언트 측에서 검색 필터링
      const filteredData = allPartsData.filter(
        (part: any) =>
          part.name?.toLowerCase().includes(searchKeyword.toLowerCase()) ||
          part.type?.toLowerCase().includes(searchKeyword.toLowerCase()) ||
          part.material?.toLowerCase().includes(searchKeyword.toLowerCase())
      );

      setParts(filteredData); // 모든 검색 결과 표시
      setCurrentPage(0);
      setTotalPages(1); // 페이징이 없으므로 1페이지로 설정
    } catch (err) {
      setError("검색에 실패했습니다.");
      console.error("Search error:", err);
    } finally {
      setLoading(false);
    }
  };

  // 부품별 상세 정보 렌더링
  const renderPartDetails = (part: Part) => {
    const details: React.ReactNode[] = [];

    // 공통 필드들
    if ("type" in part)
      details.push(
        <PartDetail key="type">
          <strong>타입:</strong> {part.type}
        </PartDetail>
      );
    if ("material" in part)
      details.push(
        <PartDetail key="material">
          <strong>재질:</strong> {part.material || "N/A"}
        </PartDetail>
      );
    if ("size" in part)
      details.push(
        <PartDetail key="size">
          <strong>크기:</strong> {part.size || "N/A"}
        </PartDetail>
      );
    if ("profile" in part)
      details.push(
        <PartDetail key="profile">
          <strong>프로필:</strong> {part.profile}
        </PartDetail>
      );
    if ("layout" in part)
      details.push(
        <PartDetail key="layout">
          <strong>레이아웃:</strong> {part.layout}
        </PartDetail>
      );
    if ("length" in part)
      details.push(
        <PartDetail key="length">
          <strong>길이:</strong> {part.length || "N/A"}
        </PartDetail>
      );
    if ("typing" in part)
      details.push(
        <PartDetail key="typing">
          <strong>타이핑:</strong> {part.typing || "N/A"}
        </PartDetail>
      );

    // 스위치 특별 필드들
    if ("linearScore" in part) {
      details.push(
        <PartDetail key="scores">
          <strong>점수:</strong> Linear({part.linearScore}) / Tactile(
          {part.tactileScore}) / Sound({part.soundScore})
        </PartDetail>
      );
    }

    return details;
  };

  useEffect(() => {
    loadParts(currentPartType);
    loadStats(currentPartType);
  }, []);

  if (loading && parts.length === 0) {
    return (
      <LoadingDiv>{PART_TYPE_LABELS[currentPartType]} 로딩 중...</LoadingDiv>
    );
  }

  return (
    <Container>
      <Title>키보드 부품 관리</Title>

      {/* 부품 타입 선택 */}
      <PartTypeSection>
        {allPartsService.getAllPartTypes().map(({ type, label }) => (
          <PartTypeButton
            key={type}
            active={currentPartType === type}
            onClick={() => handlePartTypeChange(type)}
          >
            {label}
          </PartTypeButton>
        ))}
      </PartTypeSection>

      {/* 통계 섹션 */}
      {stats && (
        <StatsSection>
          <StatCard>
            <StatNumber>
              {(Object.values(stats).find(
                (v) => typeof v === "number"
              ) as number) || 0}
            </StatNumber>
            <StatLabel>총 {PART_TYPE_LABELS[currentPartType]}</StatLabel>
          </StatCard>
          {Object.entries(stats).map(([key, value]) => {
            if (typeof value === "object" && value !== null) {
              return (
                <StatCard key={key}>
                  <StatNumber>{Object.keys(value).length}</StatNumber>
                  <StatLabel>
                    {key === "materialDistribution"
                      ? "재질 종류"
                      : key === "sizeDistribution"
                        ? "크기 종류"
                        : key === "typeDistribution"
                          ? "타입 종류"
                          : key}
                  </StatLabel>
                </StatCard>
              );
            }
            return null;
          })}
        </StatsSection>
      )}

      {/* 검색 섹션 */}
      <SearchSection>
        <SearchInput
          type="text"
          placeholder={`${PART_TYPE_LABELS[currentPartType]} 이름으로 검색...`}
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          onKeyPress={(e) => e.key === "Enter" && handleSearch()}
        />
        <SearchButton onClick={handleSearch}>검색</SearchButton>
        <SearchButton
          onClick={() => {
            setSearchKeyword("");
            loadParts(currentPartType);
          }}
        >
          초기화
        </SearchButton>
      </SearchSection>

      {/* 에러 표시 */}
      {error && <ErrorDiv>{error}</ErrorDiv>}

      {/* 부품 목록 */}
      <PartsGrid>
        {parts.map((part) => (
          <PartCard key={part.id}>
            <PartBadge>{PART_TYPE_LABELS[currentPartType]}</PartBadge>
            <PartName>{part.name}</PartName>
            {renderPartDetails(part)}
            {part.link && (
              <PartDetail>
                <a href={part.link} target="_blank" rel="noopener noreferrer">
                  상품 링크
                </a>
              </PartDetail>
            )}
          </PartCard>
        ))}
      </PartsGrid>

      {/* 데이터가 없을 때 */}
      {!loading && parts.length === 0 && !error && (
        <LoadingDiv>
          {PART_TYPE_LABELS[currentPartType]} 데이터가 없습니다.
        </LoadingDiv>
      )}
    </Container>
  );
};

export default PartsManagement;
