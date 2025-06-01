from data_generator import KeyboardDataGenerator

def get_float_input(prompt: str, min_val: float = 0.0, max_val: float = 1.0) -> float:
    """사용자로부터 float 값을 입력받는 함수"""
    while True:
        try:
            value = float(input(prompt))
            if min_val <= value <= max_val:
                return value
            print(f"값은 {min_val}에서 {max_val} 사이여야 합니다.")
        except ValueError:
            print("숫자를 입력해주세요.")

def get_switch_type() -> str:
    """스위치 타입을 입력받는 함수"""
    valid_types = ['linear', 'tactile', 'clicky']
    while True:
        print("\n스위치 타입을 선택해주세요:")
        for i, t in enumerate(valid_types, 1):
            print(f"{i}. {t}")
        
        try:
            choice = int(input("번호를 선택하세요 (1-3): "))
            if 1 <= choice <= 3:
                return valid_types[choice - 1]
            print("1에서 3 사이의 번호를 선택해주세요.")
        except ValueError:
            print("올바른 번호를 입력해주세요.")

def main():
    generator = KeyboardDataGenerator()
    
    print("\n=== 현재 등록된 스위치 목록 ===")
    generator.list_switches()
    
    print("\n=== 새로운 스위치 추가 ===")
    print("각 특성에 대해 0에서 1 사이의 값을 입력해주세요.")
    print("0: 매우 낮음, 0.5: 보통, 1: 매우 높음")
    
    try:
        # 스위치 이름 입력
        name = input("\n스위치 이름을 입력하세요: ")
        
        # 스위치 타입 선택
        switch_type = get_switch_type()
        
        # 각 특성 값 입력
        scores = {
            'linear_score': get_float_input("선형성 점수 (0-1): "),
            'tactile_score': get_float_input("촉각성 점수 (0-1): "),
            'sound_score': get_float_input("소리 점수 (0-1): "),
            'weight_score': get_float_input("무게 점수 (0-1): "),
            'smoothness_score': get_float_input("부드러움 점수 (0-1): "),
            'speed_score': get_float_input("속도 점수 (0-1): "),
            'stability_score': get_float_input("안정성 점수 (0-1): "),
            'durability_score': get_float_input("내구성 점수 (0-1): ")
        }
        
        # 스위치 추가
        generator.add_switch(
            name=name,
            switch_type=switch_type,
            **scores
        )
        
        # 업데이트된 목록 출력
        print("\n=== 업데이트된 스위치 목록 ===")
        generator.list_switches()
        
        # 데이터셋 다시 생성
        print("\n데이터셋을 업데이트하는 중...")
        generator.generate_dataset(n_samples=100)
        print("데이터셋이 성공적으로 업데이트되었습니다.")
        
    except ValueError as e:
        print(f"\n오류 발생: {e}")
    except KeyboardInterrupt:
        print("\n\n작업이 취소되었습니다.")

if __name__ == "__main__":
    main() 