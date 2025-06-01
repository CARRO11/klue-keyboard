from flask import Flask, request, jsonify, Response
from flask_cors import CORS
import json
from decimal import Decimal
from keyboard_recommender import KeyboardRecommender
from ai_recommender import AIRecommender

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False  # 한글 인코딩 설정
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True
CORS(app)  # React 클라이언트와의 통신을 위해 CORS 활성화

# 추천 시스템 인스턴스 생성
recommender = KeyboardRecommender()
ai_recommender = AIRecommender()

def convert_decimals(obj):
    """Decimal 타입을 float로 변환하는 재귀 함수"""
    if isinstance(obj, Decimal):
        return float(obj)
    elif isinstance(obj, dict):
        return {key: convert_decimals(value) for key, value in obj.items()}
    elif isinstance(obj, list):
        return [convert_decimals(item) for item in obj]
    else:
        return obj

def format_component_for_display(component, category):
    """컴포넌트를 표시용으로 포맷팅"""
    formatted = {
        'name': component['name'],
        'price_tier': component.get('price_tier', 1),
        'link': component.get('link', ''),
    }
    
    # 가격대 텍스트 추가
    price_tiers = {1: '엔트리급', 2: '중급', 3: '고급', 4: '프리미엄'}
    formatted['price_tier_text'] = price_tiers.get(int(component.get('price_tier', 1)), '미정')
    
    # 카테고리별 주요 정보 추가
    if category == 'switches':
        formatted.update({
            'type': component.get('type', 'N/A'),
            'sound_score': component.get('sound_score', 'N/A'),
            'smoothness_score': component.get('smoothness_score', 'N/A'),
            'speed_score': component.get('speed_score', 'N/A')
        })
    elif category == 'plate':
        formatted.update({
            'material': component.get('material', 'N/A'),
            'thickness': component.get('thickness', 'N/A')
        })
    elif category == 'stabilizers':
        formatted.update({
            'type': component.get('type', 'N/A'),
            'smoothness': component.get('smoothness', 'N/A')
        })
    elif category == 'keycaps':
        formatted.update({
            'material': component.get('material', 'N/A'),
            'profile': component.get('profile', 'N/A')
        })
    elif category == 'pcb':
        formatted.update({
            'rgb_support': component.get('rgb_support', False),
            'rgb_status': '지원' if component.get('rgb_support') else '미지원',
            'build_quality': component.get('build_quality', 'N/A')
        })
    
    return formatted

@app.route('/')
def home():
    """홈페이지"""
    return jsonify({
        "message": "Klue 키보드 추천 시스템 API",
        "version": "2.0.0",
        "description": "자연어로 키보드 부품을 추천받을 수 있는 AI 시스템",
        "endpoints": {
            "기본 API": [
                "/api/health - 서버 상태 확인",
                "/api/components - 부품 통계 조회",
                "/api/components/<category> - 카테고리별 부품 목록",
                "/api/preferences/templates - 선호도 템플릿"
            ],
            "추천 API": [
                "/api/recommend (POST) - 상세 선호도 기반 추천",
                "/api/recommend/natural (POST) - 자연어 기반 추천"
            ],
            "간편 API (NEW)": [
                "/api/simple-recommend (POST) - 텍스트 입력으로 간편 추천",
                "/api/quick-recommend/<message> (GET) - URL로 빠른 추천"
            ]
        },
        "usage_examples": {
            "simple_recommend": {
                "method": "POST",
                "url": "/api/simple-recommend",
                "body": {"message": "조용한 커스텀키보드 부품 추천해줘"},
                "description": "가장 간단한 방법으로 전체 추천 받기"
            },
            "quick_recommend": {
                "method": "GET", 
                "url": "/api/quick-recommend/조용한%20게이밍%20키보드",
                "description": "URL에 직접 요청을 넣어서 빠른 추천 받기"
            }
        },
        "features": [
            "🤖 AI 기반 자연어 처리",
            "🎯 개인 맞춤형 부품 추천", 
            "💬 구어체 친근한 설명",
            "🔗 구매 링크 제공",
            "📊 상세한 부품 정보"
        ]
    })

@app.route('/api/health')
def health_check():
    """서버 상태 확인"""
    return jsonify({"status": "healthy", "service": "keyboard-recommender"})

@app.route('/api/recommend', methods=['POST'])
def get_recommendations():
    """키보드 부품 추천 API"""
    try:
        # 요청 데이터 받기
        data = request.get_json()
        
        # 필수 필드 검증
        required_fields = ['switch_type', 'sound_profile', 'tactile_score', 'speed_score', 'price_tier']
        for field in required_fields:
            if field not in data:
                return jsonify({"error": f"Missing required field: {field}"}), 400
        
        # 선호도 데이터 구성
        preferences = {
            'switch_type': data['switch_type'],
            'sound_profile': int(data['sound_profile']),
            'tactile_score': int(data['tactile_score']),
            'speed_score': int(data['speed_score']),
            'price_tier': int(data['price_tier']),
            'build_quality': int(data.get('build_quality', 8)),
            'rgb_compatible': data.get('rgb_compatible', True)
        }
        
        # 추천 실행
        complete_result = recommender.recommend_complete_set(preferences)
        recommendations = complete_result.get('recommendations', {})
        
        # AI 설명 생성
        ai_explanation = ai_recommender.generate_recommendation_explanation(
            recommendations, preferences
        )
        
        # 응답 데이터 구성
        response = {
            "success": True,
            "preferences": preferences,
            "recommendations": recommendations,
            "ai_explanation": ai_explanation,
            "total_components": sum(len(items) for items in recommendations.values())
        }
        
        # Decimal 타입 변환 및 한글 인코딩 문제 해결
        response = convert_decimals(response)
        json_response = json.dumps(response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8')
        
    except Exception as e:
        error_response = {
            "success": False,
            "error": str(e),
            "message": "추천 생성 중 오류가 발생했습니다."
        }
        json_response = json.dumps(error_response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8'), 500

@app.route('/api/recommend/natural', methods=['POST'])
def get_natural_language_recommendations():
    """자연어 입력을 통한 키보드 부품 추천 API"""
    try:
        # 요청 데이터 받기
        data = request.get_json()
        
        if 'message' not in data:
            return jsonify({"error": "Missing required field: message"}), 400
        
        user_message = data['message']
        
        # 자연어를 선호도로 변환
        preferences = ai_recommender.parse_natural_language_to_preferences(user_message)
        
        if not preferences:
            return jsonify({
                "success": False,
                "error": "자연어 해석에 실패했습니다.",
                "message": "더 구체적으로 설명해주세요. 예: '조용한 게이밍 키보드', '고급 타이핑용 키보드'"
            }), 400
        
        # 추천 실행
        complete_result = recommender.recommend_complete_set(preferences)
        recommendations = complete_result.get('recommendations', {})
        
        # AI 설명 생성 (자연어 요청 포함)
        ai_explanation = ai_recommender.generate_natural_language_explanation(
            user_message, recommendations, preferences
        )
        
        # 응답 데이터 구성
        response = {
            "success": True,
            "user_request": user_message,
            "interpreted_preferences": preferences,
            "recommendations": recommendations,
            "ai_explanation": ai_explanation,
            "total_components": sum(len(items) for items in recommendations.values())
        }
        
        # Decimal 타입 변환 및 한글 인코딩 문제 해결
        response = convert_decimals(response)
        json_response = json.dumps(response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8')
        
    except Exception as e:
        error_response = {
            "success": False,
            "error": str(e),
            "message": "자연어 추천 생성 중 오류가 발생했습니다."
        }
        json_response = json.dumps(error_response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8'), 500

@app.route('/api/components', methods=['GET'])
def get_all_components():
    """모든 부품 목록 조회"""
    try:
        # 카테고리별 부품 수 조회
        stats = recommender.get_component_stats()
        
        return jsonify({
            "success": True,
            "stats": stats,
            "message": "부품 통계 조회 성공"
        })
        
    except Exception as e:
        return jsonify({
            "success": False,
            "error": str(e),
            "message": "부품 정보 조회 중 오류가 발생했습니다."
        }), 500

@app.route('/api/components/<category>', methods=['GET'])
def get_components_by_category(category):
    """카테고리별 부품 목록 조회"""
    try:
        valid_categories = ['switches', 'plate', 'stabilizers', 'keycaps', 'pcb']
        if category not in valid_categories:
            return jsonify({
                "success": False,
                "error": f"Invalid category. Valid categories: {valid_categories}"
            }), 400
        
        components = recommender.get_components_by_category(category)
        
        return jsonify({
            "success": True,
            "category": category,
            "components": components,
            "count": len(components)
        })
        
    except Exception as e:
        return jsonify({
            "success": False,
            "error": str(e),
            "message": f"{category} 부품 조회 중 오류가 발생했습니다."
        }), 500

@app.route('/api/preferences/templates', methods=['GET'])
def get_preference_templates():
    """미리 정의된 선호도 템플릿 제공"""
    templates = {
        "gaming": {
            "name": "게이밍",
            "description": "빠른 반응속도와 정확성을 중시하는 게이머용",
            "preferences": {
                "switch_type": "Linear",
                "sound_profile": 5,
                "tactile_score": 3,
                "speed_score": 9,
                "price_tier": 3,
                "build_quality": 8,
                "rgb_compatible": True
            }
        },
        "office": {
            "name": "사무용",
            "description": "조용하고 편안한 타이핑을 위한 사무환경용",
            "preferences": {
                "switch_type": "Linear",
                "sound_profile": 3,
                "tactile_score": 2,
                "speed_score": 6,
                "price_tier": 2,
                "build_quality": 7,
                "rgb_compatible": False
            }
        },
        "typing": {
            "name": "타이핑",
            "description": "장시간 타이핑에 최적화된 설정",
            "preferences": {
                "switch_type": "Tactile",
                "sound_profile": 6,
                "tactile_score": 7,
                "speed_score": 7,
                "price_tier": 3,
                "build_quality": 9,
                "rgb_compatible": False
            }
        },
        "premium": {
            "name": "프리미엄",
            "description": "최고급 부품으로 구성된 프리미엄 키보드",
            "preferences": {
                "switch_type": "Tactile",
                "sound_profile": 8,
                "tactile_score": 8,
                "speed_score": 8,
                "price_tier": 4,
                "build_quality": 10,
                "rgb_compatible": True
            }
        }
    }
    
    return jsonify({
        "success": True,
        "templates": templates
    })

@app.route('/api/simple-recommend', methods=['POST'])
def simple_recommend():
    """간단한 텍스트 입력으로 전체 추천 받기 (CLI 백엔드 버전)"""
    try:
        # 요청 데이터 받기
        data = request.get_json()
        
        if 'message' not in data:
            return Response(json.dumps({
                "success": False,
                "error": "Missing required field: message"
            }, ensure_ascii=False), content_type='application/json; charset=utf-8'), 400
        
        user_message = data['message'].strip()
        
        if not user_message:
            return Response(json.dumps({
                "success": False,
                "error": "Empty message",
                "message": "요청 내용을 입력해주세요."
            }, ensure_ascii=False), content_type='application/json; charset=utf-8'), 400
        
        # 1. 자연어를 선호도로 변환
        preferences = ai_recommender.parse_natural_language_to_preferences(user_message)
        
        if not preferences:
            return Response(json.dumps({
                "success": False,
                "error": "Failed to parse natural language",
                "message": "요청을 이해하지 못했습니다. 더 구체적으로 설명해주세요.",
                "examples": [
                    "조용한 게이밍 키보드",
                    "고급 타이핑용 키보드",
                    "사무실에서 쓸 조용한 키보드"
                ]
            }, ensure_ascii=False), content_type='application/json; charset=utf-8'), 400
        
        # 2. 추천 실행
        complete_result = recommender.recommend_complete_set(preferences)
        recommendations = complete_result.get('recommendations', {})
        
        if not recommendations:
            return Response(json.dumps({
                "success": False,
                "error": "No recommendations generated",
                "message": "추천 결과를 생성할 수 없습니다."
            }, ensure_ascii=False), content_type='application/json; charset=utf-8'), 500
        
        # 3. AI 설명 생성
        ai_explanation = ai_recommender.generate_natural_language_explanation(
            user_message, recommendations, preferences
        )
        
        # 4. 표시용 데이터 포맷팅
        formatted_recommendations = {}
        category_names = {
            'switches': '스위치',
            'plate': '플레이트', 
            'stabilizers': '스태빌라이저',
            'keycaps': '키캡',
            'pcb': 'PCB'
        }
        
        for category, components in recommendations.items():
            if components:
                formatted_recommendations[category] = {
                    'name': category_names.get(category, category),
                    'items': [format_component_for_display(comp, category) for comp in components[:3]]
                }
        
        # 5. 선호도 요약 생성
        price_names = {1: '엔트리급', 2: '중급', 3: '고급', 4: '프리미엄'}
        sound_level = preferences.get('sound_profile', 5)
        
        preference_summary = {
            'switch_type': preferences.get('switch_type', 'N/A'),
            'sound_level': sound_level,
            'sound_description': '조용함' if sound_level < 5 else '보통' if sound_level < 7 else '시끄러움',
            'price_tier': preferences.get('price_tier', 2),
            'price_description': price_names.get(preferences.get('price_tier', 2), '미정'),
            'rgb_compatible': preferences.get('rgb_compatible', False),
            'rgb_description': '선호함' if preferences.get('rgb_compatible') else '선호하지 않음'
        }
        
        # 응답 데이터 구성
        response = {
            "success": True,
            "user_request": user_message,
            "preference_summary": preference_summary,
            "recommendations": formatted_recommendations,
            "ai_explanation": ai_explanation,
            "total_components": sum(len(items['items']) for items in formatted_recommendations.values()),
            "raw_preferences": preferences,
            "raw_recommendations": recommendations
        }
        
        # Decimal 타입 변환 및 한글 인코딩 문제 해결
        response = convert_decimals(response)
        json_response = json.dumps(response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8')
        
    except Exception as e:
        error_response = {
            "success": False,
            "error": str(e),
            "message": "추천 생성 중 오류가 발생했습니다."
        }
        json_response = json.dumps(error_response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8'), 500

@app.route('/api/quick-recommend/<path:message>', methods=['GET'])
def quick_recommend(message):
    """URL 파라미터로 빠른 추천 받기"""
    try:
        # URL 디코딩
        import urllib.parse
        decoded_message = urllib.parse.unquote(message)
        
        # POST 요청과 동일한 로직 사용
        data = {'message': decoded_message}
        
        # simple_recommend 로직 재사용
        return simple_recommend_logic(decoded_message)
        
    except Exception as e:
        error_response = {
            "success": False,
            "error": str(e),
            "message": "빠른 추천 생성 중 오류가 발생했습니다."
        }
        json_response = json.dumps(error_response, ensure_ascii=False, indent=2)
        return Response(json_response, content_type='application/json; charset=utf-8'), 500

def simple_recommend_logic(user_message):
    """simple_recommend의 핵심 로직을 재사용 가능하게 분리"""
    # 1. 자연어를 선호도로 변환
    preferences = ai_recommender.parse_natural_language_to_preferences(user_message)
    
    if not preferences:
        return Response(json.dumps({
            "success": False,
            "error": "Failed to parse natural language",
            "message": "요청을 이해하지 못했습니다."
        }, ensure_ascii=False), content_type='application/json; charset=utf-8'), 400
    
    # 2. 추천 실행
    complete_result = recommender.recommend_complete_set(preferences)
    recommendations = complete_result.get('recommendations', {})
    
    # 3. AI 설명 생성
    ai_explanation = ai_recommender.generate_natural_language_explanation(
        user_message, recommendations, preferences
    )
    
    # 4. 간단한 응답 구성
    response = {
        "success": True,
        "user_request": user_message,
        "recommendations": recommendations,
        "ai_explanation": ai_explanation,
        "preferences": preferences
    }
    
    response = convert_decimals(response)
    json_response = json.dumps(response, ensure_ascii=False, indent=2)
    return Response(json_response, content_type='application/json; charset=utf-8')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5002) 