#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import json
from keyboard_recommender import KeyboardRecommender
from ai_recommender import AIRecommender

class KeyboardCLI:
    def __init__(self):
        self.recommender = KeyboardRecommender()
        self.ai_recommender = AIRecommender()
        
    def print_banner(self):
        """프로그램 시작 배너 출력"""
        print("=" * 60)
        print("🎯 KLUE 키보드 부품 추천 시스템 CLI")
        print("=" * 60)
        print("💬 자연어로 원하는 키보드를 설명해주세요!")
        print("예시:")
        print("  - '조용한 커스텀키보드 부품 추천해줘'")
        print("  - '게이밍용 고급 키보드 만들고 싶어'")
        print("  - '사무실에서 쓸 조용한 키보드'")
        print("  - '프리미엄 타이핑용 키보드'")
        print()
        print("종료하려면 'quit' 또는 'exit'를 입력하세요.")
        print("=" * 60)
        print()

    def print_recommendations(self, recommendations):
        """추천 결과를 보기 좋게 출력"""
        print("\n🎯 추천 결과")
        print("=" * 50)
        
        category_names = {
            'switches': '🔧 스위치',
            'plate': '🔧 플레이트', 
            'stabilizers': '🔧 스태빌라이저',
            'keycaps': '🔧 키캡',
            'pcb': '🔧 PCB'
        }
        
        for category, components in recommendations.items():
            if not components:
                continue
                
            print(f"\n{category_names.get(category, category.upper())}")
            print("-" * 30)
            
            # 상위 3개 추천 표시
            for i, component in enumerate(components[:3], 1):
                print(f"{i}. {component['name']}")
                
                # 카테고리별 주요 정보 표시
                if category == 'switches':
                    print(f"   타입: {component.get('type', 'N/A')}")
                    print(f"   소리: {component.get('sound_score', 'N/A')}/10")
                    print(f"   부드러움: {component.get('smoothness_score', 'N/A')}/10")
                    
                elif category == 'plate':
                    print(f"   재질: {component.get('material', 'N/A')}")
                    print(f"   두께: {component.get('thickness', 'N/A')}mm")
                    
                elif category == 'stabilizers':
                    print(f"   타입: {component.get('type', 'N/A')}")
                    print(f"   부드러움: {component.get('smoothness', 'N/A')}/10")
                    
                elif category == 'keycaps':
                    print(f"   재질: {component.get('material', 'N/A')}")
                    print(f"   프로파일: {component.get('profile', 'N/A')}")
                    
                elif category == 'pcb':
                    rgb_status = '지원' if component.get('rgb_support') else '미지원'
                    print(f"   RGB: {rgb_status}")
                    print(f"   품질: {component.get('build_quality', 'N/A')}/10")
                
                # 가격대 표시
                price_tiers = {1: '엔트리급', 2: '중급', 3: '고급', 4: '프리미엄'}
                price_tier = price_tiers.get(int(component.get('price_tier', 1)), '미정')
                print(f"   가격대: {price_tier}")
                
                # 구매 링크
                if component.get('link'):
                    print(f"   구매링크: {component['link']}")
                
                print()

    def print_ai_explanation(self, explanation):
        """AI 설명을 보기 좋게 출력"""
        print("\n🤖 AI 추천 설명")
        print("=" * 50)
        
        # 긴 설명을 적절히 나누어 출력
        lines = explanation.split('\n')
        for line in lines:
            if line.strip():
                # 긴 줄은 적절히 나누어 출력
                if len(line) > 80:
                    words = line.split(' ')
                    current_line = ""
                    for word in words:
                        if len(current_line + word) > 80:
                            print(current_line.strip())
                            current_line = word + " "
                        else:
                            current_line += word + " "
                    if current_line.strip():
                        print(current_line.strip())
                else:
                    print(line)
            else:
                print()

    def get_user_input(self):
        """사용자 입력 받기"""
        try:
            user_input = input("💬 원하는 키보드를 설명해주세요: ").strip()
            return user_input
        except KeyboardInterrupt:
            print("\n\n👋 프로그램을 종료합니다.")
            return None
        except EOFError:
            print("\n\n👋 프로그램을 종료합니다.")
            return None

    def process_request(self, user_message):
        """사용자 요청 처리"""
        print(f"\n🔍 '{user_message}' 분석 중...")
        
        try:
            # 1. 자연어를 선호도로 변환
            print("📝 AI가 요청을 분석하고 있습니다...")
            preferences = self.ai_recommender.parse_natural_language_to_preferences(user_message)
            
            if not preferences:
                print("❌ 요청을 이해하지 못했습니다. 더 구체적으로 설명해주세요.")
                print("예: '조용한 게이밍 키보드', '고급 타이핑용 키보드'")
                return
            
            print("✅ 요청 분석 완료!")
            
            # 해석된 선호도 표시
            print("\n📊 해석된 선호도:")
            switch_type = preferences.get('switch_type', 'N/A')
            sound_level = preferences.get('sound_profile', 5)
            price_tier = preferences.get('price_tier', 2)
            price_names = {1: '엔트리급', 2: '중급', 3: '고급', 4: '프리미엄'}
            
            print(f"  - 스위치 타입: {switch_type}")
            print(f"  - 소음 수준: {sound_level}/10 ({'조용함' if sound_level < 5 else '보통' if sound_level < 7 else '시끄러움'})")
            print(f"  - 가격대: {price_names.get(price_tier, '미정')}")
            print(f"  - RGB: {'선호함' if preferences.get('rgb_compatible') else '선호하지 않음'}")
            
            # 2. 추천 실행
            print("\n🎯 최적의 부품을 찾고 있습니다...")
            complete_result = self.recommender.recommend_complete_set(preferences)
            recommendations = complete_result.get('recommendations', {})
            
            if not recommendations:
                print("❌ 추천 결과를 생성할 수 없습니다.")
                return
            
            # 3. AI 설명 생성
            print("🤖 AI가 설명을 준비하고 있습니다...")
            ai_explanation = self.ai_recommender.generate_natural_language_explanation(
                user_message, recommendations, preferences
            )
            
            # 4. 결과 출력
            self.print_recommendations(recommendations)
            self.print_ai_explanation(ai_explanation)
            
            # 총 부품 수 표시
            total_components = sum(len(items) for items in recommendations.values())
            print(f"\n📈 총 {total_components}개의 부품을 추천했습니다!")
            
        except Exception as e:
            print(f"❌ 오류가 발생했습니다: {str(e)}")
            print("다시 시도해주세요.")

    def run(self):
        """메인 실행 루프"""
        self.print_banner()
        
        while True:
            user_input = self.get_user_input()
            
            if user_input is None:
                break
                
            if user_input.lower() in ['quit', 'exit', '종료', 'q']:
                print("\n👋 키보드 추천 시스템을 종료합니다. 좋은 키보드 라이프 되세요!")
                break
                
            if not user_input:
                print("❌ 입력이 비어있습니다. 다시 입력해주세요.")
                continue
                
            self.process_request(user_input)
            
            print("\n" + "="*60)
            print("다른 키보드도 추천받고 싶으시면 계속 입력해주세요!")
            print("="*60)

def main():
    """메인 함수"""
    try:
        cli = KeyboardCLI()
        cli.run()
    except KeyboardInterrupt:
        print("\n\n👋 프로그램을 종료합니다.")
    except Exception as e:
        print(f"\n❌ 예상치 못한 오류가 발생했습니다: {str(e)}")
        print("프로그램을 다시 실행해주세요.")

if __name__ == "__main__":
    main() 