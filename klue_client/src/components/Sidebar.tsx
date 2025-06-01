import React from "react";
import styled from "@emotion/styled";

const SidebarContainer = styled.div`
  width: 250px;
  min-width: 200px;
  padding: 1.5rem;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  @media (max-width: 1200px) {
    width: 220px;
  }

  @media (max-width: 768px) {
    width: 100%;
    margin-top: 1rem;
  }
`;

const Title = styled.h2`
  font-size: 1.1rem;
  margin-bottom: 1.2rem;
  color: #333;
  font-weight: 600;
`;

const MenuList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

const MenuItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.7rem 0;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    color: #666;
  }
`;

const ItemName = styled.span`
  font-size: 0.9rem;
`;

const Count = styled.span`
  color: #666;
  font-size: 0.8rem;
  background-color: #f5f5f5;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
`;

const Sidebar = () => {
  const menuItems = [
    { name: "Case 케이스", count: 1 },
    { name: "Plate 보강판", count: 2 },
    { name: "PCB 기판", count: 3 },
    { name: "Switches 스위치", count: 1 },
    { name: "Keycaps 키캡", count: 2 },
    { name: "Stabilizers 스테빌라이저", count: 3 },
    { name: "Foam 폼 / 방음재", count: 1 },
  ];

  return (
    <SidebarContainer>
      <Title>키보드의 첫 걸음</Title>
      <MenuList>
        {menuItems.map((item, index) => (
          <MenuItem key={index}>
            <ItemName>{item.name}</ItemName>
            <Count>{item.count}</Count>
          </MenuItem>
        ))}
      </MenuList>
    </SidebarContainer>
  );
};

export default Sidebar;
