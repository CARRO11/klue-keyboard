import React from "react";
import styled from "@emotion/styled";
import { Link, useNavigate } from "react-router-dom";

const NavContainer = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const Logo = styled(Link)`
  font-size: 1.5rem;
  font-weight: bold;
  color: #000;
  text-decoration: none;
  span {
    color: red;
  }
`;

const MenuContainer = styled.div`
  display: flex;
  gap: 2rem;
`;

const MenuItem = styled(Link)`
  text-decoration: none;
  color: #000;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.2s ease-in-out;

  &:hover {
    color: #666;
    background-color: #f5f5f5;
  }

  &.active {
    color: red;
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
  padding: 0.5rem 1rem;
  font-weight: 500;
  &:hover {
    color: #666;
  }
`;

const CreateAccountButton = styled.button`
  background: #000;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s ease-in-out;
  &:hover {
    background: #333;
  }
`;

const Navbar = () => {
  const navigate = useNavigate();

  const handleNavigation = (path: string) => {
    navigate(path);
  };

  return (
    <NavContainer>
      <Logo to="/">
        <span>/</span> KLUE
      </Logo>
      <MenuContainer>
        <MenuItem to="/list">List</MenuItem>
        <MenuItem to="/new-product">New product</MenuItem>
        <MenuItem to="/site">SITE</MenuItem>
        <MenuItem to="/qna">Q & A</MenuItem>
      </MenuContainer>
      <AuthContainer>
        <SignInButton onClick={() => handleNavigation("/signin")}>
          Sign in
        </SignInButton>
        <CreateAccountButton onClick={() => handleNavigation("/signup")}>
          Create free account
        </CreateAccountButton>
      </AuthContainer>
    </NavContainer>
  );
};

export default Navbar;
