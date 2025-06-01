import sqlite3
import pandas as pd
import os

class KeyboardDBManager:
    def __init__(self, db_path='keyboard_components.db'):
        """데이터베이스 매니저 초기화"""
        self.db_path = db_path
        self.conn = sqlite3.connect(db_path)
        self.create_tables()
    
    def create_tables(self):
        """테이블 생성"""
        # 스위치 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS switches (
            name TEXT PRIMARY KEY,
            type TEXT,
            linearScore REAL,
            tactileScore REAL,
            soundScore REAL,
            weightScore REAL,
            smoothnessScore REAL,
            speedScore REAL,
            stabilityScore REAL,
            durabilityScore REAL
        )
        ''')

        # 하우징 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS housing (
            name TEXT PRIMARY KEY,
            type TEXT,
            mounting TEXT,
            weight REAL,
            acoustics REAL,
            build_quality REAL,
            price_tier REAL,
            rgb_support INTEGER,
            angle INTEGER,
            size TEXT
        )
        ''')

        # 플레이트 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS plate (
            name TEXT PRIMARY KEY,
            material TEXT,
            stiffness REAL,
            sound_profile REAL,
            price_tier REAL,
            weight REAL,
            flex REAL
        )
        ''')

        # PCB 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS pcb (
            name TEXT PRIMARY KEY,
            type TEXT,
            rgb_support INTEGER,
            qmk_via INTEGER,
            flex REAL,
            price_tier REAL,
            build_quality REAL,
            features TEXT
        )
        ''')

        # 스태빌라이저 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS stabilizer (
            name TEXT PRIMARY KEY,
            type TEXT,
            rattle REAL,
            smoothness REAL,
            sound_profile REAL,
            price_tier REAL,
            build_quality REAL
        )
        ''')

        # 키캡 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS keycap (
            name TEXT PRIMARY KEY,
            material TEXT,
            profile TEXT,
            thickness REAL,
            sound_profile REAL,
            build_quality REAL,
            price_tier REAL,
            rgb_compatible INTEGER,
            durability REAL
        )
        ''')

        # 폼 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS foam (
            name TEXT PRIMARY KEY,
            type TEXT,
            thickness REAL,
            density REAL,
            sound_dampening REAL,
            compression REAL,
            durability REAL,
            price_tier REAL
        )
        ''')

        # 가스켓 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS gasket (
            name TEXT PRIMARY KEY,
            material TEXT,
            thickness REAL,
            flexibility REAL,
            dampening REAL,
            durability REAL,
            price_tier REAL
        )
        ''')

        # O-링 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS oring (
            name TEXT PRIMARY KEY,
            material TEXT,
            hardness REAL,
            dampening REAL,
            durability REAL,
            price_tier REAL
        )
        ''')

        # 케이블 테이블
        self.conn.execute('''
        CREATE TABLE IF NOT EXISTS cable (
            name TEXT PRIMARY KEY,
            type TEXT,
            connector TEXT,
            length REAL,
            quality REAL,
            flexibility REAL,
            durability REAL,
            price_tier REAL,
            detachable INTEGER
        )
        ''')

        self.conn.commit()

    def import_csv_data(self, data_dir='data'):
        """CSV 파일에서 데이터 가져오기"""
        # 각 컴포넌트에 대한 CSV 파일 읽기 및 데이터베이스에 삽입
        components = [
            'switches', 'housing', 'plate', 'pcb', 'stabilizer',
            'keycap', 'foam', 'gasket', 'oring', 'cable'
        ]

        for component in components:
            csv_path = os.path.join(data_dir, f'{component}.csv')
            if os.path.exists(csv_path):
                df = pd.read_csv(csv_path)
                
                # boolean 값을 SQLite INTEGER로 변환
                bool_columns = df.select_dtypes(include=['bool']).columns
                for col in bool_columns:
                    df[col] = df[col].astype(int)
                
                # DataFrame을 데이터베이스에 삽입
                table_name = component.rstrip('s')  # 복수형 제거 (switches -> switch)
                df.to_sql(table_name, self.conn, if_exists='replace', index=False)
                print(f"{component} 데이터가 성공적으로 가져와졌습니다.")

    def close(self):
        """데이터베이스 연결 종료"""
        self.conn.close()

    def query_components(self, component_type, conditions=None):
        """컴포넌트 쿼리
        Args:
            component_type (str): 컴포넌트 타입 (예: 'switch', 'housing' 등)
            conditions (str): WHERE 절에 사용할 조건 (옵션)
        """
        query = f"SELECT * FROM {component_type}"
        if conditions:
            query += f" WHERE {conditions}"
        
        return pd.read_sql_query(query, self.conn)

if __name__ == "__main__":
    # 데이터베이스 매니저 생성 및 CSV 데이터 가져오기
    db_manager = KeyboardDBManager()
    db_manager.import_csv_data()
    
    # 연결 종료
    db_manager.close() 