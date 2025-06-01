import mysql.connector
from mysql.connector import pooling
from typing import Dict, List, Tuple, Optional
import numpy as np
from ai_recommender import AIRecommender
from decimal import Decimal

class KeyboardRecommender:
    def __init__(self):
        # MySQL 연결 풀 설정
        self.pool_config = {
            'pool_name': 'keyboard_pool',
            'pool_size': 10,  # 연결 풀 크기
            'pool_reset_session': True,
            'host': 'localhost',
            'database': 'klue_keyboard',
            'user': 'root',
            'password': 'shin',
            'charset': 'utf8mb4',
            'collation': 'utf8mb4_unicode_ci',
            'autocommit': True
        }
        
        try:
            self.connection_pool = pooling.MySQLConnectionPool(**self.pool_config)
        except mysql.connector.Error as err:
            print(f"연결 풀 생성 오류: {err}")
            self.connection_pool = None
        self.ai_recommender = AIRecommender()
        
    def get_db_connection(self):
        """데이터베이스 연결 풀에서 연결 가져오기"""
        try:
            if self.connection_pool:
                return self.connection_pool.get_connection()
            else:
                # 풀이 없으면 직접 연결
                return mysql.connector.connect(
                    host='localhost',
                    database='klue_keyboard',
                    user='root',
                    password='shin',
                    charset='utf8mb4',
                    collation='utf8mb4_unicode_ci',
                    autocommit=True
                )
        except mysql.connector.Error as err:
            print(f"데이터베이스 연결 오류: {err}")
            return None

    def connect_db(self):
        """기존 호환성을 위한 메서드 (사용하지 않음)"""
        pass

    def close_db(self):
        """기존 호환성을 위한 메서드 (사용하지 않음)"""
        pass

    def get_weights_by_category(self, category: str) -> Dict:
        """카테고리별 가중치 반환"""
        weights = {
            'switches': {
                'sound_score': 0.15,      # 소리
                'price_tier': 0.10,       # 가격
                'durability_score': 0.10,  # 내구성
                'type': 0.10,             # 타입
                'stem_material': 0.10,    # 재질
                'linear_score': 0.09,     # 선형성
                'tactile_score': 0.09,    # 촉각성
                'weight_score': 0.07,     # 무게
                'smoothness_score': 0.07, # 부드러움
                'speed_score': 0.07,      # 속도
                'stability_score': 0.06   # 안정성
            },
            'plate': {
                'sound_profile': 0.15,    # 소리
                'price_tier': 0.15,       # 가격
                'material': 0.15,         # 재질
                'flex': 0.15,            # 유연성
                'stiffness': 0.10,       # 강성
                'thickness': 0.10,       # 두께
                'weight': 0.10,          # 무게
                'size': 0.10             # 크기
            },
            'stabilizer': {
                'sound_profile': 0.20,    # 소리
                'price_tier': 0.15,       # 가격
                'build_quality': 0.15,    # 품질
                'material': 0.15,         # 재질
                'smoothness': 0.15,       # 부드러움
                'rattle': 0.10,          # 흔들림
                'type': 0.10             # 타입
            },
            'keycap': {
                'sound_profile': 0.20,    # 소리
                'price_tier': 0.15,       # 가격
                'build_quality': 0.15,    # 품질
                'material': 0.15,         # 재질
                'profile': 0.15,         # 프로파일
                'thickness': 0.10,       # 두께
                'rgb_compatible': 0.05,  # RGB 호환성
                'durability': 0.05       # 내구성
            },
            'pcb': {
                'price_tier': 0.20,       # 가격
                'build_quality': 0.20,    # 품질
                'features': 0.15,         # 기능
                'flex': 0.15,            # 유연성
                'rgb_support': 0.10,     # RGB 지원
                'qmk_via': 0.10,         # QMK/VIA 지원
                'size': 0.10             # 크기
            }
        }
        return weights.get(category, weights['switches'])
            
    def calculate_similarity_score(self, user_preferences: Dict, item_features: Dict, category: str) -> float:
        """사용자 선호도와 아이템 특성 간의 유사도 계산"""
        score = 0.0
        weights = self.get_weights_by_category(category)
        
        for key, weight in weights.items():
            if key in user_preferences and key in item_features:
                if isinstance(user_preferences[key], (int, float)) and isinstance(item_features[key], (int, float)):
                    # 수치형 데이터의 경우 (1-10 스케일로 정규화)
                    user_val = self.normalize_value(user_preferences[key], key)
                    item_val = self.normalize_value(item_features[key], key)
                    score += weight * (1 - abs(user_val - item_val) / 10)
                else:
                    # 카테고리형 데이터의 경우
                    if key in ['material', 'type', 'profile']:
                        # 재질, 타입, 프로파일의 경우 부분 일치도 고려
                        user_val = str(user_preferences[key]).lower()
                        item_val = str(item_features[key]).lower()
                        if user_val == item_val:
                            score += weight
                        elif user_val in item_val or item_val in user_val:
                            score += weight * 0.5
                    else:
                        # 그 외의 경우 정확한 일치만 고려
                        score += weight if str(user_preferences[key]).lower() == str(item_features[key]).lower() else 0
                    
        return score

    def normalize_value(self, value: float, key: str) -> float:
        """값을 1-10 스케일로 정규화"""
        if value is None:
            return 0
            
        if key in ['price_tier']:
            # 가격 티어는 1-4 스케일
            return value * 2.5
        elif key in ['sound_score', 'linear_score', 'tactile_score', 'weight_score', 
                    'smoothness_score', 'speed_score', 'stability_score', 'durability_score',
                    'sound_profile', 'build_quality']:
            # 이미 1-10 스케일인 경우
            return value
        elif key in ['thickness']:
            # mm 단위의 두께를 1-10 스케일로 변환 (0.5mm-5mm 범위 가정)
            return min(10, max(1, value * 2))
        elif key in ['flex', 'stiffness', 'smoothness', 'rattle']:
            # 0-1 범위를 1-10 스케일로 변환
            return min(10, max(1, value * 10))
        elif key in ['weight']:
            # 그램 단위의 무게를 1-10 스케일로 변환 (50g-500g 범위 가정)
            return min(10, max(1, value / 50))
        elif key in ['flex_level', 'size']:
            # 0-100 범위를 1-10 스케일로 변환
            return min(10, max(1, value / 10))
        else:
            # 기본적으로 값을 1-10 범위로 클리핑
            return min(10, max(1, float(value)))

    def recommend_switches(self, preferences: Dict) -> List[Dict]:
        """스위치 추천"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data('switches')
                
            cursor = conn.cursor(dictionary=True)
            query = """
            SELECT 
                *, 
                (sound_score + linear_score + tactile_score + weight_score + 
                 smoothness_score + speed_score + stability_score + durability_score) / 8 as overall_score
            FROM Switches 
            WHERE CURRENT_TIMESTAMP BETWEEN startdate AND enddate
            """
            cursor.execute(query)
            switches = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for switch in switches:
                for key, value in switch.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        switch[key] = float(value)
            
            # 유사도 점수 계산 및 정렬
            scored_switches = [
                {**switch, 'similarity_score': self.calculate_similarity_score(preferences, switch, 'switches')}
                for switch in switches
            ]
            
            return sorted(scored_switches, key=lambda x: x['similarity_score'], reverse=True)[:10]
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data('switches')
        except Exception as e:
            print(f"스위치 추천 중 오류 발생: {e}")
            return self._get_dummy_data('switches')
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def recommend_plate(self, preferences: Dict, switch_type: str) -> List[Dict]:
        """플레이트 추천"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data('plate')
                
            cursor = conn.cursor(dictionary=True)
            query = """
            SELECT 
                *, 
                (sound_profile + flex_level + weight + thickness) / 4 as overall_score
            FROM Plate 
            WHERE CURRENT_TIMESTAMP BETWEEN startdate AND enddate
            """
            cursor.execute(query)
            plates = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for plate in plates:
                for key, value in plate.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        plate[key] = float(value)
            
            # 스위치 타입에 따른 플레이트 특성 조정
            for plate in plates:
                if switch_type == 'Linear':
                    plate['compatibility_score'] = plate['flex_level'] * 0.7 + plate['sound_profile'] * 0.3
                elif switch_type == 'Tactile':
                    plate['compatibility_score'] = plate['flex_level'] * 0.5 + plate['sound_profile'] * 0.5
                else:  # Clicky
                    plate['compatibility_score'] = plate['sound_profile'] * 0.8 + plate['flex_level'] * 0.2
            
            scored_plates = [
                {**plate, 'similarity_score': self.calculate_similarity_score(preferences, plate, 'plate')}
                for plate in plates
            ]
            
            return sorted(scored_plates, key=lambda x: x['similarity_score'], reverse=True)[:10]
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data('plate')
        except Exception as e:
            print(f"플레이트 추천 중 오류 발생: {e}")
            return self._get_dummy_data('plate')
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def recommend_stabilizers(self, preferences: Dict) -> List[Dict]:
        """스태빌라이저 추천"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data('stabilizers')
                
            cursor = conn.cursor(dictionary=True)
            query = """
            SELECT 
                *, 
                (sound_profile + smoothness + build_quality - rattle) / 3 as overall_score
            FROM Stabilizer 
            WHERE CURRENT_TIMESTAMP BETWEEN startdate AND enddate
            """
            cursor.execute(query)
            stabilizers = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for stabilizer in stabilizers:
                for key, value in stabilizer.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        stabilizer[key] = float(value)
            
            scored_stabilizers = [
                {**stabilizer, 'similarity_score': self.calculate_similarity_score(preferences, stabilizer, 'stabilizers')}
                for stabilizer in stabilizers
            ]
            
            return sorted(scored_stabilizers, key=lambda x: x['similarity_score'], reverse=True)[:10]
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data('stabilizers')
        except Exception as e:
            print(f"스태빌라이저 추천 중 오류 발생: {e}")
            return self._get_dummy_data('stabilizers')
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def recommend_keycaps(self, preferences: Dict) -> List[Dict]:
        """키캡 추천"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data('keycaps')
                
            cursor = conn.cursor(dictionary=True)
            query = """
            SELECT 
                *, 
                (sound_profile + durability + thickness) / 3 as overall_score
            FROM Keycap 
            WHERE CURRENT_TIMESTAMP BETWEEN startdate AND enddate
            """
            cursor.execute(query)
            keycaps = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for keycap in keycaps:
                for key, value in keycap.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        keycap[key] = float(value)
            
            scored_keycaps = [
                {**keycap, 'similarity_score': self.calculate_similarity_score(preferences, keycap, 'keycaps')}
                for keycap in keycaps
            ]
            
            return sorted(scored_keycaps, key=lambda x: x['similarity_score'], reverse=True)[:10]
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data('keycaps')
        except Exception as e:
            print(f"키캡 추천 중 오류 발생: {e}")
            return self._get_dummy_data('keycaps')
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def recommend_pcb(self, preferences: Dict) -> List[Dict]:
        """PCB 추천"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data('pcb')
                
            cursor = conn.cursor(dictionary=True)
            query = """
            SELECT 
                *, 
                (build_quality + flex) / 2 as overall_score
            FROM PCB 
            WHERE CURRENT_TIMESTAMP BETWEEN startdate AND enddate
            """
            cursor.execute(query)
            pcbs = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for pcb in pcbs:
                for key, value in pcb.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        pcb[key] = float(value)
            
            scored_pcbs = [
                {**pcb, 'similarity_score': self.calculate_similarity_score(preferences, pcb, 'pcb')}
                for pcb in pcbs
            ]
            
            return sorted(scored_pcbs, key=lambda x: x['similarity_score'], reverse=True)[:10]
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data('pcb')
        except Exception as e:
            print(f"PCB 추천 중 오류 발생: {e}")
            return self._get_dummy_data('pcb')
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def get_component_stats(self):
        """카테고리별 부품 통계 조회"""
        cursor = None
        try:
            cursor = self.conn.cursor(dictionary=True)
            
            stats = {}
            categories = {
                'switches': 'Switches',
                'plate': 'Plate', 
                'stabilizers': 'Stabilizer',
                'keycaps': 'Keycap',
                'pcb': 'PCB'
            }
            
            for key, table in categories.items():
                cursor.execute(f"SELECT COUNT(*) as count FROM {table}")
                result = cursor.fetchone()
                stats[key] = result['count']
            
            return stats
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return {}
        finally:
            if cursor:
                cursor.close()

    def get_components_by_category(self, category):
        """카테고리별 부품 목록 조회"""
        conn = None
        cursor = None
        try:
            conn = self.get_db_connection()
            if not conn:
                return self._get_dummy_data(category)
            
            table_mapping = {
                'switches': 'Switches',
                'plate': 'Plate',
                'stabilizers': 'Stabilizer', 
                'keycaps': 'Keycap',
                'pcb': 'PCB'
            }
            
            if category not in table_mapping:
                return []
            
            table_name = table_mapping[category]
            cursor = conn.cursor(dictionary=True)
            cursor.execute(f"SELECT * FROM {table_name} ORDER BY name")
            components = cursor.fetchall()
            
            # Decimal 타입을 float로 변환
            for component in components:
                for key, value in component.items():
                    if hasattr(value, 'is_finite'):  # Decimal 타입 체크
                        component[key] = float(value)
            
            return components
            
        except mysql.connector.Error as err:
            print(f"데이터베이스 오류: {err}")
            return self._get_dummy_data(category)
        except Exception as e:
            print(f"기타 오류: {e}")
            return self._get_dummy_data(category)
        finally:
            if cursor:
                cursor.close()
            if conn:
                conn.close()

    def _get_dummy_data(self, category):
        """더미 데이터 반환"""
        dummy_data = {
            'switches': [
                {
                    'id': 1,
                    'name': '체리 MX 레드',
                    'type': 'Linear',
                    'stem_material': 'POM',
                    'sound_score': 6.0,
                    'linear_score': 9.0,
                    'tactile_score': 2.0,
                    'weight_score': 6.0,
                    'smoothness_score': 8.0,
                    'speed_score': 9.0,
                    'stability_score': 8.0,
                    'durability_score': 9.0,
                    'link': 'https://kbdfans.com/collections/switches'
                },
                {
                    'id': 2,
                    'name': '가테론 옐로우',
                    'type': 'Linear',
                    'stem_material': 'POM',
                    'sound_score': 7.0,
                    'linear_score': 8.0,
                    'tactile_score': 2.0,
                    'weight_score': 7.0,
                    'smoothness_score': 9.0,
                    'speed_score': 8.0,
                    'stability_score': 8.0,
                    'durability_score': 8.0,
                    'link': 'https://kbdfans.com/collections/switches'
                }
            ],
            'keycaps': [
                {
                    'id': 1,
                    'name': 'Cherry 프로파일 PBT 키캡',
                    'material': 'PBT',
                    'profile': 'Cherry',
                    'thickness': 1.4,
                    'sound_profile': 7.0,
                    'durability': 9.0,
                    'rgb_compatible': True,
                    'link': 'https://kbdfans.com/collections/keycaps'
                }
            ],
            'pcb': [
                {
                    'id': 1,
                    'name': 'DZ60 RGB PCB',
                    'build_quality': 8.0,
                    'features': 'RGB, 핫스왑',
                    'rgb_support': True,
                    'qmk_via': True,
                    'flex': 6.0,
                    'link': 'https://kbdfans.com/collections/pcb'
                }
            ],
            'plate': [
                {
                    'id': 1,
                    'name': '알루미늄 플레이트',
                    'material': 'Aluminum',
                    'thickness': 1.5,
                    'flex_level': 5,
                    'sound_profile': 7.0,
                    'weight': 150,
                    'link': 'https://kbdfans.com/collections/plate'
                }
            ],
            'stabilizers': [
                {
                    'id': 1,
                    'name': 'Cherry 클립인 스테빌라이저',
                    'type': 'Clip-in',
                    'material': 'PC',
                    'sound_profile': 6.0,
                    'smoothness': 7.0,
                    'rattle': 3.0,
                    'build_quality': 8.0,
                    'link': 'https://kbdfans.com/collections/stabilizer'
                }
            ]
        }
        
        return dummy_data.get(category, [])

    def recommend_complete_set(self, preferences: Dict) -> Dict:
        """전체 키보드 세트 추천"""
        try:
            # 각 카테고리별 추천 생성
            switch_type = preferences.get('switch_type', 'Linear')
            
            recommendations = {
                'switches': self.recommend_switches(preferences),
                'plate': self.recommend_plate(preferences, switch_type),
                'stabilizers': self.recommend_stabilizers(preferences),
                'keycaps': self.recommend_keycaps(preferences),
                'pcb': self.recommend_pcb(preferences)
            }
            
            # 포맷된 결과 생성
            formatted_result = format_recommendations(recommendations)
            
            return {
                'success': True,
                'preferences': preferences,
                'recommendations': recommendations,
                'formatted_result': formatted_result,
                'total_components': sum(len(items) for items in recommendations.values())
            }
            
        except Exception as e:
            print(f"전체 세트 추천 중 오류 발생: {e}")
            return {
                'success': False,
                'error': str(e),
                'recommendations': {},
                'formatted_result': '추천 생성 중 오류가 발생했습니다.',
                'total_components': 0
            }

def format_recommendations(recommendations: Dict[str, List[Dict]]) -> str:
    """추천 결과를 보기 좋게 포맷팅"""
    
    # 카테고리별 한글 이름 매핑
    category_names = {
        'switches': '스위치',
        'plate': '플레이트',
        'stabilizers': '스태빌라이저',
        'keycaps': '키캡',
        'pcb': 'PCB'
    }
    
    # 가격 티어 매핑
    price_tier_names = {
        1: '엔트리급 (1티어)',
        2: '중급 (2티어)',
        3: '고급 (3티어)',
        4: '프리미엄 (4티어)'
    }
    
    result = []
    result.append("\n=== 키보드 부품 추천 결과 ===\n")
    
    for category, items in recommendations.items():
        result.append(f"▶ {category_names.get(category, category)}")
        if not items:
            result.append("   추천할 수 있는 제품이 없습니다.")
            result.append("")
            continue
        
        for i, item in enumerate(items[:5], 1):
            try:
                name = item.get('name', 'Unknown')
                price_tier = item.get('price_tier', 0)
                price_tier_str = price_tier_names.get(int(price_tier), '가격 정보 없음')
                link = item.get('link', 'N/A')
                
                result.append(f"\n   {i}. {name}")
                result.append(f"      ⊙ 가격대: {price_tier_str}")
                result.append(f"      ⊙ 구매링크: {link}")
                
                # 카테고리별 중요 특성 출력
                if category == 'switches':
                    result.extend([
                        f"      ⊙ 타입: {item.get('type', 'N/A')}",
                        f"      ⊙ 소리: {item.get('sound_score', 0):.1f}/10",
                        f"      ⊙ 부드러움: {item.get('smoothness_score', 0):.1f}/10",
                        f"      ⊙ 속도: {item.get('speed_score', 0):.1f}/10",
                        f"      ⊙ 안정성: {item.get('stability_score', 0):.1f}/10",
                        f"      ⊙ 재질: {item.get('stem_material', 'N/A')}"
                    ])
                elif category == 'plate':
                    result.extend([
                        f"      ⊙ 재질: {item.get('material', 'N/A')}",
                        f"      ⊙ 두께: {item.get('thickness', 0):.1f}mm",
                        f"      ⊙ 유연성: {item.get('flex_level', 0)}/10",
                        f"      ⊙ 소리: {item.get('sound_profile', 0):.1f}/10",
                        f"      ⊙ 무게: {item.get('weight', 0)}g"
                    ])
                elif category == 'stabilizers':
                    result.extend([
                        f"      ⊙ 타입: {item.get('type', 'N/A')}",
                        f"      ⊙ 재질: {item.get('material', 'N/A')}",
                        f"      ⊙ 소리: {item.get('sound_profile', 0):.1f}/10",
                        f"      ⊙ 부드러움: {item.get('smoothness', 0):.1f}/10",
                        f"      ⊙ 흔들림: {item.get('rattle', 0):.1f}/10 (낮을수록 좋음)",
                        f"      ⊙ 품질: {item.get('build_quality', 0):.1f}/10"
                    ])
                elif category == 'keycaps':
                    result.extend([
                        f"      ⊙ 재질: {item.get('material', 'N/A')}",
                        f"      ⊙ 프로파일: {item.get('profile', 'N/A')}",
                        f"      ⊙ 두께: {float(item.get('thickness', 0)):.1f}mm",
                        f"      ⊙ 소리: {float(item.get('sound_profile', 0)):.1f}/10",
                        f"      ⊙ 내구성: {float(item.get('durability', 0)):.1f}/10",
                        f"      ⊙ RGB 호환: {'예' if item.get('rgb_compatible') else '아니오'}"
                    ])
                elif category == 'pcb':
                    features = item.get('features', '').split(',')
                    features_str = ', '.join(f.strip() for f in features) if features else 'N/A'
                    result.extend([
                        f"      ⊙ 품질: {item.get('build_quality', 0):.1f}/10",
                        f"      ⊙ 기능: {features_str}",
                        f"      ⊙ RGB: {'지원' if item.get('rgb_support') else '미지원'}",
                        f"      ⊙ QMK/VIA: {'지원' if item.get('qmk_via') else '미지원'}",
                        f"      ⊙ 유연성: {item.get('flex', 0):.1f}/10"
                    ])
            except Exception as e:
                result.append(f"      ⊙ 항목 포맷팅 중 오류: {e}")
            
            result.append("")  # 항목 간 구분을 위한 빈 줄
    
    return '\n'.join(result)

# 사용 예시
if __name__ == "__main__":
    recommender = KeyboardRecommender()
    
    # 사용자 선호도 예시
    preferences = {
        # 소리 관련
        'sound_profile': 7,    # 조용한 소리 선호 (1-10)
        'sound_score': 3,      # 낮은 소리 선호 (1-10)
        'sound_level': 3,      # 낮은 소리 선호 (1-10)
        
        # 가격 관련
        'price_tier': 2,       # 중급 제품 선호 (1-4)
        
        # 품질 관련
        'build_quality': 8,    # 높은 품질 선호 (1-10)
        'durability_score': 8, # 높은 내구성 선호 (1-10)
        
        # 타입 관련
        'switch_type': 'linear', # 리니어 스위치 선호
        'type': 'linear',      # 리니어 선호
        
        # 재질 관련
        'material': 'POM',     # POM 재질 선호
        'stem_material': 'POM', # POM 재질 선호
        
        # 특성 관련
        'smoothness': 8,       # 부드러운 타감 선호 (1-10)
        'smoothness_score': 8, # 부드러운 타감 선호 (1-10)
        'speed_score': 7,      # 빠른 반응 선호 (1-10)
        'stability_score': 8,  # 안정적인 타감 선호 (1-10)
        'linear_score': 9,     # 선형적인 타감 선호 (1-10)
        'tactile_score': 3,    # 촉각 선호도 낮음 (1-10)
        
        # 물리적 특성
        'stiffness': 6,        # 중간 정도의 강성 선호 (1-10)
        'flex': 7,            # 적당한 플렉스 선호 (1-10)
        'weight_score': 6,    # 중간 정도의 무게 선호 (1-10)
        
        # 기타 특성
        'profile': 'cherry',   # Cherry 프로파일 선호
        'rgb_compatible': True, # RGB 호환성 필요
        'qmk_via': True       # QMK/VIA 지원 필요
    }
    
    # 전체 세트 추천
    recommendations = recommender.recommend_complete_set(preferences)
    
    # 결과 출력
    print(recommendations['formatted_result']) 