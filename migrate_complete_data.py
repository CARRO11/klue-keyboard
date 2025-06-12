#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
KLUE 키보드 - 완전한 데이터 마이그레이션 스크립트
로컬 MySQL에서 Railway PostgreSQL로 모든 부품 데이터 마이그레이션
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
        # 각 엔드포인트별 현재 상태 확인
        endpoints = ['switches', 'keycaps', 'stabilizers', 'pcbs', 'keyboardcases', 'cables', 'plates', 'gaskets', 'foams']
        
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

# ===== PCB 마이그레이션 =====
def fetch_pcb_data(conn):
    """로컬에서 PCB 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, form_factor, mount_style, layout, switch_type, link
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
        "formFactor": pcb_data['form_factor'],
        "mountStyle": pcb_data['mount_style'],
        "layout": pcb_data['layout'],
        "switchType": pcb_data['switch_type'],
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

# ===== 키보드케이스 마이그레이션 =====
def fetch_keyboardcase_data(conn):
    """로컬에서 키보드케이스 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, form_factor, mount_style, link
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
        "material": case_data['material'],
        "formFactor": case_data['form_factor'],
        "mountStyle": case_data['mount_style'],
        "link": case_data['link']
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

# ===== 케이블 마이그레이션 =====
def fetch_cable_data(conn):
    """로컬에서 케이블 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, connector_type, length, color, link
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
        "connectorType": cable_data['connector_type'],
        "length": cable_data['length'],
        "color": cable_data['color'],
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

# ===== PLATE 마이그레이션 =====
def fetch_plate_data(conn):
    """로컬에서 플레이트 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, Compatibility, flex_level, thickness, stiffness, sound_profile, link
    FROM Plate 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_plate_to_railway_format(plate_data):
    """로컬 플레이트 데이터를 Railway 형식으로 변환"""
    return {
        "name": plate_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": plate_data['material'],
        "compatibility": plate_data['Compatibility'],
        "flexLevel": plate_data['flex_level'],
        "thickness": float(plate_data['thickness']) if plate_data['thickness'] else 0.0,
        "stiffness": float(plate_data['stiffness']) if plate_data['stiffness'] else 0.0,
        "soundProfile": float(plate_data['sound_profile']) if plate_data['sound_profile'] else 0.0,
        "link": plate_data['link']
    }

def send_plate_to_railway(plate_data):
    """플레이트 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/plates"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=plate_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ 플레이트 '{plate_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 플레이트 '{plate_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_plates():
    """플레이트 데이터 마이그레이션"""
    print("🔄 플레이트 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        plates = fetch_plate_data(conn)
        print(f"📊 로컬에서 {len(plates)}개 플레이트 발견")
        
        success_count = 0
        for plate in plates:
            railway_plate = convert_plate_to_railway_format(plate)
            if send_plate_to_railway(railway_plate):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 플레이트 마이그레이션 완료: {success_count}/{len(plates)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 플레이트 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

# ===== GASKET 마이그레이션 =====
def fetch_gasket_data(conn):
    """로컬에서 가스켓 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, typing, thickness, flexibility, dampening, durability, link
    FROM Gasket 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_gasket_to_railway_format(gasket_data):
    """로컬 가스켓 데이터를 Railway 형식으로 변환"""
    return {
        "name": gasket_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": gasket_data['material'],
        "typing": gasket_data['typing'],
        "thickness": float(gasket_data['thickness']) if gasket_data['thickness'] else 0.0,
        "flexibility": float(gasket_data['flexibility']) if gasket_data['flexibility'] else 0.0,
        "dampening": float(gasket_data['dampening']) if gasket_data['dampening'] else 0.0,
        "durability": float(gasket_data['durability']) if gasket_data['durability'] else 0.0,
        "link": gasket_data['link']
    }

def send_gasket_to_railway(gasket_data):
    """가스켓 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/gaskets"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=gasket_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ 가스켓 '{gasket_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 가스켓 '{gasket_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_gaskets():
    """가스켓 데이터 마이그레이션"""
    print("🔄 가스켓 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        gaskets = fetch_gasket_data(conn)
        print(f"📊 로컬에서 {len(gaskets)}개 가스켓 발견")
        
        success_count = 0
        for gasket in gaskets:
            railway_gasket = convert_gasket_to_railway_format(gasket)
            if send_gasket_to_railway(railway_gasket):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 가스켓 마이그레이션 완료: {success_count}/{len(gaskets)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 가스켓 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

# ===== FOAM 마이그레이션 =====
def fetch_foam_data(conn):
    """로컬에서 폼 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, material, type, thickness, density, sound_dampening, compression, durability, link
    FROM Foam 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_foam_to_railway_format(foam_data):
    """로컬 폼 데이터를 Railway 형식으로 변환"""
    return {
        "name": foam_data['name'],
        "startdate": "2025-06-10T12:00:00",
        "enddate": "2025-12-31T23:59:59",
        "material": foam_data['material'],
        "type": foam_data['type'],
        "thickness": float(foam_data['thickness']) if foam_data['thickness'] else 0.0,
        "density": float(foam_data['density']) if foam_data['density'] else 0.0,
        "soundDampening": float(foam_data['sound_dampening']) if foam_data['sound_dampening'] else 0.0,
        "compression": float(foam_data['compression']) if foam_data['compression'] else 0.0,
        "durability": float(foam_data['durability']) if foam_data['durability'] else 0.0,
        "link": foam_data['link']
    }

def send_foam_to_railway(foam_data):
    """폼 데이터를 Railway API로 전송"""
    url = f"{RAILWAY_API_BASE}/foams"
    headers = {'Content-Type': 'application/json'}
    
    try:
        response = requests.post(url, json=foam_data, headers=headers)
        if response.status_code == 200:
            print(f"✅ 폼 '{foam_data['name']}' 생성 성공")
            return True
        else:
            print(f"❌ 폼 '{foam_data['name']}' 생성 실패: {response.status_code}")
            print(f"   응답: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 네트워크 오류: {e}")
        return False

def migrate_foams():
    """폼 데이터 마이그레이션"""
    print("🔄 폼 데이터 마이그레이션 시작...")
    
    conn = connect_local_db()
    if not conn:
        return False
    
    try:
        foams = fetch_foam_data(conn)
        print(f"📊 로컬에서 {len(foams)}개 폼 발견")
        
        success_count = 0
        for foam in foams:
            railway_foam = convert_foam_to_railway_format(foam)
            if send_foam_to_railway(railway_foam):
                success_count += 1
            time.sleep(0.5)
        
        print(f"🎉 폼 마이그레이션 완료: {success_count}/{len(foams)} 성공")
        return True
        
    except Exception as e:
        print(f"❌ 폼 마이그레이션 오류: {e}")
        return False
    finally:
        conn.close()

def main():
    """메인 실행 함수"""
    print("🚚 KLUE 키보드 - 완전한 데이터 마이그레이션 시작")
    print("=" * 60)
    
    # Railway API 상태 확인
    if not check_railway_status():
        print("❌ Railway API 연결 실패, 마이그레이션 중단")
        return
    
    print("\n" + "=" * 60)
    
    # 모든 부품 마이그레이션
    migrations = [
        ("PCB", migrate_pcbs),
        ("키보드케이스", migrate_keyboardcases),
        ("케이블", migrate_cables),
        ("플레이트", migrate_plates),
        ("가스켓", migrate_gaskets),
        ("폼", migrate_foams)
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
        
        # 다음 마이그레이션 전 잠시 대기
        time.sleep(2)
    
    print("\n" + "=" * 60)
    print(f"🎉 마이그레이션 완료! ({total_success}/{total_attempted} 성공)")
    
    # 최종 상태 확인
    print("\n📊 최종 Railway 상태:")
    check_railway_status()

if __name__ == "__main__":
    main() 