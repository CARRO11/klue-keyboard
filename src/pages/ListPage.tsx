import React from "react";
import styled from "@emotion/styled";

const PageContainer = styled.div`
  padding: 2rem;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  margin-bottom: 2rem;
`;

const ListPage = () => {
  return (
    <PageContainer>
      <Title>Keyboard List</Title>
      {/* 여기에 키보드 목록 컴포넌트가 들어갈 예정입니다 */}
    </PageContainer>
  );
};

export default ListPage;
