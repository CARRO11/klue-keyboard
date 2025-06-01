import React from "react";
import styled from "@emotion/styled";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import MainHeader from "./components/MainHeader";
import Calendar from "./components/Calendar";
import Sidebar from "./components/Sidebar";
import Stats from "./components/Stats";
import SitePage from "./pages/SitePage";
import ListPage from "./pages/ListPage";
import CategoryPage from "./pages/CategoryPage";
import BuildPage from "./pages/BuildPage";
import KeyboardRecommendation from "./pages/KeyboardRecommendation";

const AppContainer = styled.div`
  min-height: 100vh;
  background-color: #f8f9fa;
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 1rem;
`;

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

const HomePage = () => (
  <>
    <MainHeader />
    <MainContent>
      <LeftSection>
        <Calendar />
        <Stats />
      </LeftSection>
      <Sidebar />
    </MainContent>
  </>
);

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <AppContainer>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/site" element={<SitePage />} />
          <Route path="/list" element={<ListPage />} />
          <Route path="/list/:categoryId" element={<CategoryPage />} />
          <Route path="/build" element={<BuildPage />} />
          <Route path="/ai" element={<KeyboardRecommendation />} />
        </Routes>
      </AppContainer>
    </BrowserRouter>
  );
};

export default App;
