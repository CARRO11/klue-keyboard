#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
KLUE 키보드 - PCB 올바른 형식 마이그레이션 스크립트
"""

import mysql.connector
import requests
import json
import time

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

def fetch_pcb_data(conn):
    """로컬에서 PCB 데이터 가져오기"""
    cursor = conn.cursor(dictionary=True)
    query = """
    SELECT name, layout, hotswap, wireless, rgb, usb_type, firmware_type, 
           rgb_support, qmk_via, flex, price_tier, build_quality, features, link
    FROM PCB 
    ORDER BY id
    """
    cursor.execute(query)
    return cursor.fetchall()

def convert_pcb_to_railway_format(pcb_data, case_id=1):
    """로컬 PCB 데이터를 Railway 형식으로 변환 - 올바른 KeyboardCase 객체 형식"""
    return {
        "name": pcb_data['name'],
        "description": f"Layout: {pcb_data.get('layout', 'Unknown')}, USB: {pcb_data.get('usb_type', 'Unknown')}",
        "keyboardCase": {  # 객체 형태로 전송
            "id": case_id
        }
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
            print(f"   요청 데이터: {json.dumps(pcb_data, indent=2)}")
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
        for i, pcb in enumerate(pcbs, 1):
            # 각 PCB를 다른 케이스에 배정 (1-10 순환)
            case_id = ((i - 1) % 10) + 1
            railway_pcb = convert_pcb_to_railway_format(pcb, case_id)
            
            print(f"📋 PCB '{pcb['name']}' → 케이스 ID {case_id}")
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

def main():
    """메인 실행 함수"""
    print("🚚 KLUE 키보드 - PCB 올바른 형식 마이그레이션")
    print("=" * 60)
    
    # PCB 마이그레이션 실행
    if migrate_pcbs():
        print("✅ PCB 마이그레이션 성공!")
    else:
        print("❌ PCB 마이그레이션 실패!")

if __name__ == "__main__":
    main() 