#!/usr/bin/env python3
"""
KLUE 키보드 데이터 마이그레이션 스크립트
로컬 MySQL 데이터베이스에서 Railway 데이터베이스로 데이터를 마이그레이션
"""

import mysql.connector
import requests
import json
from datetime import datetime
import time

# 설정
LOCAL_DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'shin',
    'database': 'klue_keyboard'
}

RAILWAY_API_BASE = 'https://klue-keyboard-production.up.railway.app/api'

def connect_local_db():
    """로컬 데이터베이스 연결"""
    try:
        conn = mysql.connector.connect(**LOCAL_DB_CONFIG)
        print("✅ 로컬 데이터베이스 연결 성공")
        return conn
    except Exception as e:
        print(f"❌ 로컬 데이터베이스 연결 실패: {e}")
        return None

def fetch_switches_data(conn):
    """로컬에서 스위치 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, type, pressure, lubrication, stem_material,
           linear_score, tactile_score, sound_score, link
    FROM Switches 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_switch_to_railway_format(switch_data):
    """로컬 스위치 데이터를 Railway 형식으로 변환"""
    return {
        "name": switch_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59", 
        "type": switch_data['type'],
        "pressure": switch_data['pressure'],
        "lubrication": switch_data['lubrication'],
        "stemMaterial": switch_data['stem_material'],
        "linearScore": float(switch_data['linear_score']) if switch_data['linear_score'] else 0.0,
        "tactileScore": float(switch_data['tactile_score']) if switch_data['tactile_score'] else 0.0,
        "soundScore": float(switch_data['sound_score']) if switch_data['sound_score'] else 0.0,
        "link": switch_data['link']
    }

def send_switch_to_railway(switch_data):
    """스위치 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/switches"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=switch_data, headers=headers)
        if response.status_code == 200:
            result = response.json()
            print(f"✅ 스위치 '{switch_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 스위치 '{switch_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_switches():
    """스위치 데이터 마이그레이션"""
    print("🔄 스위치 데이터 마이그레이션 시작...")
    
    # 로컬 DB 연결
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        # 로컬 스위치 데이터 가져오기
        switches = fetch_switches_data(conn)
        print(f"📊 로컬에서 {len(switches)}개 스위치 발견")
        
        success_count = 0
        
        for switch in switches:
            # Railway 형식으로 변환
            railway_switch = convert_switch_to_railway_format(switch)
            
            # Railway로 전송
            if send_switch_to_railway(railway_switch):
                success_count += 1
            
            # API 제한을 피하기 위해 잠시 대기
            time.sleep(0.5)
        
        print(f"🎉 스위치 마이그레이션 완료: {success_count}/{len(switches)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 스위치 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

def fetch_keycaps_data(conn):
    """로컬에서 키캡 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, thickness, profile, switch_compatibility, link
    FROM Keycap 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_keycap_to_railway_format(keycap_data):
    """로컬 키캡 데이터를 Railway 형식으로 변환"""
    return {
        "name": keycap_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": keycap_data['material'],
        "thickness": keycap_data['thickness'],
        "profile": keycap_data['profile'],
        "switchCompatibility": keycap_data['switch_compatibility'],
        "link": keycap_data['link']
    }

def send_keycap_to_railway(keycap_data):
    """키캡 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/keycaps"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=keycap_data, headers=headers)
        if response.status_code == 200:
            result = response.json()
            print(f"✅ 키캡 '{keycap_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 키캡 '{keycap_data['name']}' 생성 실패: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_keycaps():
    """키캡 데이터 마이그레이션"""
    print("🔄 키캡 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        keycaps = fetch_keycaps_data(conn)
        print(f"📊 로컬에서 {len(keycaps)}개 키캡 발견")
        
        success_count = 0
        for keycap in keycaps:
            railway_keycap = convert_keycap_to_railway_format(keycap)
            if send_keycap_to_railway(railway_keycap):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 키캡 마이그레이션 완료: {success_count}/{len(keycaps)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 키캡 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

def check_railway_status():
    """Railway API 상태 확인"""
    try:
        response = requests.get(f"{RAILWAY_API_BASE}/switches")
        if response.status_code == 200:
            data = response.json()
            print(f"🔍 Railway 현재 스위치 개수: {data.get('totalItems', 0)}")
            return True
        else:
            print(f"❌ Railway API 접근 실패: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Railway 연결 오류: {e}")
        return False

def fetch_stabilizers_data(conn):
    """로컬에서 스테빌라이저 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, size, link
    FROM Stabilizer 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_stabilizer_to_railway_format(stab_data):
    """로컬 스테빌라이저 데이터를 Railway 형식으로 변환"""
    return {
        "name": stab_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": stab_data['material'],
        "size": stab_data['size'],
        "link": stab_data['link']
    }

def send_stabilizer_to_railway(stab_data):
    """스테빌라이저 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/stabilizers"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=stab_data, headers=headers)
        if response.status_code == 200:
            result = response.json()
            print(f"✅ 스테빌라이저 '{stab_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 스테빌라이저 '{stab_data['name']}' 생성 실패: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_stabilizers():
    """스테빌라이저 데이터 마이그레이션"""
    print("🔄 스테빌라이저 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        stabilizers = fetch_stabilizers_data(conn)
        print(f"📊 로컬에서 {len(stabilizers)}개 스테빌라이저 발견")
        
        success_count = 0
        for stab in stabilizers:
            railway_stab = convert_stabilizer_to_railway_format(stab)
            if send_stabilizer_to_railway(railway_stab):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 스테빌라이저 마이그레이션 완료: {success_count}/{len(stabilizers)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 스테빌라이저 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

def main():
    """메인 실행 함수"""
    print("🚚 KLUE 키보드 데이터 마이그레이션 시작")
    print("=" * 50)
    
    # Railway API 상태 확인
    if not check_railway_status():
        print("❌ Railway API 연결 실패, 마이그레이션 중단")
        return
    
    # 스위치 마이그레이션
    if migrate_switches():
        print("✅ 스위치 마이그레이션 완료")
    else:
        print("❌ 스위치 마이그레이션 실패")
    
    print()
    
    # 키캡 마이그레이션
    if migrate_keycaps():
        print("✅ 키캡 마이그레이션 완료")
    else:
        print("❌ 키캡 마이그레이션 실패")
    
    print()
    
    # 스테빌라이저 마이그레이션
    if migrate_stabilizers():
        print("✅ 스테빌라이저 마이그레이션 완료")
    else:
        print("❌ 스테빌라이저 마이그레이션 실패")
    
    print()
    print("🎉 데이터 마이그레이션 완료!")
    
    # 최종 상태 확인
    check_railway_status()

if __name__ == "__main__":
    main() 