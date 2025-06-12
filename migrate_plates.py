import requests
import mysql.connector
import json

# 로컬 MySQL 연결
local_db = mysql.connector.connect(
    host='localhost',
    user='root',
    password='shin',
    database='klue_keyboard'
)

# Railway API 엔드포인트
RAILWAY_API_BASE = 'https://klue-keyboard-production.up.railway.app/api'

def get_local_plates():
    """로컬 MySQL에서 Plate 데이터 가져오기"""
    cursor = local_db.cursor(dictionary=True)
    cursor.execute("SELECT * FROM plate")
    plates = cursor.fetchall()
    cursor.close()
    return plates

def get_railway_keyboard_cases():
    """Railway에서 KeyboardCase 목록 가져오기"""
    try:
        response = requests.get(f'{RAILWAY_API_BASE}/cases')
        if response.status_code == 200:
            cases = response.json()
            # API가 배열을 직접 반환함
            return {case['name']: case['id'] for case in cases}
        return {}
    except Exception as e:
        print(f"KeyboardCase 조회 실패: {e}")
        return {}

def create_plate(plate_data):
    """Railway에 Plate 생성"""
    try:
        response = requests.post(f'{RAILWAY_API_BASE}/plates', json=plate_data)
        if response.status_code == 200 or response.status_code == 201:
            print(f"✅ Plate 생성 성공: {plate_data['name']}")
            return True
        else:
            print(f"❌ Plate 생성 실패: {plate_data['name']} - {response.status_code}")
            print(f"Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Plate 생성 중 오류: {plate_data['name']} - {e}")
        return False

def main():
    print("🔄 Plate 데이터 마이그레이션 시작...")
    
    # Railway KeyboardCase 목록 가져오기
    print("KeyboardCase 목록 조회 중...")
    railway_cases = get_railway_keyboard_cases()
    print(f"Railway에서 {len(railway_cases)}개 KeyboardCase 발견")
    print(f"KeyboardCase 목록: {list(railway_cases.keys())}")
    
    # 로컬 Plate 데이터 가져오기
    print("로컬 Plate 데이터 조회 중...")
    local_plates = get_local_plates()
    print(f"로컬에서 {len(local_plates)}개 Plate 발견")
    
    if not railway_cases:
        print("❌ Railway KeyboardCase를 찾을 수 없습니다. 마이그레이션을 중단합니다.")
        return
    
    success_count = 0
    fail_count = 0
    
    # Railway에 있는 첫 번째 KeyboardCase를 기본값으로 사용
    default_case_id = list(railway_cases.values())[0]
    default_case_name = list(railway_cases.keys())[0]
    
    for i, plate in enumerate(local_plates):
        # Plate 데이터 준비 (로컬 DB 구조에 맞게)
        description = f"Material: {plate.get('material', 'Unknown')}, Size: {plate.get('size', 'Unknown')}"
        if plate.get('Compatibility'):
            description += f", Compatibility: {plate['Compatibility']}"
        if plate.get('thickness'):
            description += f", Thickness: {plate['thickness']}mm"
        
        plate_data = {
            'name': plate['name'],
            'description': description,
            'keyboardCase': {
                'id': default_case_id  # 모든 플레이트를 첫 번째 케이스에 연결
            }
        }
        
        print(f"🔄 [{i+1}/{len(local_plates)}] Plate 생성 중: {plate['name']}")
        
        # Plate 생성
        if create_plate(plate_data):
            success_count += 1
        else:
            fail_count += 1
    
    print(f"\n📊 마이그레이션 완료:")
    print(f"✅ 성공: {success_count}개")
    print(f"❌ 실패: {fail_count}개")
    print(f"📌 모든 플레이트가 '{default_case_name}' 케이스에 연결되었습니다.")
    
    local_db.close()

if __name__ == "__main__":
    main() 