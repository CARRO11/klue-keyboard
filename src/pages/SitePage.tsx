import React from "react";
import styled from "@emotion/styled";
import Calendar from "../components/Calendar";
import Stats from "../components/Stats";
import Sidebar from "../components/Sidebar";

const MainContent = styled.div`
  display: flex;
  padding: 2rem;
  gap: 2rem;
  margin-top: 2rem;

  @media (max-width: 768px) {
    flex-direction: column;
    padding: 1rem;
  }
`;

const LeftSection = styled.div`
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2rem;

  @media (max-width: 768px) {
    width: 100%;
  }
`;

const SitePage: React.FC = () => {
  return (
    <MainContent>
      <LeftSection>
        <Calendar />
        <Stats />
      </LeftSection>
      <Sidebar />
    </MainContent>
  );
};

export default SitePage;
