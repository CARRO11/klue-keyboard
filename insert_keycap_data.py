import mysql.connector
from datetime import datetime

def get_keycap_data():
    keycaps = [
        {
            'name': 'GMK Red Samurai',
            'material': 'ABS',
            'profile': 'Cherry',
            'thickness': 1.5,
            'sound_profile': 8.0,
            'build_quality': 9.0,
            'price_tier': 3,
            'rgb_compatible': True,
            'durability': 7.5
        },
        {
            'name': 'ePBT Black Japanese',
            'material': 'PBT',
            'profile': 'Cherry',
            'thickness': 1.4,
            'sound_profile': 7.5,
            'build_quality': 8.5,
            'price_tier': 2,
            'rgb_compatible': True,
            'durability': 9.0
        },
        {
            'name': 'MT3 Susuwatari',
            'material': 'ABS',
            'profile': 'MT3',
            'thickness': 1.6,
            'sound_profile': 8.5,
            'build_quality': 9.0,
            'price_tier': 3,
            'rgb_compatible': False,
            'durability': 8.0
        },
        {
            'name': 'SA Laser',
            'material': 'ABS',
            'profile': 'SA',
            'thickness': 1.5,
            'sound_profile': 9.0,
            'build_quality': 8.5,
            'price_tier': 3,
            'rgb_compatible': True,
            'durability': 7.5
        },
        {
            'name': 'NP PBT Crayon',
            'material': 'PBT',
            'profile': 'NP',
            'thickness': 1.4,
            'sound_profile': 7.0,
            'build_quality': 8.0,
            'price_tier': 1,
            'rgb_compatible': True,
            'durability': 8.5
        },
        {
            'name': 'KAT Arctic',
            'material': 'PBT',
            'profile': 'KAT',
            'thickness': 1.6,
            'sound_profile': 8.0,
            'build_quality': 8.5,
            'price_tier': 2,
            'rgb_compatible': True,
            'durability': 9.0
        },
        {
            'name': 'DSA Granite',
            'material': 'PBT',
            'profile': 'DSA',
            'thickness': 1.4,
            'sound_profile': 7.5,
            'build_quality': 8.0,
            'price_tier': 2,
            'rgb_compatible': True,
            'durability': 8.5
        },
        {
            'name': 'GMK Olivia',
            'material': 'ABS',
            'profile': 'Cherry',
            'thickness': 1.5,
            'sound_profile': 8.0,
            'build_quality': 9.0,
            'price_tier': 3,
            'rgb_compatible': True,
            'durability': 7.5
        },
        {
            'name': 'XDA Canvas',
            'material': 'PBT',
            'profile': 'XDA',
            'thickness': 1.4,
            'sound_profile': 7.0,
            'build_quality': 8.0,
            'price_tier': 2,
            'rgb_compatible': True,
            'durability': 8.5
        },
        {
            'name': 'OEM Profile PBT',
            'material': 'PBT',
            'profile': 'OEM',
            'thickness': 1.3,
            'sound_profile': 6.5,
            'build_quality': 7.5,
            'price_tier': 1,
            'rgb_compatible': True,
            'durability': 8.0
        }
    ]
    return keycaps

def insert_keycap_data():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    cursor = conn.cursor()

    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    future_time = '2025-12-31 23:59:59'

    keycaps = get_keycap_data()

    try:
        for keycap in keycaps:
            cursor.execute("""
                INSERT INTO Keycap (
                    startdate, enddate, name, material,
                    profile, thickness, sound_profile,
                    build_quality, price_tier,
                    rgb_compatible, durability
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time,
                keycap['name'], keycap['material'],
                keycap['profile'], keycap['thickness'],
                keycap['sound_profile'], keycap['build_quality'],
                keycap['price_tier'], keycap['rgb_compatible'],
                keycap['durability']
            ))

        conn.commit()
        print("Keycap 데이터가 성공적으로 추가되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        conn.rollback()

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    insert_keycap_data() 