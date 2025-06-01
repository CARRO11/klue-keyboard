import React from "react";
import styled from "@emotion/styled";
import { Link } from "react-router-dom";

const HeaderContainer = styled.div`
  padding: 2rem;
  margin-bottom: 2rem;
`;

const TitleLink = styled(Link)`
  text-decoration: none;
  color: inherit;
  cursor: pointer;
  display: inline-block;

  &:hover {
    opacity: 0.8;
  }
`;

const Title = styled.h1`
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 1rem;
`;

const Subtitle = styled.p`
  font-size: 1rem;
  color: #666;
  margin-bottom: 0.5rem;
`;

const MainHeader = () => {
  return (
    <HeaderContainer>
      <TitleLink to="/">
        <Title>Custom is You Clue is KLUE</Title>
      </TitleLink>
      <Subtitle>스위치, 키캡, 하우징, 조립까지..</Subtitle>
      <Subtitle>
        초보자도 쉽게 접근할 수 있는 커스텀 키보드 플랫폼, KLUE입니다.
      </Subtitle>
    </HeaderContainer>
  );
};

export default MainHeader;
