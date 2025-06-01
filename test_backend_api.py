#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import sys

class BackendAPITester:
    def __init__(self, base_url="http://localhost:5002"):
        self.base_url = base_url
        
    def test_simple_recommend(self, message):
        """간편 추천 API 테스트"""
        print(f"🔍 테스트: {message}")
        print("=" * 60)
        
        url = f"{self.base_url}/api/simple-recommend"
        data = {"message": message}
        
        try:
            response = requests.post(url, json=data, timeout=30)
            
            if response.status_code == 200:
                result = response.json()
                
                if result.get('success'):
                    print("✅ 추천 성공!")
                    print(f"📝 요청: {result['user_request']}")
                    print()
                    
                    # 선호도 요약 출력
                    pref = result['preference_summary']
                    print("📊 해석된 선호도:")
                    print(f"  - 스위치: {pref['switch_type']}")
                    print(f"  - 소음: {pref['sound_level']}/10 ({pref['sound_description']})")
                    print(f"  - 가격: {pref['price_description']}")
                    print(f"  - RGB: {pref['rgb_description']}")
                    print()
                    
                    # 추천 결과 출력
                    print("🎯 추천 결과:")
                    for category, data in result['recommendations'].items():
                        print(f"\n🔧 {data['name']}:")
                        for i, item in enumerate(data['items'][:2], 1):  # 상위 2개만 표시
                            print(f"  {i}. {item['name']} ({item['price_tier_text']})")
                            if 'type' in item:
                                print(f"     타입: {item['type']}")
                            if 'material' in item:
                                print(f"     재질: {item['material']}")
                    
                    print(f"\n📈 총 {result['total_components']}개 부품 추천")
                    
                    # AI 설명 일부 출력
                    explanation = result['ai_explanation']
                    if explanation:
                        print("\n🤖 AI 설명 (일부):")
                        lines = explanation.split('\n')[:5]  # 처음 5줄만
                        for line in lines:
                            if line.strip():
                                print(f"  {line[:80]}...")
                                break
                    
                else:
                    print(f"❌ 추천 실패: {result.get('message', 'Unknown error')}")
                    
            else:
                print(f"❌ HTTP 오류: {response.status_code}")
                print(response.text)
                
        except requests.exceptions.RequestException as e:
            print(f"❌ 연결 오류: {e}")
        except Exception as e:
            print(f"❌ 예상치 못한 오류: {e}")
            
        print("\n" + "=" * 60)
    
    def test_quick_recommend(self, message):
        """빠른 추천 API 테스트 (GET 방식)"""
        print(f"⚡ 빠른 테스트: {message}")
        print("=" * 60)
        
        # URL 인코딩
        import urllib.parse
        encoded_message = urllib.parse.quote(message)
        url = f"{self.base_url}/api/quick-recommend/{encoded_message}"
        
        try:
            response = requests.get(url, timeout=30)
            
            if response.status_code == 200:
                result = response.json()
                
                if result.get('success'):
                    print("✅ 빠른 추천 성공!")
                    print(f"📝 요청: {result['user_request']}")
                    
                    # 간단한 결과만 출력
                    recommendations = result['recommendations']
                    total = sum(len(items) for items in recommendations.values())
                    print(f"🎯 {total}개 부품 추천 완료")
                    
                    # 카테고리별 첫 번째 추천만 출력
                    for category, items in recommendations.items():
                        if items:
                            first_item = items[0]
                            print(f"  {category}: {first_item['name']}")
                else:
                    print(f"❌ 추천 실패: {result.get('message', 'Unknown error')}")
                    
            else:
                print(f"❌ HTTP 오류: {response.status_code}")
                
        except Exception as e:
            print(f"❌ 오류: {e}")
            
        print("\n" + "=" * 60)
    
    def test_server_status(self):
        """서버 상태 확인"""
        print("🏥 서버 상태 확인")
        print("=" * 60)
        
        try:
            # 홈페이지 확인
            response = requests.get(f"{self.base_url}/", timeout=5)
            if response.status_code == 200:
                info = response.json()
                print(f"✅ 서버 실행 중 - {info['message']}")
                print(f"📦 버전: {info['version']}")
            
            # 헬스체크 확인
            response = requests.get(f"{self.base_url}/api/health", timeout=5)
            if response.status_code == 200:
                health = response.json()
                print(f"💚 헬스체크: {health['status']}")
            
        except Exception as e:
            print(f"❌ 서버 연결 실패: {e}")
            print("서버가 실행 중인지 확인해주세요: python3 app.py")
            
        print("\n" + "=" * 60)

def main():
    """메인 함수"""
    tester = BackendAPITester()
    
    print("🎯 KLUE 키보드 추천 시스템 - 백엔드 API 테스터")
    print("=" * 60)
    
    # 서버 상태 확인
    tester.test_server_status()
    
    # 테스트 케이스들
    test_cases = [
        "조용한 커스텀키보드 부품 추천해줘",
        "게이밍용 고급 키보드 만들고 싶어",
        "사무실에서 쓸 조용한 키보드",
        "프리미엄 타이핑용 키보드"
    ]
    
    print("📝 간편 추천 API 테스트 (POST)")
    print("=" * 60)
    
    for i, test_case in enumerate(test_cases, 1):
        print(f"\n[테스트 {i}/{len(test_cases)}]")
        tester.test_simple_recommend(test_case)
        
        if i < len(test_cases):
            input("계속하려면 Enter를 누르세요...")
    
    print("\n⚡ 빠른 추천 API 테스트 (GET)")
    print("=" * 60)
    
    # 빠른 추천 테스트
    tester.test_quick_recommend("조용한 게이밍 키보드")
    
    print("🎉 모든 테스트 완료!")
    print("=" * 60)
    print("💡 사용법:")
    print("  POST: curl -X POST http://localhost:5002/api/simple-recommend -H 'Content-Type: application/json' -d '{\"message\":\"조용한 키보드\"}'")
    print("  GET:  curl http://localhost:5002/api/quick-recommend/조용한%20키보드")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n👋 테스트를 중단합니다.")
    except Exception as e:
        print(f"\n❌ 예상치 못한 오류: {e}") 