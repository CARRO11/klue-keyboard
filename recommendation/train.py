import pandas as pd
import numpy as np
from ai_model import KeyboardAI
from sklearn.model_selection import train_test_split
import json
import os

def load_training_data():
    """학습 데이터 로드"""
    # 스위치 데이터
    switches_df = pd.read_csv('data/switches.csv')
    
    # 사운드 프로파일 데이터
    sound_profiles_df = pd.read_csv('data/sound_profiles.csv')
    
    # 사용자 선호도 데이터
    preferences_df = pd.read_csv('data/user_preferences.csv')
    
    # 만족도 점수 (레이블)
    satisfaction_scores = pd.read_csv('data/satisfaction_scores.csv')
    
    return (
        switches_df.to_dict('records'),
        sound_profiles_df.to_dict('records'),
        preferences_df.to_dict('records'),
        satisfaction_scores['score'].tolist()
    )

def main():
    # AI 모델 초기화
    model = KeyboardAI()
    
    # 데이터 로드
    switches, sound_profiles, preferences, labels = load_training_data()
    
    # 데이터 분할
    (
        train_switches,
        val_switches,
        train_sound_profiles,
        val_sound_profiles,
        train_preferences,
        val_preferences,
        train_labels,
        val_labels
    ) = train_test_split(
        switches,
        sound_profiles,
        preferences,
        labels,
        test_size=0.2,
        random_state=42
    )
    
    # 모델 학습
    print("모델 학습 시작...")
    model.train(
        switches=train_switches,
        sound_profiles=train_sound_profiles,
        preferences=train_preferences,
        labels=train_labels,
        epochs=100,
        batch_size=32
    )
    
    # 검증 세트에서 성능 평가
    print("\n검증 세트에서 성능 평가 중...")
    val_switch_embeddings = np.vstack([model.encode_switch(s) for s in val_switches])
    val_sound_embeddings = np.vstack([model.encode_sound_profile(s) for s in val_sound_profiles])
    val_pref_embeddings = np.vstack([model.encode_preferences(p) for p in val_preferences])
    
    val_predictions = model.recommendation_model.predict(
        [val_switch_embeddings, val_sound_embeddings, val_pref_embeddings]
    ).flatten()
    
    mse = np.mean((val_predictions - val_labels) ** 2)
    mae = np.mean(np.abs(val_predictions - val_labels))
    
    print(f"검증 세트 MSE: {mse:.4f}")
    print(f"검증 세트 MAE: {mae:.4f}")
    
    # 모델 저장
    print("\n모델 저장 중...")
    os.makedirs('models', exist_ok=True)
    model.save_models('models')
    print("모델 저장 완료!")

if __name__ == "__main__":
    main() 