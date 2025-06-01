import mysql.connector
from datetime import datetime

def get_plate_data():
    plates = [
        {
            'name': 'Aluminum 60%',
            'material': 'Aluminum',
            'stiffness': 8.5,
            'sound_profile': 7.0,
            'price_tier': 2,
            'weight': 150,
            'flex': 3.0
        },
        {
            'name': 'Brass 60%',
            'material': 'Brass',
            'stiffness': 9.5,
            'sound_profile': 8.5,
            'price_tier': 3,
            'weight': 300,
            'flex': 2.0
        },
        {
            'name': 'FR4 60%',
            'material': 'FR4',
            'stiffness': 6.0,
            'sound_profile': 7.5,
            'price_tier': 1,
            'weight': 80,
            'flex': 7.0
        },
        {
            'name': 'Carbon Fiber 60%',
            'material': 'Carbon Fiber',
            'stiffness': 7.5,
            'sound_profile': 6.5,
            'price_tier': 3,
            'weight': 100,
            'flex': 5.0
        },
        {
            'name': 'POM 60%',
            'material': 'POM',
            'stiffness': 5.0,
            'sound_profile': 8.0,
            'price_tier': 2,
            'weight': 120,
            'flex': 6.5
        },
        {
            'name': 'Polycarbonate 60%',
            'material': 'Polycarbonate',
            'stiffness': 4.5,
            'sound_profile': 8.5,
            'price_tier': 2,
            'weight': 90,
            'flex': 7.5
        },
        {
            'name': 'Steel 60%',
            'material': 'Steel',
            'stiffness': 9.0,
            'sound_profile': 7.5,
            'price_tier': 2,
            'weight': 280,
            'flex': 2.5
        },
        {
            'name': 'Aluminum 65%',
            'material': 'Aluminum',
            'stiffness': 8.5,
            'sound_profile': 7.0,
            'price_tier': 2,
            'weight': 170,
            'flex': 3.0
        },
        {
            'name': 'FR4 75%',
            'material': 'FR4',
            'stiffness': 6.0,
            'sound_profile': 7.5,
            'price_tier': 1,
            'weight': 100,
            'flex': 7.0
        },
        {
            'name': 'Brass 75%',
            'material': 'Brass',
            'stiffness': 9.5,
            'sound_profile': 8.5,
            'price_tier': 3,
            'weight': 350,
            'flex': 2.0
        }
    ]
    return plates

def insert_plate_data():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="shin",
        database="klue_keyboard"
    )
    cursor = conn.cursor()

    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    future_time = '2025-12-31 23:59:59'

    plates = get_plate_data()

    try:
        for plate in plates:
            cursor.execute("""
                INSERT INTO Plate (
                    startdate, enddate, name, material,
                    stiffness, sound_profile, price_tier,
                    weight, flex
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                current_time, future_time,
                plate['name'], plate['material'],
                plate['stiffness'], plate['sound_profile'],
                plate['price_tier'], plate['weight'],
                plate['flex']
            ))

        conn.commit()
        print("Plate 데이터가 성공적으로 추가되었습니다.")

    except Exception as e:
        print(f"에러 발생: {e}")
        conn.rollback()

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    insert_plate_data() 