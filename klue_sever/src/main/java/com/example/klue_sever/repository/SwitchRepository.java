package com.example.klue_sever.repository;

import com.example.klue_sever.entity.Switch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwitchRepository extends JpaRepository<Switch, Integer> {
    // 필요한 경우 여기에 커스텀 쿼리 메서드를 추가할 수 있습니다.
} 