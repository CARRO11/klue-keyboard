import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, Model
from sklearn.preprocessing import StandardScaler
import joblib
from typing import Dict, List, Tuple, Optional

class KeyboardAI:
    def __init__(self):
        self.switch_encoder = self._build_switch_encoder()
        self.sound_encoder = self._build_sound_encoder()
        self.preference_encoder = self._build_preference_encoder()
        self.recommendation_model = self._build_recommendation_model()
        self.scaler = StandardScaler()
        
    def _build_switch_encoder(self) -> Model:
        """스위치 특성을 인코딩하는 신경망"""
        input_features = layers.Input(shape=(8,))  # 스위치의 8가지 특성
        
        # 특성 임베딩
        x = layers.Dense(32, activation='relu')(input_features)
        x = layers.BatchNormalization()(x)
        x = layers.Dropout(0.2)(x)
        
        # 특성 압축
        x = layers.Dense(16, activation='relu')(x)
        x = layers.BatchNormalization()(x)
        
        # 최종 임베딩
        switch_embedding = layers.Dense(8, activation='relu', name='switch_embedding')(x)
        
        return Model(input_features, switch_embedding, name='switch_encoder')
        
    def _build_sound_encoder(self) -> Model:
        """사운드 프로파일 특성을 인코딩하는 신경망"""
        input_features = layers.Input(shape=(6,))  # 사운드 관련 6가지 특성
        
        x = layers.Dense(16, activation='relu')(input_features)
        x = layers.BatchNormalization()(x)
        x = layers.Dropout(0.2)(x)
        
        sound_embedding = layers.Dense(8, activation='relu', name='sound_embedding')(x)
        
        return Model(input_features, sound_embedding, name='sound_encoder')
        
    def _build_preference_encoder(self) -> Model:
        """사용자 선호도를 인코딩하는 신경망"""
        input_features = layers.Input(shape=(10,))  # 사용자 선호도 10가지 특성
        
        x = layers.Dense(32, activation='relu')(input_features)
        x = layers.BatchNormalization()(x)
        x = layers.Dropout(0.2)(x)
        
        x = layers.Dense(16, activation='relu')(x)
        x = layers.BatchNormalization()(x)
        
        preference_embedding = layers.Dense(8, activation='relu', name='preference_embedding')(x)
        
        return Model(input_features, preference_embedding, name='preference_encoder')
        
    def _build_recommendation_model(self) -> Model:
        """최종 추천 모델"""
        # 각 인코더의 출력을 입력으로 받음
        switch_embedding = layers.Input(shape=(8,))
        sound_embedding = layers.Input(shape=(8,))
        preference_embedding = layers.Input(shape=(8,))
        
        # 임베딩 결합
        combined = layers.Concatenate()([switch_embedding, sound_embedding, preference_embedding])
        
        # 추천 점수 계산
        x = layers.Dense(32, activation='relu')(combined)
        x = layers.BatchNormalization()(x)
        x = layers.Dropout(0.3)(x)
        
        x = layers.Dense(16, activation='relu')(x)
        x = layers.BatchNormalization()(x)
        
        # 최종 추천 점수 (0~1 사이)
        recommendation_score = layers.Dense(1, activation='sigmoid', name='recommendation_score')(x)
        
        return Model(
            [switch_embedding, sound_embedding, preference_embedding],
            recommendation_score,
            name='recommendation_model'
        )
    
    def encode_switch(self, switch_data: Dict) -> np.ndarray:
        """스위치 데이터를 인코딩"""
        features = np.array([
            switch_data.get('linearScore', 0),
            switch_data.get('tactileScore', 0),
            switch_data.get('soundScore', 0),
            switch_data.get('weightScore', 0),
            switch_data.get('smoothnessScore', 0),
            switch_data.get('speedScore', 0),
            switch_data.get('stabilityScore', 0),
            switch_data.get('durabilityScore', 0)
        ]).reshape(1, -1)
        
        return self.switch_encoder.predict(features)
        
    def encode_sound_profile(self, components: Dict) -> np.ndarray:
        """사운드 프로파일 관련 부품들의 특성을 인코딩"""
        features = np.array([
            components.get('foam', {}).get('dampingScore', 0),
            components.get('foam', {}).get('densityScore', 0),
            components.get('gasket', {}).get('flexibilityScore', 0),
            components.get('gasket', {}).get('thicknessScore', 0),
            components.get('dampener', {}).get('absorptionScore', 0),
            components.get('dampener', {}).get('resonanceScore', 0)
        ]).reshape(1, -1)
        
        return self.sound_encoder.predict(features)
        
    def encode_preferences(self, preferences: Dict) -> np.ndarray:
        """사용자 선호도를 인코딩"""
        features = np.array([
            preferences.get('tactilePref', 0),
            preferences.get('soundPref', 0),
            preferences.get('weightPref', 0),
            preferences.get('speedPref', 0),
            preferences.get('stabilityPref', 0),
            preferences.get('durabilityPref', 0),
            preferences.get('budgetPref', 0),
            preferences.get('aestheticsPref', 0),
            preferences.get('customizabilityPref', 0),
            preferences.get('ergonomicsPref', 0)
        ]).reshape(1, -1)
        
        return self.preference_encoder.predict(features)
    
    def get_recommendation_score(
        self,
        switch_embedding: np.ndarray,
        sound_embedding: np.ndarray,
        preference_embedding: np.ndarray
    ) -> float:
        """주어진 임베딩들에 대한 추천 점수 계산"""
        return float(self.recommendation_model.predict(
            [switch_embedding, sound_embedding, preference_embedding]
        )[0][0])
    
    def train(
        self,
        switches: List[Dict],
        sound_profiles: List[Dict],
        preferences: List[Dict],
        labels: List[float],
        epochs: int = 100,
        batch_size: int = 32
    ):
        """모델 학습"""
        # 데이터 전처리
        switch_embeddings = np.vstack([self.encode_switch(s) for s in switches])
        sound_embeddings = np.vstack([self.encode_sound_profile(s) for s in sound_profiles])
        preference_embeddings = np.vstack([self.encode_preferences(p) for p in preferences])
        
        # 레이블 변환
        labels = np.array(labels)
        
        # 모델 학습
        self.recommendation_model.fit(
            [switch_embeddings, sound_embeddings, preference_embeddings],
            labels,
            epochs=epochs,
            batch_size=batch_size,
            validation_split=0.2,
            callbacks=[
                tf.keras.callbacks.EarlyStopping(
                    monitor='val_loss',
                    patience=10,
                    restore_best_weights=True
                )
            ]
        )
    
    def save_models(self, path: str):
        """모델들을 저장"""
        self.switch_encoder.save(f"{path}/switch_encoder")
        self.sound_encoder.save(f"{path}/sound_encoder")
        self.preference_encoder.save(f"{path}/preference_encoder")
        self.recommendation_model.save(f"{path}/recommendation_model")
        joblib.dump(self.scaler, f"{path}/scaler.pkl")
    
    def load_models(self, path: str):
        """저장된 모델들을 로드"""
        self.switch_encoder = tf.keras.models.load_model(f"{path}/switch_encoder")
        self.sound_encoder = tf.keras.models.load_model(f"{path}/sound_encoder")
        self.preference_encoder = tf.keras.models.load_model(f"{path}/preference_encoder")
        self.recommendation_model = tf.keras.models.load_model(f"{path}/recommendation_model")
        self.scaler = joblib.load(f"{path}/scaler.pkl") 