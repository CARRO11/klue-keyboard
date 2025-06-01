import React from "react";
import styled from "@emotion/styled";

const SidebarContainer = styled.div`
  width: 300px;
  padding: 1rem;
`;

const Title = styled.h2`
  font-size: 1.2rem;
  margin-bottom: 1rem;
`;

const MenuList = styled.ul`
  list-style: none;
  padding: 0;
`;

const MenuItem = styled.li`
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  cursor: pointer;
  &:hover {
    color: #666;
  }
`;

const Count = styled.span`
  color: #666;
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
            <span>{item.name}</span>
            <Count>{item.count}</Count>
          </MenuItem>
        ))}
      </MenuList>
    </SidebarContainer>
  );
};

export default Sidebar;
