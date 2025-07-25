import React from "react";
import styled from "@emotion/styled";
import { Link, useLocation } from "react-router-dom";

const NavContainer = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #000;
`;

const Logo = styled(Link)`
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
  text-decoration: none;
  span {
    color: red;
  }
`;

const MenuContainer = styled.div`
  display: flex;
  gap: 2rem;
`;

const MenuItem = styled(Link, {
  shouldForwardProp: (prop) => prop !== "$isActive",
})<{ $isActive: boolean }>`
  text-decoration: none;
  color: ${(props) => (props.$isActive ? "#ffd700" : "#fff")};
  font-weight: ${(props) => (props.$isActive ? "600" : "400")};
  transition: color 0.2s ease;

  &:hover {
    color: #ffd700;
  }
`;

const AuthContainer = styled.div`
  display: flex;
  gap: 1rem;
`;

const SignInButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  color: #fff;
  &:hover {
    color: #ffd700;
  }
`;

const CreateAccountButton = styled.button`
  background: #fff;
  color: #000;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  &:hover {
    background: #f0f0f0;
  }
`;

const Navbar = () => {
  const location = useLocation();

  return (
    <NavContainer>
      <Logo to="/">
        <span>/</span> KLUE
      </Logo>
      <MenuContainer>
        <MenuItem
          to="/new-product"
          $isActive={location.pathname === "/new-product"}
        >
          New product
        </MenuItem>
        <MenuItem to="/parts" $isActive={location.pathname === "/parts"}>
          Parts
        </MenuItem>
        <MenuItem to="/build" $isActive={location.pathname === "/build"}>
          Build
        </MenuItem>
        <MenuItem to="/site" $isActive={location.pathname === "/site"}>
          Site
        </MenuItem>
        <MenuItem to="/ai" $isActive={location.pathname === "/ai"}>
          AI
        </MenuItem>
        <MenuItem to="/request" $isActive={location.pathname === "/request"}>
          Request
        </MenuItem>
      </MenuContainer>
      <AuthContainer>
        <SignInButton>마이</SignInButton>
        <CreateAccountButton>회원가입</CreateAccountButton>
      </AuthContainer>
    </NavContainer>
  );
};

export default Navbar;
