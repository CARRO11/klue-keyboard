from typing import List, Dict
import os
import pandas as pd

class DataGenerator:
    def generate_switch_data(self, n_samples: int = 100) -> List[Dict]:
        """스위치 데이터 생성"""
        switches = []
        
        # 실제 스위치 데이터만 사용
        for name, switch in self.real_switches.items():
            switch_copy = switch.copy()
            switch_copy['name'] = name
            switches.append(switch_copy)
        
        return switches

    def generate_dataset(
        self,
        n_samples: int = 100,
        output_dir: str = 'data'
    ):
        """전체 데이터셋 생성"""
        # 데이터 생성
        switches = self.generate_switch_data()  # n_samples 파라미터 제거
        sound_profiles = self.generate_sound_profile_data(n_samples)
        preferences = self.generate_preference_data(n_samples)
        
        # 만족도 점수 계산
        satisfaction_scores = []
        for i in range(n_samples):
            score = self.calculate_satisfaction_score(
                switches[i % len(switches)],  # 실제 스위치를 순환하며 사용
                sound_profiles[i],
                preferences[i]
            )
            satisfaction_scores.append({
                'score': score,
                'switch_name': switches[i % len(switches)]['name'],  # 스위치 이름 추가
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