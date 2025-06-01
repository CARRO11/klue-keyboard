import React from "react";
import styled from "@emotion/styled";

const StatsContainer = styled.div`
  display: flex;
  gap: 4rem;
  padding: 2rem;
  margin-top: 2rem;
`;

const StatItem = styled.div`
  display: flex;
  flex-direction: column;
`;

const StatValue = styled.span`
  font-size: 2.5rem;
  font-weight: bold;
`;

const StatLabel = styled.span`
  color: #666;
  margin-top: 0.5rem;
`;

const Stats = () => {
  return (
    <StatsContainer>
      <StatItem>
        <StatValue>2943</StatValue>
        <StatLabel>Keyboards Built</StatLabel>
      </StatItem>
      <StatItem>
        <StatValue>$1M+</StatValue>
        <StatLabel>Parts Tracked</StatLabel>
      </StatItem>
    </StatsContainer>
  );
};

export default Stats;
