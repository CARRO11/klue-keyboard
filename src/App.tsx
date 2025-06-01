import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import MainHeader from "./components/MainHeader";
import SitePage from "./pages/SitePage";
import ListPage from "./pages/ListPage";
import CategoryPage from "./pages/CategoryPage";
import BuildPage from "./pages/BuildPage";

function App() {
  return (
    <Router>
      <div className="app">
        <Navbar />
        <MainHeader />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<SitePage />} />
            <Route path="/list" element={<ListPage />} />
            <Route path="/category" element={<CategoryPage />} />
            <Route path="/build" element={<BuildPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
