#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
KLUE 키보드 - 수정된 데이터 마이그레이션 스크립트 
정확한 컬럼명으로 PCB, 키보드케이스, 케이블 마이그레이션
"""

import mysql.connector
import requests
import json
import time
from datetime import datetime

# 연결 설정
LOCAL_DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'shin',
    'database': 'klue_keyboard'
}

RAILWAY_API_BASE = "https://klue-keyboard-production.up.railway.app/api"

def connect_local_db():
    """로컬 MySQL 데이터베이스 연결"""
    try:
        conn = mysql.connector.connect(**LOCAL_DB_CONFIG)
        print("✅ 로컬 MySQL 연결 성공")
        return conn
    except Exception as e:
        print(f"❌ 로컬 DB 연결 실패: {e}")
        return None

def check_railway_status():
    """Railway API 상태 확인"""
    try:
        endpoints = ['switches', 'keycaps', 'stabilizers', 'pcbs', 'keyboardcases', 'cables']
        
        print("🔍 Railway 현재 상태:")
        for endpoint in endpoints:
            try:
                response = requests.get(f"{RAILWAY_API_BASE}/{endpoint}")
                if response.status_code == 200:
                    data = response.json()
                    count = data.get('totalItems', len(data) if isinstance(data, list) else 0)
                    print(f"  - {endpoint}: {count}개")
                else:
                    print(f"  - {endpoint}: 접근 실패 ({response.status_code})")
            except:
                print(f"  - {endpoint}: 연결 실패")
        
        return True
    except Exception as e:
        print(f"❌ Railway 연결 오류: {e}")
        return False

# ===== PCB 마이그레이션 (수정된 컬럼명) =====
def fetch_pcb_data(conn):
    """로컬에서 PCB 데이터 가져오기 - 정확한 컬럼명 사용"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, layout, hotswap, wireless, rgb, usb_type, firmware_type, 
           rgb_support, qmk_via, flex, price_tier, build_quality, features, link
    FROM PCB 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_pcb_to_railway_format(pcb_data):
    """로컬 PCB 데이터를 Railway 형식으로 변환"""
    return {
        "name": pcb_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "layout": pcb_data['layout'],
        "hotswap": bool(pcb_data['hotswap']) if pcb_data['hotswap'] is not None else False,
        "wireless": bool(pcb_data['wireless']) if pcb_data['wireless'] is not None else False,
        "rgb": pcb_data['rgb'],
        "usbType": pcb_data['usb_type'],
        "firmwareType": pcb_data['firmware_type'],
        "rgbSupport": bool(pcb_data['rgb_support']) if pcb_data['rgb_support'] is not None else False,
        "qmkVia": bool(pcb_data['qmk_via']) if pcb_data['qmk_via'] is not None else False,
        "flex": float(pcb_data['flex']) if pcb_data['flex'] else 0.0,
        "priceTier": float(pcb_data['price_tier']) if pcb_data['price_tier'] else 0.0,
        "buildQuality": float(pcb_data['build_quality']) if pcb_data['build_quality'] else 0.0,
        "features": pcb_data['features'],
        "link": pcb_data['link']
    }

def send_pcb_to_railway(pcb_data):
    """PCB 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/pcbs"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=pcb_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ PCB '{pcb_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ PCB '{pcb_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_pcbs():
    """PCB 데이터 마이그레이션"""
    print("🔄 PCB 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        pcbs = fetch_pcb_data(conn)
        print(f"📊 로컬에서 {len(pcbs)}개 PCB 발견")
        
        success_count = 0
        for pcb in pcbs:
            railway_pcb = convert_pcb_to_railway_format(pcb)
            if send_pcb_to_railway(railway_pcb):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 PCB 마이그레이션 완료: {success_count}/{len(pcbs)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ PCB 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

# ===== 키보드케이스 마이그레이션 (수정된 컬럼명) =====
def fetch_keyboardcase_data(conn):
    """로컬에서 키보드케이스 데이터 가져오기 - 정확한 컬럼명 사용"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, type, mounting, weight, acoustics, build_quality, 
           price_tier, rgb_support, angle, size
    FROM KeyboardCase 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_keyboardcase_to_railway_format(case_data):
    """로컬 키보드케이스 데이터를 Railway 형식으로 변환"""
    return {
        "name": case_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "type": case_data['type'],
        "mounting": case_data['mounting'],
        "weight": float(case_data['weight']) if case_data['weight'] else 0.0,
        "acoustics": float(case_data['acoustics']) if case_data['acoustics'] else 0.0,
        "buildQuality": float(case_data['build_quality']) if case_data['build_quality'] else 0.0,
        "priceTier": float(case_data['price_tier']) if case_data['price_tier'] else 0.0,
        "rgbSupport": bool(case_data['rgb_support']) if case_data['rgb_support'] is not None else False,
        "angle": int(case_data['angle']) if case_data['angle'] else 0,
        "size": case_data['size'],
        "link": None  # KeyboardCase 테이블에 link 컬럼이 없음
    }

def send_keyboardcase_to_railway(case_data):
    """키보드케이스 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/keyboardcases"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=case_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ 키보드케이스 '{case_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 키보드케이스 '{case_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_keyboardcases():
    """키보드케이스 데이터 마이그레이션"""
    print("🔄 키보드케이스 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        cases = fetch_keyboardcase_data(conn)
        print(f"📊 로컬에서 {len(cases)}개 키보드케이스 발견")
        
        success_count = 0
        for case in cases:
            railway_case = convert_keyboardcase_to_railway_format(case)
            if send_keyboardcase_to_railway(railway_case):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 키보드케이스 마이그레이션 완료: {success_count}/{len(cases)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 키보드케이스 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

# ===== 케이블 마이그레이션 (수정된 컬럼명) =====
def fetch_cable_data(conn):
    """로컬에서 케이블 데이터 가져오기 - 정확한 컬럼명 사용"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, length, type, connector, quality, 
           flexibility, durability, price_tier, detachable, link
    FROM Cable 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_cable_to_railway_format(cable_data):
    """로컬 케이블 데이터를 Railway 형식으로 변환"""
    return {
        "name": cable_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": cable_data['material'],
        "length": int(cable_data['length']) if cable_data['length'] else 0,
        "type": cable_data['type'],
        "connector": cable_data['connector'],
        "quality": float(cable_data['quality']) if cable_data['quality'] else 0.0,
        "flexibility": float(cable_data['flexibility']) if cable_data['flexibility'] else 0.0,
        "durability": float(cable_data['durability']) if cable_data['durability'] else 0.0,
        "priceTier": float(cable_data['price_tier']) if cable_data['price_tier'] else 0.0,
        "detachable": bool(cable_data['detachable']) if cable_data['detachable'] is not None else False,
        "link": cable_data['link']
    }

def send_cable_to_railway(cable_data):
    """케이블 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/cables"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=cable_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ 케이블 '{cable_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 케이블 '{cable_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_cables():
    """케이블 데이터 마이그레이션"""
    print("🔄 케이블 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        cables = fetch_cable_data(conn)
        print(f"📊 로컬에서 {len(cables)}개 케이블 발견")
        
        success_count = 0
        for cable in cables:
            railway_cable = convert_cable_to_railway_format(cable)
            if send_cable_to_railway(railway_cable):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 케이블 마이그레이션 완료: {success_count}/{len(cables)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 케이블 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

def main():
    """메인 실행 함수"""
    print("🚚 KLUE 키보드 - 수정된 데이터 마이그레이션")
    print("=" * 50)
    
    # Railway API 상태 확인
    if not check_railway_status():
        print("❌ Railway API 연결 실패, 마이그레이션 중단")
        return
    
    print("\n" + "=" * 50)
    
    # 수정된 마이그레이션 실행
    migrations = [
        ("PCB", migrate_pcbs),
        ("키보드케이스", migrate_keyboardcases),
        ("케이블", migrate_cables)
    ]
    
    total_success = 0
    total_attempted = 0
    
    for name, migrate_func in migrations:
        print(f"\n🔄 {name} 마이그레이션 시작...")
        if migrate_func():
            print(f"✅ {name} 마이그레이션 완료")
            total_success += 1
        else:
            print(f"❌ {name} 마이그레이션 실패")
        total_attempted += 1
        
        time.sleep(2)
    
    print("\n" + "=" * 50)
    print(f"🎉 마이그레이션 완료! ({total_success}/{total_attempted} 성공)")
    
    # 최종 상태 확인
    print("\n📊 최종 Railway 상태:")
    check_railway_status()

if __name__ == "__main__":
    main() 