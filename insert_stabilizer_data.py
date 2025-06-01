import mysql.connector
from datetime import datetime

def get_stabilizer_data():
    stabilizers = [
        {
            'name': 'Durock V2 Screw-in',
            'type': 'Screw-in',
            'rattle': 2.0,
            'smoothness': 9.0,
            'sound_profile': 8.5,
            'price_tier': 3,
            'build_quality': 9.0
        },
        {
            'name': 'C³Equalz Screw-in',
            'type': 'Screw-in',
            'rattle': 2.5,
            'smoothness': 8.5,
            'sound_profile': 8.0,
            'price_tier': 3,
            'build_quality': 8.5
        },
        {
            'name': 'Zeal Screw-in V2',
            'type': 'Screw-in',
            'rattle': 2.0,
            'smoothness': 9.0,
            'sound_profile': 8.5,
            'price_tier': 3,
            'build_quality': 9.0
        },
        {
            'name': 'GMK Screw-in',
            'type': 'Screw-in',
            'rattle': 3.5,
            'smoothness': 7.5,
            'sound_profile': 7.0,
            'price_tier': 2,
            'build_quality': 7.5
        },
        {
            'name': 'Cherry Clip-in',
            'type': 'Clip-in',
            'rattle': 4.5,
            'smoothness': 6.5,
            'sound_profile': 6.0,
            'price_tier': 1,
            'build_quality': 7.0
        },
        {
            'name': 'Everglide Panda V3',
            'type': 'Screw-in',
            'rattle': 2.5,
            'smoothness': 8.5,
            'sound_profile': 8.0,
            'price_tier': 2,
            'build_quality': 8.5
        },
        {
            'name': 'Owlab Screw-in',
            'type': 'Screw-in',
            'rattle': 2.0,
            'smoothness': 8.5,
            'sound_profile': 8.5,
            'price_tier': 3,
            'build_quality': 8.5
        },
        {
            'name': 'TX Stabilizers Rev. 3',
            'type': 'Screw-in',
            'rattle': 1.5,
            'smoothness': 9.5,
            'sound_profile': 9.0,
            'price_tier': 3,
            'build_quality': 9.5
        },
        {
            'name': 'Staebies',
            'type': 'Screw-in',
            'rattle': 2.0,
            'smoothness': 9.0,
            'sound_profile': 8.5,
            'price_tier': 3,
            'build_quality': 9.0
        },
        {
            'name': 'KBDfans PCB Screw-in',
            'type': 'Screw-in',
            'rattle': 3.0,
            'smoothness': 7.5,
            'sound_profile': 7.0,
            'price_tier': 2,
            'build_quality': 7.5
        }
    ]
    return stabilizers

def insert_stabilizer_data():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    cursor = conn.cursor()

    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    future_time = '2025-12-31 23:59:59'

    stabilizers = get_stabilizer_data()

    try:
        for stab in stabilizers:
            cursor.execute("""
                INSERT INTO Stabilizer (
                    startdate, enddate, name, type,
                    rattle, smoothness, sound_profile,
                    price_tier, build_quality
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time,
                stab['name'], stab['type'],
                stab['rattle'], stab['smoothness'],
                stab['sound_profile'], stab['price_tier'],
                stab['build_quality']
            ))

        conn.commit()
        print("Stabilizer 데이터가 성공적으로 추가되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        conn.rollback()

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    insert_stabilizer_data() 