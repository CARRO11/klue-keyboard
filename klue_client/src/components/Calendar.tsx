import React from "react";
import styled from "@emotion/styled";

const CalendarContainer = styled.div`
  padding: 2rem;
  width: 100%;
  max-width: 1000px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const CalendarHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
`;

const CalendarTitle = styled.div`
  font-weight: bold;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const CurrentDate = styled.div`
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
`;

const SubTitle = styled.div`
  font-size: 1rem;
  color: #666;
`;

const NavigationButtons = styled.div`
  display: flex;
  gap: 1rem;
`;

const NavButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
  color: #333;
  padding: 0.5rem;
  &:hover {
    color: #666;
  }
`;

const CalendarGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 1rem;
`;

const WeekDay = styled.div`
  text-align: center;
  padding: 1rem;
  font-weight: bold;
  color: #666;
  font-size: 1rem;
`;

const Day = styled.div`
  text-align: center;
  padding: 1.2rem;
  cursor: pointer;
  border-radius: 8px;
  font-size: 1.1rem;
  &:hover {
    background-color: #f0f0f0;
  }
`;

const TimeDisplay = styled.div`
  margin-top: 2rem;
  font-size: 1.2rem;
  color: #666;
`;

const Calendar = () => {
  const weekDays = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  const days = Array.from({ length: 31 }, (_, i) => i + 1);

  // 현재 날짜 정보
  const currentDate = new Date();
  const month = currentDate.toLocaleString("en-US", { month: "long" });
  const year = currentDate.getFullYear();

  return (
    <CalendarContainer>
      <CalendarHeader>
        <CalendarTitle>
          <CurrentDate>
            {month} {year}
          </CurrentDate>
          <SubTitle>Keyboards Built</SubTitle>
        </CalendarTitle>
        <NavigationButtons>
          <NavButton>←</NavButton>
          <NavButton>→</NavButton>
        </NavigationButtons>
      </CalendarHeader>
      <CalendarGrid>
        {weekDays.map((day) => (
          <WeekDay key={day}>{day}</WeekDay>
        ))}
        {days.map((day) => (
          <Day key={day}>{day}</Day>
        ))}
      </CalendarGrid>
      <TimeDisplay>
        Time{" "}
        {currentDate.toLocaleTimeString("en-US", {
          hour: "2-digit",
          minute: "2-digit",
          hour12: true,
        })}
      </TimeDisplay>
    </CalendarContainer>
  );
};

export default Calendar;
