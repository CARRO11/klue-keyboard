import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.metrics.pairwise import cosine_similarity
import tensorflow as tf
from tensorflow.keras import layers, Model
import joblib
import json
from typing import Dict, Optional, List

class KeyboardRecommender:
    def __init__(self):
        self.switch_encoder = self._build_switch_encoder()
        self.keycap_encoder = self._build_keycap_encoder()
        self.sound_encoder = self._build_sound_encoder()
        self.scaler = StandardScaler()
        
        # 스위치 특성별 가중치 정의
        self.switch_weights = {
            'typing_feel': {
                'tactileScore': 0.4,
                'smoothnessScore': 0.3,
                'weightScore': 0.3
            },
            'sound': {
                'soundScore': 0.6,
                'tactileScore': 0.2,
                'smoothnessScore': 0.2
            },
            'gaming': {
                'linearScore': 0.4,
                'smoothnessScore': 0.4,
                'weightScore': 0.2
            }
        }

        # 사운드 프로파일 정의
        self.sound_profiles = {
            'silent': {
                'foam_materials': ['eva', 'sorbothane'],
                'gasket_materials': ['silicone', 'poron'],
                'dampener_materials': ['silicone', 'pe foam']
            },
            'thocky': {
                'foam_materials': ['pe foam', 'eva'],
                'gasket_materials': ['poron', 'epdm'],
                'dampener_materials': ['pe foam', 'felt']
            },
            'clacky': {
                'foam_materials': ['thin pe', 'none'],
                'gasket_materials': ['thin poron', 'fr4'],
                'dampener_materials': ['none', 'thin pe']
            },
            'poppy': {
                'foam_materials': ['pe foam', 'thin eva'],
                'gasket_materials': ['poron', 'epdm'],
                'dampener_materials': ['pe foam', 'thin silicone']
            }
        }
        
    def _build_switch_encoder(self):
        # 스위치 특성을 인코딩하는 신경망
        input_layer = layers.Input(shape=(5,))  # linearScore, tactileScore, soundScore, weightScore, smoothnessScore
        x = layers.Dense(32, activation='relu')(input_layer)
        x = layers.Dense(16, activation='relu')(x)
        encoded = layers.Dense(8, activation='relu')(x)
        
        return Model(input_layer, encoded)
        
    def _build_keycap_encoder(self):
        # 키캡 특성을 인코딩하는 신경망
        input_layer = layers.Input(shape=(4,))  # profile, material, thickness, compatibility
        x = layers.Dense(16, activation='relu')(input_layer)
        encoded = layers.Dense(8, activation='relu')(x)
        
        return Model(input_layer, encoded)
        
    def _build_sound_encoder(self):
        # 사운드 프로파일 특성을 인코딩하는 신경망
        input_layer = layers.Input(shape=(3,))  # foam, gasket, dampener materials
        x = layers.Dense(16, activation='relu')(input_layer)
        encoded = layers.Dense(8, activation='relu')(x)
        
        return Model(input_layer, encoded)

    def encode_switch_features(self, switch_data):
        features = np.array([
            switch_data['linearScore'],
            switch_data['tactileScore'],
            switch_data['soundScore'],
            switch_data['weightScore'],
            switch_data['smoothnessScore']
        ]).reshape(1, -1)
        
        return self.switch_encoder.predict(features)

    def encode_keycap_features(self, keycap_data):
        # 키캡 프로파일 원-핫 인코딩
        profiles = {'cherry': [1,0,0], 'oem': [0,1,0], 'sa': [0,0,1]}
        profile_encoded = profiles.get(keycap_data['profile'].lower(), [0,0,0])
        
        # 재질 원-핫 인코딩
        materials = {'pbt': [1,0], 'abs': [0,1]}
        material_encoded = materials.get(keycap_data['material'].lower(), [0,0])
        
        features = np.array(profile_encoded + material_encoded).reshape(1, -1)
        return self.keycap_encoder.predict(features)

    def calculate_sound_compatibility(self, components, target_sound):
        target_profile = self.sound_profiles.get(target_sound.lower(), {})
        if not target_profile:
            return 0.0
            
        score = 0.0
        if components.get('foam', {}).get('material', '').lower() in target_profile['foam_materials']:
            score += 0.33
        if components.get('gasket', {}).get('material', '').lower() in target_profile['gasket_materials']:
            score += 0.33
        if components.get('dampener', {}).get('material', '').lower() in target_profile['dampener_materials']:
            score += 0.34
            
        return score

    def recommend_by_condition(self, condition: str, available_components: Dict) -> Dict:
        """
        특정 조건에 따라 부품을 추천합니다.
        
        :param condition: 추천 조건 (예: "타건감이 좋은", "게이밍용", "조용한" 등)
        :param available_components: 사용 가능한 부품 목록
        :return: 추천 결과
        """
        condition = condition.lower()
        recommendations = {}
        reasons = []

        # 타건감 관련 조건
        if any(keyword in condition for keyword in ['타건감', '타이핑']):
            switch = self._recommend_switch_by_typing_feel(available_components.get('switches', []))
            if switch:
                recommendations['switch'] = switch
                reasons.append(f"선택된 스위치 ({switch['name']})는 뛰어난 타건감을 제공합니다. "
                             f"촉각적 피드백이 {switch.get('tactileScore', 0)*100:.0f}점, "
                             f"부드러움이 {switch.get('smoothnessScore', 0)*100:.0f}점으로 "
                             f"타이핑에 최적화되어 있습니다.")

        # 게이밍 관련 조건
        elif any(keyword in condition for keyword in ['게이밍', '게임']):
            switch = self._recommend_switch_for_gaming(available_components.get('switches', []))
            if switch:
                recommendations['switch'] = switch
                reasons.append(f"선택된 스위치 ({switch['name']})는 게이밍에 적합합니다. "
                             f"선형성이 {switch.get('linearScore', 0)*100:.0f}점, "
                             f"부드러움이 {switch.get('smoothnessScore', 0)*100:.0f}점으로 "
                             f"빠른 키 입력이 가능합니다.")

        # 사운드 관련 조건
        elif any(keyword in condition for keyword in ['소리', '사운드', '조용한', '톡톡한']):
            sound_type = self._determine_sound_type(condition)
            components = self._recommend_components_by_sound(
                available_components,
                sound_type
            )
            recommendations.update(components)
            
            if 'switch' in components:
                switch = components['switch']
                reasons.append(f"선택된 스위치 ({switch['name']})는 {sound_type} 사운드 프로파일에 "
                             f"적합하며, 소리 품질이 {switch.get('soundScore', 0)*100:.0f}점입니다.")
            
            if 'foam' in components:
                foam = components['foam']
                reasons.append(f"선택된 폼 ({foam['material']})은 {sound_type} 사운드를 구현하는 데 "
                             f"도움이 됩니다.")

        return {
            'recommendations': recommendations,
            'reason': "\n".join(reasons)
        }

    def _recommend_switch_by_typing_feel(self, switches: List[Dict]) -> Optional[Dict]:
        """타건감 기준으로 스위치를 추천합니다."""
        if not switches:
            return None

        scores = []
        weights = self.switch_weights['typing_feel']
        
        for switch in switches:
            score = (
                switch.get('tactileScore', 0) * weights['tactileScore'] +
                switch.get('smoothnessScore', 0) * weights['smoothnessScore'] +
                switch.get('weightScore', 0) * weights['weightScore']
            )
            scores.append((switch, score))

        return max(scores, key=lambda x: x[1])[0] if scores else None

    def _recommend_switch_for_gaming(self, switches: List[Dict]) -> Optional[Dict]:
        """게이밍용 스위치를 추천합니다."""
        if not switches:
            return None

        scores = []
        weights = self.switch_weights['gaming']
        
        for switch in switches:
            score = (
                switch.get('linearScore', 0) * weights['linearScore'] +
                switch.get('smoothnessScore', 0) * weights['smoothnessScore'] +
                switch.get('weightScore', 0) * weights['weightScore']
            )
            scores.append((switch, score))

        return max(scores, key=lambda x: x[1])[0] if scores else None

    def _determine_sound_type(self, condition: str) -> str:
        """조건에서 원하는 사운드 타입을 결정합니다."""
        if '조용한' in condition or '무소음' in condition:
            return 'silent'
        elif '톡톡한' in condition or '경쾌한' in condition:
            return 'clacky'
        elif '둔탁한' in condition or '묵직한' in condition:
            return 'thocky'
        elif '팝팝한' in condition:
            return 'poppy'
        return 'thocky'  # 기본값

    def _recommend_components_by_sound(self, available_components: Dict, sound_type: str) -> Dict:
        """사운드 프로파일에 맞는 부품들을 추천합니다."""
        recommendations = {}
        
        # 스위치 추천
        switches = available_components.get('switches', [])
        if switches:
            switch_scores = [(switch, switch.get('soundScore', 0)) for switch in switches]
            recommendations['switch'] = max(switch_scores, key=lambda x: x[1])[0]

        # 폼 추천
        foams = available_components.get('foams', [])
        if foams:
            target_materials = self.sound_profiles[sound_type]['foam_materials']
            for foam in foams:
                if foam['material'].lower() in target_materials:
                    recommendations['foam'] = foam
                    break

        return recommendations

if __name__ == "__main__":
    # 모델 초기화
    recommender = KeyboardRecommender()
    
    # 입력 데이터 읽기
    input_data = json.loads(input())
    
    # 추천 실행
    result = recommender.recommend_by_condition(
        input_data['condition'],
        input_data['available_components']
    )
    
    # 결과 출력
    print(json.dumps(result, ensure_ascii=False)) 