from keyboard_recommender import KeyboardRecommender

def test_linear_preference():
    """리니어 스위치 선호 사용자 테스트"""
    recommender = KeyboardRecommender()
    
    preferences = {
        # 소리 관련
        'sound_profile': 3,     # 조용한 소리 선호
        'sound_score': 3,      
        'sound_level': 3,      
        
        # 가격 관련
        'price_tier': 2,       # 중급 제품 선호
        
        # 품질 관련
        'build_quality': 9,    # 높은 품질 선호
        'durability_score': 9, 
        
        # 타입 관련
        'type': 'linear',      
        'switch_type': 'linear',
        
        # 재질 관련
        'material': 'POM',     
        'stem_material': 'POM',
        
        # 특성 관련
        'smoothness_score': 9, # 매우 부드러운 타감 선호
        'speed_score': 8,      # 빠른 반응 선호
        'stability_score': 7,  # 안정적인 타감 선호
        'linear_score': 9,     # 매우 선형적인 타감 선호
        'tactile_score': 1,    # 촉각 선호도 매우 낮음
        
        # 물리적 특성
        'stiffness': 4,        # 낮은 강성 선호
        'flex': 8,            # 높은 유연성 선호
        'weight_score': 4,    # 가벼운 무게 선호
    }
    
    print("\n=== 리니어 스위치 선호 사용자 테스트 ===")
    recommendations = recommender.recommend_complete_set(preferences)
    print_recommendations(recommendations)

def test_tactile_preference():
    """택타일 스위치 선호 사용자 테스트"""
    recommender = KeyboardRecommender()
    
    preferences = {
        # 소리 관련
        'sound_profile': 7,     # 중간 소리 선호
        'sound_score': 7,      
        'sound_level': 7,      
        
        # 가격 관련
        'price_tier': 3,       # 상급 제품 선호
        
        # 품질 관련
        'build_quality': 9,    # 높은 품질 선호
        'durability_score': 9, 
        
        # 타입 관련
        'type': 'tactile',     
        'switch_type': 'tactile',
        
        # 재질 관련
        'material': 'POM',     
        'stem_material': 'POM',
        
        # 특성 관련
        'smoothness_score': 7, # 중간 부드러움 선호
        'speed_score': 6,      # 중간 속도 선호
        'stability_score': 8,  # 안정적인 타감 선호
        'linear_score': 2,     # 선형성 선호도 낮음
        'tactile_score': 9,    # 촉각 선호도 매우 높음
        
        # 물리적 특성
        'stiffness': 7,        # 높은 강성 선호
        'flex': 5,            # 중간 유연성 선호
        'weight_score': 7,    # 무거운 무게 선호
    }
    
    print("\n=== 택타일 스위치 선호 사용자 테스트 ===")
    recommendations = recommender.recommend_complete_set(preferences)
    print_recommendations(recommendations)

def test_silent_preference():
    """저소음 선호 사용자 테스트"""
    recommender = KeyboardRecommender()
    
    preferences = {
        # 소리 관련
        'sound_profile': 2,     # 매우 조용한 소리 선호
        'sound_score': 2,      
        'sound_level': 2,      
        
        # 가격 관련
        'price_tier': 4,       # 프리미엄 제품 선호
        
        # 품질 관련
        'build_quality': 9,    # 높은 품질 선호
        'durability_score': 9, 
        
        # 타입 관련
        'type': 'silent',      
        'switch_type': 'silent',
        
        # 재질 관련
        'material': 'POM',     
        'stem_material': 'POM',
        
        # 특성 관련
        'smoothness_score': 8, # 부드러운 타감 선호
        'speed_score': 7,      # 중간 속도 선호
        'stability_score': 9,  # 매우 안정적인 타감 선호
        'linear_score': 8,     # 선형성 선호
        'tactile_score': 2,    # 촉각 선호도 낮음
        
        # 물리적 특성
        'stiffness': 5,        # 중간 강성 선호
        'flex': 6,            # 중간 유연성 선호
        'weight_score': 5,    # 중간 무게 선호
    }
    
    print("\n=== 저소음 선호 사용자 테스트 ===")
    recommendations = recommender.recommend_complete_set(preferences)
    print_recommendations(recommendations)

def print_recommendations(recommendations):
    """추천 결과 출력"""
    if not recommendations:
        print("추천 결과를 가져올 수 없습니다.")
        return
        
    print("\n=== 기본 추천 결과 ===")
    print(recommendations['formatted_result'])
    
    print("\n=== AI 추천 설명 ===")
    print(recommendations['ai_explanation'])

if __name__ == "__main__":
    # 세 가지 다른 선호도로 테스트 실행
    test_linear_preference()
    test_tactile_preference()
    test_silent_preference() 