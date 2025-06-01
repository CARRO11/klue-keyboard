import numpy as np
import pandas as pd
from typing import Dict, List
import json
import os

class KeyboardDataGenerator:
    def __init__(self):
        # 실제 스위치 데이터
        self.real_switches = {
            # 체리 MX 시리즈
            'Cherry MX Red': {
                'type': 'linear',
                'linearScore': 0.9,    # 선형성 (매우 높음)
                'tactileScore': 0.1,   # 촉각성 (매우 낮음)
                'soundScore': 0.5,     # 소리 (보통)
                'weightScore': 0.6,    # 무게 (45g)
                'smoothnessScore': 0.8, # 부드러움 (매우 부드러움)
                'speedScore': 0.8,     # 속도 (빠름)
                'stabilityScore': 0.7,  # 안정성 (좋음)
                'durabilityScore': 0.8  # 내구성 (1억회)
            },
            'Cherry MX Brown': {
                'type': 'tactile',
                'linearScore': 0.3,    # 선형성 (낮음)
                'tactileScore': 0.8,   # 촉각성 (높음)
                'soundScore': 0.6,     # 소리 (중간)
                'weightScore': 0.6,    # 무게 (45g)
                'smoothnessScore': 0.7, # 부드러움 (부드러움)
                'speedScore': 0.7,     # 속도 (빠름)
                'stabilityScore': 0.7,  # 안정성 (좋음)
                'durabilityScore': 0.8  # 내구성 (1억회)
            },
            'Cherry MX Blue': {
                'type': 'clicky',
                'linearScore': 0.3,    # 선형성 (낮음)
                'tactileScore': 0.8,   # 촉각성 (높음)
                'soundScore': 0.9,     # 소리 (매우 큼)
                'weightScore': 0.7,    # 무게 (50g)
                'smoothnessScore': 0.6, # 부드러움 (보통)
                'speedScore': 0.6,     # 속도 (보통)
                'stabilityScore': 0.7,  # 안정성 (좋음)
                'durabilityScore': 0.8  # 내구성 (1억회)
            },
            
            # 게이트론 스위치
            'Gateron Yellow': {
                'type': 'linear',
                'linearScore': 0.85,   # 선형성 (매우 높음)
                'tactileScore': 0.1,   # 촉각성 (매우 낮음)
                'soundScore': 0.6,     # 소리 (중간)
                'weightScore': 0.55,   # 무게 (50g)
                'smoothnessScore': 0.9, # 부드러움 (매우 부드러움)
                'speedScore': 0.85,    # 속도 (매우 빠름)
                'stabilityScore': 0.75, # 안정성 (매우 좋음)
                'durabilityScore': 0.7  # 내구성 (8천만회)
            }
        }
        
        # 사운드 프로파일별 특성 범위
        self.sound_rules = {
            'silent': {
                'dampingScore': (0.7, 1.0),
                'densityScore': (0.6, 0.9),
                'flexibilityScore': (0.4, 0.7),
                'thicknessScore': (0.5, 0.8),
                'absorptionScore': (0.7, 1.0),
                'resonanceScore': (0.0, 0.3)
            },
            'thocky': {
                'dampingScore': (0.5, 0.8),
                'densityScore': (0.7, 1.0),
                'flexibilityScore': (0.5, 0.8),
                'thicknessScore': (0.6, 0.9),
                'absorptionScore': (0.5, 0.8),
                'resonanceScore': (0.3, 0.6)
            },
            'clacky': {
                'dampingScore': (0.2, 0.5),
                'densityScore': (0.3, 0.6),
                'flexibilityScore': (0.6, 0.9),
                'thicknessScore': (0.3, 0.6),
                'absorptionScore': (0.2, 0.5),
                'resonanceScore': (0.7, 1.0)
            }
        }
    
    def add_switch(
        self,
        name: str,
        switch_type: str,
        linear_score: float,
        tactile_score: float,
        sound_score: float,
        weight_score: float,
        smoothness_score: float,
        speed_score: float,
        stability_score: float,
        durability_score: float
    ):
        """새로운 스위치 추가
        
        Args:
            name: 스위치 이름
            switch_type: 스위치 타입 ('linear', 'tactile', 'clicky' 중 하나)
            linear_score: 선형성 점수 (0~1)
            tactile_score: 촉각성 점수 (0~1)
            sound_score: 소리 점수 (0~1)
            weight_score: 무게 점수 (0~1)
            smoothness_score: 부드러움 점수 (0~1)
            speed_score: 속도 점수 (0~1)
            stability_score: 안정성 점수 (0~1)
            durability_score: 내구성 점수 (0~1)
        """
        if switch_type not in ['linear', 'tactile', 'clicky']:
            raise ValueError("switch_type must be one of: 'linear', 'tactile', 'clicky'")
            
        # 모든 점수가 0~1 사이인지 확인
        scores = [
            linear_score, tactile_score, sound_score,
            weight_score, smoothness_score, speed_score,
            stability_score, durability_score
        ]
        
        for score in scores:
            if not 0 <= score <= 1:
                raise ValueError("All scores must be between 0 and 1")
        
        # 새로운 스위치 추가
        self.real_switches[name] = {
            'type': switch_type,
            'linearScore': linear_score,
            'tactileScore': tactile_score,
            'soundScore': sound_score,
            'weightScore': weight_score,
            'smoothnessScore': smoothness_score,
            'speedScore': speed_score,
            'stabilityScore': stability_score,
            'durabilityScore': durability_score
        }
        
        print(f"스위치 '{name}'가 성공적으로 추가되었습니다.")
    
    def remove_switch(self, name: str):
        """스위치 제거"""
        if name in self.real_switches:
            del self.real_switches[name]
            print(f"스위치 '{name}'가 성공적으로 제거되었습니다.")
        else:
            print(f"스위치 '{name}'를 찾을 수 없습니다.")
    
    def list_switches(self):
        """등록된 모든 스위치 출력"""
        print("\n등록된 스위치 목록:")
        print("-" * 50)
        for name, data in self.real_switches.items():
            print(f"\n스위치: {name}")
            print(f"타입: {data['type']}")
            print(f"선형성: {data['linearScore']:.2f}")
            print(f"촉각성: {data['tactileScore']:.2f}")
            print(f"소리: {data['soundScore']:.2f}")
            print(f"무게: {data['weightScore']:.2f}")
            print(f"부드러움: {data['smoothnessScore']:.2f}")
            print(f"속도: {data['speedScore']:.2f}")
            print(f"안정성: {data['stabilityScore']:.2f}")
            print(f"내구성: {data['durabilityScore']:.2f}")
        print("-" * 50)
    
    def generate_switch_data(self) -> List[Dict]:
        """실제 스위치 데이터만 반환"""
        switches = []
        
        # 실제 스위치 데이터만 사용
        for name, switch in self.real_switches.items():
            switch_copy = switch.copy()
            switch_copy['name'] = name
            switches.append(switch_copy)
        
        return switches
    
    def generate_sound_profile_data(self, n_samples: int = 100) -> List[Dict]:
        """사운드 프로파일 데이터 생성"""
        profiles = []
        
        for i in range(n_samples):
            profile_type = np.random.choice(['silent', 'thocky', 'clacky'])
            rules = self.sound_rules[profile_type]
            
            profile = {
                'type': profile_type,
                'name': f'Profile_{i+1}'
            }
            
            # 각 구성 요소별 특성 생성
            for feature, (min_val, max_val) in rules.items():
                noise = np.random.normal(0, 0.05)
                value = np.clip(
                    np.random.uniform(min_val, max_val) + noise,
                    0.0, 1.0
                )
                profile[feature] = float(value)
            
            profiles.append(profile)
        
        return profiles
    
    def generate_preference_data(self, n_samples: int = 100) -> List[Dict]:
        """사용자 선호도 데이터 생성"""
        preferences = []
        
        # 다양한 사용자 프로필 정의
        profiles = {
            'gamer': {
                'tactilePref': (0.2, 0.5),
                'soundPref': (0.3, 0.7),
                'weightPref': (0.3, 0.6),
                'speedPref': (0.7, 1.0),
                'stabilityPref': (0.6, 0.9),
                'durabilityPref': (0.7, 1.0),
                'budgetPref': (0.4, 0.8),
                'aestheticsPref': (0.5, 0.9),
                'customizabilityPref': (0.4, 0.8),
                'ergonomicsPref': (0.6, 0.9)
            },
            'typist': {
                'tactilePref': (0.7, 1.0),
                'soundPref': (0.5, 0.9),
                'weightPref': (0.5, 0.8),
                'speedPref': (0.5, 0.8),
                'stabilityPref': (0.7, 1.0),
                'durabilityPref': (0.6, 0.9),
                'budgetPref': (0.5, 0.9),
                'aestheticsPref': (0.4, 0.8),
                'customizabilityPref': (0.5, 0.9),
                'ergonomicsPref': (0.7, 1.0)
            },
            'enthusiast': {
                'tactilePref': (0.6, 1.0),
                'soundPref': (0.7, 1.0),
                'weightPref': (0.6, 1.0),
                'speedPref': (0.5, 0.8),
                'stabilityPref': (0.8, 1.0),
                'durabilityPref': (0.8, 1.0),
                'budgetPref': (0.7, 1.0),
                'aestheticsPref': (0.8, 1.0),
                'customizabilityPref': (0.8, 1.0),
                'ergonomicsPref': (0.7, 1.0)
            }
        }
        
        for i in range(n_samples):
            profile_type = np.random.choice(['gamer', 'typist', 'enthusiast'])
            rules = profiles[profile_type]
            
            preference = {
                'type': profile_type,
                'userId': f'User_{i+1}'
            }
            
            for feature, (min_val, max_val) in rules.items():
                noise = np.random.normal(0, 0.05)
                value = np.clip(
                    np.random.uniform(min_val, max_val) + noise,
                    0.0, 1.0
                )
                preference[feature] = float(value)
            
            preferences.append(preference)
        
        return preferences
    
    def calculate_satisfaction_score(
        self,
        switch: Dict,
        sound_profile: Dict,
        preference: Dict
    ) -> float:
        """만족도 점수 계산"""
        # 스위치 점수
        switch_score = (
            switch['tactileScore'] * preference['tactilePref'] +
            switch['soundScore'] * preference['soundPref'] +
            switch['weightScore'] * preference['weightPref'] +
            switch['speedScore'] * preference['speedPref'] +
            switch['stabilityScore'] * preference['stabilityPref'] +
            switch['durabilityScore'] * preference['durabilityPref']
        ) / 6
        
        # 사운드 프로파일 점수
        sound_score = (
            sound_profile['dampingScore'] * preference['soundPref'] +
            sound_profile['absorptionScore'] * preference['soundPref']
        ) / 2
        
        # 최종 점수 계산 (0~1 사이)
        final_score = (switch_score * 0.6 + sound_score * 0.4)
        
        return float(final_score)
    
    def generate_dataset(
        self,
        n_samples: int = 100,
        output_dir: str = 'data'
    ):
        """전체 데이터셋 생성"""
        # 데이터 생성
        switches = self.generate_switch_data()  # 실제 스위치만 사용
        sound_profiles = self.generate_sound_profile_data(n_samples)
        preferences = self.generate_preference_data(n_samples)
        
        # 만족도 점수 계산
        satisfaction_scores = []
        for i in range(n_samples):
            switch = switches[i % len(switches)]  # 실제 스위치를 순환하며 사용
            score = self.calculate_satisfaction_score(
                switch,
                sound_profiles[i],
                preferences[i]
            )
            satisfaction_scores.append({
                'score': score,
                'switch_name': switch['name'],
                'sound_profile': sound_profiles[i]['type'],
                'user_type': preferences[i]['type']
            })
        
        # 데이터 저장
        os.makedirs(output_dir, exist_ok=True)
        
        pd.DataFrame(switches).to_csv(
            f'{output_dir}/switches.csv', index=False
        )
        pd.DataFrame(sound_profiles).to_csv(
            f'{output_dir}/sound_profiles.csv', index=False
        )
        pd.DataFrame(preferences).to_csv(
            f'{output_dir}/user_preferences.csv', index=False
        )
        pd.DataFrame(satisfaction_scores).to_csv(
            f'{output_dir}/satisfaction_scores.csv', index=False
        )
        
        print(f"데이터셋이 {output_dir} 디렉토리에 생성되었습니다.")
        print(f"실제 스위치 {len(switches)}개와 {n_samples}개의 사용자 데이터로 구성되었습니다.")

if __name__ == "__main__":
    # 데이터 생성기 초기화
    generator = KeyboardDataGenerator()
    
    # 현재 등록된 스위치 목록 출력
    generator.list_switches()
    
    # 새로운 스위치 추가 예시
    print("\n새로운 스위치 추가 예시:")
    generator.add_switch(
        name="Gateron Black Ink V2",
        switch_type="linear",
        linear_score=0.95,    # 매우 부드러운 선형성
        tactile_score=0.1,    # 거의 없는 촉각성
        sound_score=0.8,      # 깊은 소리
        weight_score=0.7,     # 70g 정도의 무게
        smoothness_score=0.95, # 매우 부드러움
        speed_score=0.8,      # 빠른 속도
        stability_score=0.85,  # 매우 안정적
        durability_score=0.75  # 좋은 내구성
    )
    
    # 업데이트된 스위치 목록 출력
    generator.list_switches()
    
    # 데이터셋 생성
    generator.generate_dataset(n_samples=100) 