import mysql.connector
from datetime import datetime

def get_magnetic_switch_data():
    # 자석축 스위치와 Gateron Jade 데이터
    switches = [
        {
            'name': 'Gateron KS-3 Linear',
            'type': 'Magnetic',
            'linear_score': 9.5,
            'tactile_score': 1.5,
            'sound_score': 8.5,
            'weight_score': 7.0,
            'smoothness_score': 9.5,
            'speed_score': 9.5,
            'stability_score': 9.0,
            'durability_score': 9.5,
            'pressure': 45,
            'stem_material': 'POM'
        },
        {
            'name': 'Gateron KS-3X47 Linear',
            'type': 'Magnetic',
            'linear_score': 9.5,
            'tactile_score': 1.5,
            'sound_score': 8.0,
            'weight_score': 7.5,
            'smoothness_score': 9.5,
            'speed_score': 9.5,
            'stability_score': 9.0,
            'durability_score': 9.5,
            'pressure': 47,
            'stem_material': 'POM'
        },
        {
            'name': 'Varmilo EC Rose V2',
            'type': 'Magnetic',
            'linear_score': 9.0,
            'tactile_score': 2.0,
            'sound_score': 8.5,
            'weight_score': 7.0,
            'smoothness_score': 9.0,
            'speed_score': 9.0,
            'stability_score': 9.0,
            'durability_score': 9.5,
            'pressure': 45,
            'stem_material': 'POM'
        },
        {
            'name': 'Varmilo EC Sakura V2',
            'type': 'Magnetic',
            'linear_score': 9.0,
            'tactile_score': 2.0,
            'sound_score': 8.0,
            'weight_score': 6.5,
            'smoothness_score': 9.0,
            'speed_score': 9.0,
            'stability_score': 9.0,
            'durability_score': 9.5,
            'pressure': 35,
            'stem_material': 'POM'
        },
        {
            'name': 'Gateron Jade',
            'type': 'Clicky',
            'linear_score': 2.0,
            'tactile_score': 8.5,
            'sound_score': 9.5,
            'weight_score': 7.5,
            'smoothness_score': 8.0,
            'speed_score': 7.5,
            'stability_score': 8.5,
            'durability_score': 8.5,
            'pressure': 50,
            'stem_material': 'POM'
        }
    ]
    return switches

def insert_magnetic_switches():
    # MySQL 연결
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    cursor = conn.cursor()

    # 현재 시간과 미래 시간 설정
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    future_time = '2025-12-31 23:59:59'

    # 스위치 데이터 가져오기
    switches = get_magnetic_switch_data()

    try:
        # 데이터 삽입
        for switch in switches:
            cursor.execute("""
                INSERT INTO Switches (
                    startdate, enddate, name, type,
                    linear_score, tactile_score, sound_score,
                    weight_score, smoothness_score, speed_score,
                    stability_score, durability_score,
                    pressure, stem_material
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time,
                switch['name'], switch['type'],
                switch['linear_score'], switch['tactile_score'],
                switch['sound_score'], switch['weight_score'],
                switch['smoothness_score'], switch['speed_score'],
                switch['stability_score'], switch['durability_score'],
                switch['pressure'], switch['stem_material']
            ))

        # 변경사항 저장
        conn.commit()
        print("자석축 스위치 데이터가 성공적으로 추가되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        conn.rollback()

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    insert_magnetic_switches() 