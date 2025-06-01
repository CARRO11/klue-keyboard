from keyboard_components import KeyboardComponentGenerator
from typing import List, Dict, Any, Callable
import sys

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

def get_bool_input(prompt: str) -> bool:
    """사용자로부터 예/아니오 입력받는 함수"""
    while True:
        response = input(f"{prompt} (y/n): ").lower()
        if response in ['y', 'yes']:
            return True
        elif response in ['n', 'no']:
            return False
        print("'y' 또는 'n'으로 답해주세요.")

def get_string_input(prompt: str, options: List[str] = None) -> str:
    """사용자로부터 문자열 입력받는 함수"""
    while True:
        value = input(prompt).strip()
        if not value:
            print("값을 입력해주세요.")
            continue
        if options and value not in options:
            print(f"다음 중 하나를 선택해주세요: {', '.join(options)}")
            continue
        return value

def get_int_input(prompt: str, min_val: int = None, max_val: int = None) -> int:
    """사용자로부터 정수 값을 입력받는 함수"""
    while True:
        try:
            value = int(input(prompt))
            if (min_val is None or value >= min_val) and (max_val is None or value <= max_val):
                return value
            print(f"값은 {min_val}에서 {max_val} 사이여야 합니다.")
        except ValueError:
            print("올바른 숫자를 입력해주세요.")

def get_list_input(prompt: str) -> List[str]:
    """사용자로부터 쉼표로 구분된 리스트를 입력받는 함수"""
    while True:
        value = input(prompt).strip()
        if not value:
            print("최소한 하나의 값을 입력해주세요.")
            continue
        return [item.strip() for item in value.split(',')]

def add_housing(generator: KeyboardComponentGenerator):
    """하우징 추가"""
    print("\n=== 새로운 하우징 추가 ===")
    
    # 기본 정보 입력
    name = get_string_input("하우징 이름: ")
    housing_type = get_string_input(
        "하우징 타입 (aluminum/plastic/acrylic): ",
        ['aluminum', 'plastic', 'acrylic']
    )
    mounting = get_string_input(
        "마운팅 방식 (tray_mount/gasket_mount/top_mount): ",
        ['tray_mount', 'gasket_mount', 'top_mount']
    )
    
    # 수치 정보 입력
    weight = get_float_input("무게감 (0-1): ")
    acoustics = get_float_input("음향 특성 (0-1): ")
    build_quality = get_float_input("빌드 품질 (0-1): ")
    price_tier = get_float_input("가격대 (0-1): ")
    
    # 기타 정보 입력
    rgb_support = get_bool_input("RGB 지원 여부")
    angle = get_int_input("타이핑 각도 (도): ", 0, 15)
    size = get_string_input(
        "키보드 크기 (60%/65%/75%/TKL/Full): ",
        ['60%', '65%', '75%', 'TKL', 'Full']
    )
    
    # 하우징 추가
    generator.add_housing(
        name=name,
        housing_type=housing_type,
        mounting=mounting,
        weight=weight,
        acoustics=acoustics,
        build_quality=build_quality,
        price_tier=price_tier,
        rgb_support=rgb_support,
        angle=angle,
        size=size
    )

def add_plate(generator: KeyboardComponentGenerator):
    """플레이트 추가"""
    print("\n=== 새로운 플레이트 추가 ===")
    
    # 기본 정보 입력
    name = get_string_input("플레이트 이름: ")
    material = get_string_input(
        "재질 (aluminum/brass/fr4/pom/carbon_fiber): ",
        ['aluminum', 'brass', 'fr4', 'pom', 'carbon_fiber']
    )
    
    # 수치 정보 입력
    stiffness = get_float_input("강성 (0-1): ")
    sound_profile = get_float_input("소리 특성 (0-1): ")
    price_tier = get_float_input("가격대 (0-1): ")
    weight = get_float_input("무게 (0-1): ")
    flex = get_float_input("유연성 (0-1): ")
    
    # 플레이트 추가
    generator.add_plate(
        name=name,
        material=material,
        stiffness=stiffness,
        sound_profile=sound_profile,
        price_tier=price_tier,
        weight=weight,
        flex=flex
    )

def add_pcb(generator: KeyboardComponentGenerator):
    """PCB 추가"""
    print("\n=== 새로운 PCB 추가 ===")
    
    # 기본 정보 입력
    name = get_string_input("PCB 이름: ")
    pcb_type = get_string_input(
        "PCB 타입 (hotswap/solder): ",
        ['hotswap', 'solder']
    )
    
    # 지원 기능 입력
    rgb_support = get_bool_input("RGB 지원 여부")
    qmk_via = get_bool_input("QMK/VIA 지원 여부")
    
    # 수치 정보 입력
    flex = get_float_input("기판 유연성 (0-1): ")
    price_tier = get_float_input("가격대 (0-1): ")
    build_quality = get_float_input("빌드 품질 (0-1): ")
    
    # 특징 목록 입력
    print("\n지원하는 기능들을 쉼표로 구분하여 입력하세요")
    print("예: rgb_per_key,usb_c,esd_protection")
    features = get_list_input("지원 기능: ")
    
    # PCB 추가
    generator.add_pcb(
        name=name,
        pcb_type=pcb_type,
        rgb_support=rgb_support,
        qmk_via=qmk_via,
        flex=flex,
        price_tier=price_tier,
        build_quality=build_quality,
        features=features
    )

def add_stabilizer(generator: KeyboardComponentGenerator):
    """스태빌라이저 추가"""
    print("\n=== 새로운 스태빌라이저 추가 ===")
    
    # 기본 정보 입력
    name = get_string_input("스태빌라이저 이름: ")
    stab_type = get_string_input(
        "타입 (screw_in/clip_in/plate_mount): ",
        ['screw_in', 'clip_in', 'plate_mount']
    )
    
    # 수치 정보 입력
    rattle = get_float_input("흔들림 (0-1, 낮을수록 좋음): ")
    smoothness = get_float_input("부드러움 (0-1): ")
    sound_profile = get_float_input("소리 특성 (0-1): ")
    price_tier = get_float_input("가격대 (0-1): ")
    build_quality = get_float_input("빌드 품질 (0-1): ")
    
    # 스태빌라이저 추가
    generator.add_stabilizer(
        name=name,
        stab_type=stab_type,
        rattle=rattle,
        smoothness=smoothness,
        sound_profile=sound_profile,
        price_tier=price_tier,
        build_quality=build_quality
    )

def view_components(generator: KeyboardComponentGenerator):
    """부품 목록 보기"""
    print("\n=== 부품 목록 보기 ===")
    print("1. 하우징")
    print("2. 플레이트")
    print("3. PCB")
    print("4. 스태빌라이저")
    print("0. 이전 메뉴로")
    
    try:
        choice = get_int_input("\n보고 싶은 부품의 번호를 입력하세요 (0-4): ", 0, 4)
        
        if choice == 0:
            return
        elif choice == 1:
            generator.list_components('housing')
        elif choice == 2:
            generator.list_components('plate')
        elif choice == 3:
            generator.list_components('pcb')
        elif choice == 4:
            generator.list_components('stabilizer')
        
        input("\n계속하려면 Enter를 누르세요...")
        
    except ValueError as e:
        print(f"\n오류 발생: {e}")
    except KeyboardInterrupt:
        print("\n\n작업이 취소되었습니다.")

def main():
    generator = KeyboardComponentGenerator()
    
    while True:
        print("\n=== 키보드 부품 추가 프로그램 ===")
        print("1. 하우징 추가")
        print("2. 플레이트 추가")
        print("3. PCB 추가")
        print("4. 스태빌라이저 추가")
        print("5. 부품 목록 보기")
        print("6. 데이터셋 생성")
        print("0. 종료")
        
        try:
            choice = get_int_input("\n원하는 작업의 번호를 입력하세요 (0-6): ", 0, 6)
            
            if choice == 0:
                print("\n프로그램을 종료합니다.")
                break
            elif choice == 1:
                add_housing(generator)
            elif choice == 2:
                add_plate(generator)
            elif choice == 3:
                add_pcb(generator)
            elif choice == 4:
                add_stabilizer(generator)
            elif choice == 5:
                view_components(generator)
            elif choice == 6:
                generator.generate_dataset()
            
        except KeyboardInterrupt:
            print("\n\n작업이 취소되었습니다.")
        except Exception as e:
            print(f"\n오류 발생: {e}")

if __name__ == "__main__":
    main() 