import React from "react";
import styled from "@emotion/styled";

const CalendarContainer = styled.div`
  padding: 1rem;
  width: 100%;
  max-width: 800px;
`;

const CalendarHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  gap: 1rem;
`;

const CalendarTitle = styled.div`
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

const NavigationButtons = styled.div`
  display: flex;
  gap: 0.5rem;
`;

const NavButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
`;

const CalendarGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
`;

const WeekDay = styled.div`
  text-align: center;
  padding: 0.5rem;
  font-weight: bold;
  color: #666;
`;

const Day = styled.div<{ isToday?: boolean }>`
  text-align: center;
  padding: 0.5rem;
  cursor: pointer;
  border-radius: 50%;
  ${(props) =>
    props.isToday &&
    `
    background-color: red;
    color: white;
  `}
  &:hover {
    background-color: #f0f0f0;
  }
`;

const TimeDisplay = styled.div`
  margin-top: 1rem;
  font-size: 1.2rem;
`;

const Calendar = () => {
  const weekDays = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  const days = Array.from({ length: 31 }, (_, i) => i + 1);

  return (
    <CalendarContainer>
      <CalendarHeader>
        <CalendarTitle>
          <span>Keyboards Built</span>
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
          <Day key={day} isToday={day === 7}>
            {day}
          </Day>
        ))}
      </CalendarGrid>
      <TimeDisplay>Time 09:41 AM</TimeDisplay>
    </CalendarContainer>
  );
};

export default Calendar;
