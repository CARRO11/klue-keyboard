import React, { useState, useEffect } from "react";
import styled from "@emotion/styled";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import BuildPage from "./pages/BuildPage";
import KeyboardRecommendation from "./pages/KeyboardRecommendation";
import PartsManagement from "./pages/PartsManagement";
import SitePage from "./pages/SitePage";
import AdminProductPage from "./pages/AdminProductPage";
import RequestPage from "./pages/RequestPage";

const AppContainer = styled.div`
  min-height: 100vh;
  background-color: #f8f9fa;
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 1rem;
`;

const HomeContainer = styled.div`
  padding: 2rem;
  min-height: calc(100vh - 60px);
`;

const MainContent = styled.div`
  display: flex;
  gap: 3rem;
  align-items: flex-start;
`;

const LeftSection = styled.div`
  flex: 1;
`;

const RightSection = styled.div`
  width: 320px;
  flex-shrink: 0;
`;

const Title = styled.h1`
  font-size: 2rem;
  color: #333;
  margin-bottom: 1rem;
`;

const Description = styled.div`
  color: #000;
  font-size: 0.9rem;
  margin-bottom: 2rem;
  padding: 0.75rem;
  background-color: #fffaf0;
  border-radius: 4px;
  border-left: 4px solid #ffd700;
`;

const CalendarContainer = styled.div`
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const CalendarHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
`;

const CalendarNavButton = styled.button`
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #666;
  padding: 0.5rem;

  &:hover {
    color: #333;
  }
`;

const CalendarTitle = styled.h3`
  font-size: 1.1rem;
  color: #333;
  margin: 0;
`;

const CalendarGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
`;

const CalendarDay = styled.div`
  text-align: center;
  padding: 0.5rem;
  font-size: 0.85rem;
  color: #999;
  font-weight: 500;
`;

const CalendarDate = styled.div<{ $isToday?: boolean }>`
  text-align: center;
  padding: 0.5rem;
  font-size: 0.9rem;
  color: #333;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s;

  ${(props) =>
    props.$isToday &&
    `
    background-color: #000;
    color: white;
    font-weight: 600;
  `}

  &:hover {
    background-color: ${(props) => (props.$isToday ? "#333" : "#f0f0f0")};
  }
`;

const CategorySection = styled.div`
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const CategoryTitle = styled.h3`
  font-size: 1.1rem;
  color: #333;
  margin-bottom: 1rem;
  text-align: center;
`;

const CategoryList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

const CategoryItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background-color: #f8f9fa;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #e9ecef;
  }
`;

const CategoryName = styled.span<{ $isLoading?: boolean }>`
  color: ${(props) => (props.$isLoading ? "#999" : "#333")};
  font-size: 0.9rem;
`;

const CategoryCount = styled.span<{ $isLoading?: boolean }>`
  background-color: ${(props) => (props.$isLoading ? "#ccc" : "#000")};
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
  min-width: 20px;
  text-align: center;
  ${(props) =>
    props.$isLoading && "animation: pulse 1.5s ease-in-out infinite;"}

  @keyframes pulse {
    0%,
    100% {
      opacity: 1;
    }
    50% {
      opacity: 0.5;
    }
  }
`;

const BASE_URL = process.env.REACT_APP_API_URL || "http://127.0.0.1:8080";

const HomePage = () => {
  const navigate = useNavigate();
  const [currentDate, setCurrentDate] = useState(new Date());
  const [categoryStats, setCategoryStats] = useState({
    case: 0,
    plate: 0,
    pcb: 0,
    switches: 0,
    keycaps: 0,
    stabilizers: 0,
    foam: 0,
  });
  const [loading, setLoading] = useState(true);

  // 부품 개수 가져오기
  useEffect(() => {
    const fetchComponentStats = async () => {
      try {
        setLoading(true);

        // 실제 데이터가 있는 카테고리들
        const promises = [
          fetch(`${BASE_URL}/switches`).then((res) => res.json()),
          fetch(`${BASE_URL}/keycaps`).then((res) => res.json()),
          fetch(`${BASE_URL}/pcbs`).then((res) => res.json()),
          fetch(`${BASE_URL}/plates`).then((res) => res.json()),
          fetch(`${BASE_URL}/stabilizers`).then((res) => res.json()),
        ];

        const [switchesRes, keycapsRes, pcbsRes, platesRes, stabilizersRes] =
          await Promise.all(promises);

        setCategoryStats({
          case: 1, // 더미 데이터
          plate: platesRes.success ? platesRes.count : 2,
          pcb: pcbsRes.success ? pcbsRes.count : 3,
          switches: switchesRes.success ? switchesRes.count : 1,
          keycaps: keycapsRes.success ? keycapsRes.count : 2,
          stabilizers: stabilizersRes.success ? stabilizersRes.count : 3,
          foam: 1, // 더미 데이터
        });
      } catch (error) {
        console.error("부품 통계 조회 실패:", error);
        // 에러 시 기본값 유지
        setCategoryStats({
          case: 1,
          plate: 2,
          pcb: 3,
          switches: 1,
          keycaps: 2,
          stabilizers: 3,
          foam: 1,
        });
      } finally {
        setLoading(false);
      }
    };

    fetchComponentStats();
  }, []);

  // 캘린더 생성 함수
  const generateCalendar = () => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startingDayOfWeek = firstDay.getDay();
    const daysInMonth = lastDay.getDate();

    const days = [];
    const dayNames = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];

    // 요일 헤더
    dayNames.forEach((day) => {
      days.push(<CalendarDay key={day}>{day}</CalendarDay>);
    });

    // 빈 칸들 (월 시작 전)
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(<CalendarDate key={`empty-${i}`}></CalendarDate>);
    }

    // 실제 날짜들
    const today = new Date();
    for (let day = 1; day <= daysInMonth; day++) {
      const isToday =
        today.getDate() === day &&
        today.getMonth() === month &&
        today.getFullYear() === year;

      days.push(
        <CalendarDate key={day} $isToday={isToday}>
          {day}
        </CalendarDate>
      );
    }

    return days;
  };

  const navigateMonth = (direction: number) => {
    setCurrentDate((prev) => {
      const newDate = new Date(prev);
      newDate.setMonth(prev.getMonth() + direction);
      return newDate;
    });
  };

  const handleCategoryClick = (categoryId: string) => {
    switch (categoryId) {
      case "switches":
      case "plate":
      case "pcb":
      case "keycaps":
      case "stabilizers":
        navigate("/parts");
        break;
      case "case":
      case "foam":
        navigate("/build");
        break;
      default:
        navigate("/parts");
    }
  };

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const categories = [
    { id: "case", name: "Case 케이스", count: categoryStats.case },
    { id: "plate", name: "Plate 보강판", count: categoryStats.plate },
    { id: "pcb", name: "PCB 기판", count: categoryStats.pcb },
    { id: "switches", name: "Switches 스위치", count: categoryStats.switches },
    { id: "keycaps", name: "Keycaps 키캡", count: categoryStats.keycaps },
    {
      id: "stabilizers",
      name: "Stabilizers 스테빌라이저",
      count: categoryStats.stabilizers,
    },
    { id: "foam", name: "Foam 폼 / 차음재", count: categoryStats.foam },
  ];

  return (
    <HomeContainer>
      <MainContent>
        <LeftSection>
          <Title>KLUE for custom keyboard</Title>
          <Description>
            스위치, 키캡, 하우징, 조립까지!
            <br />
            초보자도 쉽게 접근할 수 있는 커스텀 키보드 플랫폼, KLUE
          </Description>

          <CalendarContainer>
            <CalendarHeader>
              <CalendarNavButton onClick={() => navigateMonth(-1)}>
                ←
              </CalendarNavButton>
              <CalendarTitle>
                {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
              </CalendarTitle>
              <CalendarNavButton onClick={() => navigateMonth(1)}>
                →
              </CalendarNavButton>
            </CalendarHeader>
            <CalendarGrid>{generateCalendar()}</CalendarGrid>
          </CalendarContainer>
        </LeftSection>

        <RightSection>
          <CategorySection>
            <CategoryTitle>키보드와 친해지기</CategoryTitle>
            <CategoryList>
              {categories.map((category) => (
                <CategoryItem
                  key={category.id}
                  onClick={() => handleCategoryClick(category.id)}
                >
                  <CategoryName $isLoading={loading}>
                    {category.name}
                  </CategoryName>
                  <CategoryCount $isLoading={loading}>
                    {loading ? "..." : category.count}
                  </CategoryCount>
                </CategoryItem>
              ))}
            </CategoryList>
          </CategorySection>
        </RightSection>
      </MainContent>
    </HomeContainer>
  );
};

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <AppContainer>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/parts" element={<PartsManagement />} />
          <Route path="/build" element={<BuildPage />} />
          <Route path="/ai" element={<KeyboardRecommendation />} />
          <Route path="/site" element={<SitePage />} />
          <Route path="/new-product" element={<AdminProductPage />} />
          <Route path="/request" element={<RequestPage />} />
        </Routes>
      </AppContainer>
    </BrowserRouter>
  );
};

export default App;
