import mysql.connector
from datetime import datetime

def get_pcb_data():
    pcbs = [
        {
            'name': 'KBD75 V3.1',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 7.5,
            'price_tier': 3,
            'build_quality': 8.5,
            'features': 'Hot-swap, USB Type-C, RGB LED, Multiple layouts'
        },
        {
            'name': 'DZ60RGB V2',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 6.5,
            'price_tier': 2,
            'build_quality': 8.0,
            'features': 'Hot-swap, USB Type-C, Per-key RGB'
        },
        {
            'name': 'NK65 Entry',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 7.0,
            'price_tier': 2,
            'build_quality': 7.5,
            'features': 'Hot-swap, USB Type-C, RGB underglow'
        },
        {
            'name': 'Tofu60 PCB',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 6.0,
            'price_tier': 2,
            'build_quality': 8.0,
            'features': 'Multiple layouts, USB Type-C'
        },
        {
            'name': 'Bakeneko60',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 8.0,
            'price_tier': 2,
            'build_quality': 8.5,
            'features': 'Unified Daughterboard, QMK/VIA'
        },
        {
            'name': 'KBD67 MKII',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 7.5,
            'price_tier': 3,
            'build_quality': 9.0,
            'features': 'Hot-swap, USB Type-C, ESD protection'
        },
        {
            'name': 'Discipline65',
            'rgb_support': False,
            'qmk_via': True,
            'flex': 6.0,
            'price_tier': 2,
            'build_quality': 7.5,
            'features': 'Through-hole components, DIY kit'
        },
        {
            'name': 'WT60-D',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 7.0,
            'price_tier': 3,
            'build_quality': 9.0,
            'features': 'Wilba Tech PCB, Premium features'
        },
        {
            'name': 'Instant60',
            'rgb_support': True,
            'qmk_via': True,
            'flex': 6.5,
            'price_tier': 2,
            'build_quality': 8.0,
            'features': 'Hot-swap, Multiple layouts'
        },
        {
            'name': 'Plain60',
            'rgb_support': False,
            'qmk_via': True,
            'flex': 6.0,
            'price_tier': 1,
            'build_quality': 7.0,
            'features': 'Basic 60% PCB, QMK support'
        }
    ]
    return pcbs

def insert_pcb_data():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    cursor = conn.cursor()

    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    future_time = '2025-12-31 23:59:59'

    pcbs = get_pcb_data()

    try:
        for pcb in pcbs:
            cursor.execute("""
                INSERT INTO PCB (
                    startdate, enddate, name,
                    rgb_support, qmk_via, flex,
                    price_tier, build_quality, features
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time,
                pcb['name'], pcb['rgb_support'],
                pcb['qmk_via'], pcb['flex'],
                pcb['price_tier'], pcb['build_quality'],
                pcb['features']
            ))

        conn.commit()
        print("PCB 데이터가 성공적으로 추가되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        conn.rollback()

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    insert_pcb_data() 