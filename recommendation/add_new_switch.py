from data_generator import KeyboardDataGenerator

def main():
    # 데이터 생성기 초기화
    generator = KeyboardDataGenerator()
    
    # 현재 등록된 스위치 목록 출력
    print("\n현재 등록된 스위치:")
    generator.list_switches()
    
    # 새로운 스위치 추가
    try:
        # 예시: Kailh Box White 스위치 추가
        generator.add_switch(
            name="Kailh Box White",
            switch_type="clicky",
            linear_score=0.3,     # 낮은 선형성
            tactile_score=0.85,   # 높은 촉각성
            sound_score=0.9,      # 매우 큰 소리
            weight_score=0.6,     # 중간 무게 (50g)
            smoothness_score=0.7, # 부드러움
            speed_score=0.75,    # 빠른 속도
            stability_score=0.8,  # 매우 안정적
            durability_score=0.85 # 높은 내구성
        )
        
        # 업데이트된 스위치 목록 출력
        print("\n업데이트된 스위치 목록:")
        generator.list_switches()
        
        # 데이터셋 다시 생성 (선택사항)
        generator.generate_dataset(n_samples=100)
        print("\n새로운 데이터셋이 생성되었습니다.")
        
    except ValueError as e:
        print(f"오류 발생: {e}")

if __name__ == "__main__":
    main() 