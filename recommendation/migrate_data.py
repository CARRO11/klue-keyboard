import sqlite3
import mysql.connector
import pandas as pd
from datetime import datetime

def migrate_data():
    # SQLite 연결
    sqlite_conn = sqlite3.connect('keyboard_components.db')

    # MySQL 연결
    mysql_conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    mysql_cursor = mysql_conn.cursor()

    # 현재 시간 (startdate로 사용)
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    # 미래 시간 (enddate로 사용, 예: 1년 후)
    future_time = '2025-12-31 23:59:59'

    try:
        # Switch 데이터 마이그레이션
        switches = pd.read_sql_query("SELECT * FROM switches", sqlite_conn)
        for _, switch in switches.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Switch (
                    startdate, enddate, name, type, 
                    linear_score, tactile_score, sound_score, 
                    weight_score, smoothness_score, speed_score,
                    stability_score, durability_score
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, switch['name'], switch['type'],
                switch['linearScore'], switch['tactileScore'], switch['soundScore'],
                switch['weightScore'], switch['smoothnessScore'], switch['speedScore'],
                switch['stabilityScore'], switch['durabilityScore']
            ))

        # KeyboardCase 데이터 마이그레이션
        cases = pd.read_sql_query("SELECT * FROM housing", sqlite_conn)
        for _, case in cases.iterrows():
            mysql_cursor.execute("""
                INSERT INTO KeyboardCase (
                    name, type, mounting, weight, acoustics,
                    build_quality, price_tier, rgb_support, angle, size
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                case['name'], case['type'], case['mounting'], case['weight'],
                case['acoustics'], case['build_quality'], case['price_tier'],
                case['rgb_support'], case['angle'], case['size']
            ))

        # PCB 데이터 마이그레이션
        pcbs = pd.read_sql_query("SELECT * FROM pcb", sqlite_conn)
        for _, pcb in pcbs.iterrows():
            mysql_cursor.execute("""
                INSERT INTO PCB (
                    startdate, enddate, name, rgb_support, qmk_via,
                    flex, price_tier, build_quality, features
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, pcb['name'], pcb['rgb_support'],
                pcb['qmk_via'], pcb['flex'], pcb['price_tier'],
                pcb['build_quality'], pcb['features']
            ))

        # Plate 데이터 마이그레이션
        plates = pd.read_sql_query("SELECT * FROM plate", sqlite_conn)
        for _, plate in plates.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Plate (
                    startdate, enddate, name, material, stiffness,
                    sound_profile, price_tier, weight, flex
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, plate['name'], plate['material'],
                plate['stiffness'], plate['sound_profile'], plate['price_tier'],
                plate['weight'], plate['flex']
            ))

        # Stabilizer 데이터 마이그레이션
        stabilizers = pd.read_sql_query("SELECT * FROM stabilizer", sqlite_conn)
        for _, stab in stabilizers.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Stabilizer (
                    startdate, enddate, name, type, rattle,
                    smoothness, sound_profile, price_tier, build_quality
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, stab['name'], stab['type'],
                stab['rattle'], stab['smoothness'], stab['sound_profile'],
                stab['price_tier'], stab['build_quality']
            ))

        # Keycap 데이터 마이그레이션
        keycaps = pd.read_sql_query("SELECT * FROM keycap", sqlite_conn)
        for _, keycap in keycaps.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Keycap (
                    startdate, enddate, name, material, profile,
                    thickness, sound_profile, build_quality,
                    price_tier, rgb_compatible, durability
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, keycap['name'], keycap['material'],
                keycap['profile'], keycap['thickness'], keycap['sound_profile'],
                keycap['build_quality'], keycap['price_tier'],
                keycap['rgb_compatible'], keycap['durability']
            ))

        # Foam 데이터 마이그레이션
        foams = pd.read_sql_query("SELECT * FROM foam", sqlite_conn)
        for _, foam in foams.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Foam (
                    startdate, enddate, name, type, thickness,
                    density, sound_dampening, compression,
                    durability, price_tier
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, foam['name'], foam['type'],
                foam['thickness'], foam['density'], foam['sound_dampening'],
                foam['compression'], foam['durability'], foam['price_tier']
            ))

        # Gasket 데이터 마이그레이션
        gaskets = pd.read_sql_query("SELECT * FROM gasket", sqlite_conn)
        for _, gasket in gaskets.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Gasket (
                    startdate, enddate, name, material, thickness,
                    flexibility, dampening, durability, price_tier
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, gasket['name'], gasket['material'],
                gasket['thickness'], gasket['flexibility'], gasket['dampening'],
                gasket['durability'], gasket['price_tier']
            ))

        # Cable 데이터 마이그레이션
        cables = pd.read_sql_query("SELECT * FROM cable", sqlite_conn)
        for _, cable in cables.iterrows():
            mysql_cursor.execute("""
                INSERT INTO Cable (
                    startdate, enddate, name, type, connector,
                    length, quality, flexibility, durability,
                    price_tier, detachable
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time, cable['name'], cable['type'],
                cable['connector'], cable['length'], cable['quality'],
                cable['flexibility'], cable['durability'],
                cable['price_tier'], cable['detachable']
            ))

        # 변경사항 저장
        mysql_conn.commit()
        print("데이터 마이그레이션이 성공적으로 완료되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        mysql_conn.rollback()

    finally:
        # 연결 종료
        sqlite_conn.close()
        mysql_cursor.close()
        mysql_conn.close()

if __name__ == "__main__":
    migrate_data() 