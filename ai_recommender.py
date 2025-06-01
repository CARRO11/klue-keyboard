import os
from typing import Dict, List
import openai
from dotenv import load_dotenv
import json

load_dotenv()
openai.api_key = os.getenv('OPENAI_API_KEY')

class AIRecommender:
    def __init__(self):
        self.conversation_history = []
        
    def generate_recommendation_explanation(self, recommendations: Dict[str, List[Dict]], preferences: Dict) -> str:
        """AI 추천 설명 생성"""
        try:
            # 추천 결과 요약 생성
            summary = self._create_recommendation_summary(recommendations)
            preference_summary = self._create_preference_summary(preferences)
            
            # OpenAI API 호출
            response = openai.ChatCompletion.create(
                model="gpt-4o-mini",
                messages=[
                    {
                        "role": "system", 
                        "content": """당신은 기계식 키보드 전문가입니다. 
                        사용자의 선호도를 기반으로 추천된 키보드 부품들에 대해 자세하고 친근한 어투로 설명해주세요.
                        각 부품이 사용자의 선호도와 어떻게 부합하는지, 그리고 이 부품들이 조합되었을 때의 시너지도 설명해주세요.
                        전문 용어는 가능한 쉽게 풀어서 설명하고, 각 부품의 장점과 특징을 구체적으로 언급해주세요."""
                    },
                    {
                        "role": "user", 
                        "content": f"""
                        [사용자 선호도]
                        {preference_summary}

                        [추천된 부품]
                        {summary}

                        위 부품들이 사용자의 선호도에 어떻게 부합하는지 설명해주세요. 
                        특히 다음 사항들을 포함해주세요:
                        1. 각 부품이 선택된 이유
                        2. 부품들의 조합으로 인한 시너지 효과
                        3. 사용자가 기대할 수 있는 타건감과 사운드
                        4. 특별히 주목할 만한 기능이나 특징
                        """
                    }
                ],
                temperature=0.7,
                max_tokens=600
            )
            
            return response.choices[0].message['content']
            
        except Exception as e:
            print(f"AI 설명 생성 중 오류가 발생했습니다: {str(e)}")
            print("기본 설명을 제공합니다.")
            return self._generate_basic_explanation(recommendations, preferences)

    def _generate_basic_explanation(self, recommendations: Dict[str, List[Dict]], preferences: Dict) -> str:
        """기본 추천 설명 생성 (AI 없이)"""
        explanation = []
        explanation.append("🎯 추천 부품 분석")
        explanation.append("=" * 50)
        
        # 선호도 분석
        if 'switch_type' in preferences:
            switch_type = preferences['switch_type']
            if switch_type == 'Linear':
                explanation.append("✨ 리니어 스위치를 선호하시는군요! 부드럽고 일관된 타건감을 원하시는 분께 적합합니다.")
            elif switch_type == 'Tactile':
                explanation.append("✨ 택타일 스위치를 선호하시는군요! 명확한 피드백과 적당한 저항감을 좋아하시는 분께 적합합니다.")
            elif switch_type == 'Clicky':
                explanation.append("✨ 클릭키 스위치를 선호하시는군요! 명확한 소리와 촉감을 원하시는 분께 적합합니다.")
        
        # 추천된 부품별 설명
        for category, items in recommendations.items():
            if not items:
                continue
                
            top_item = items[0]
            
            if category == 'switches':
                explanation.append(f"\n🔧 추천 스위치: {top_item['name']}")
                explanation.append(f"   • 타입: {top_item.get('type', 'N/A')}")
                explanation.append(f"   • 이 스위치는 균형잡힌 성능으로 많은 사용자들에게 사랑받고 있습니다.")
                
            elif category == 'plate':
                explanation.append(f"\n🔧 추천 플레이트: {top_item['name']}")
                explanation.append(f"   • 재질: {top_item.get('material', 'N/A')}")
                explanation.append(f"   • 이 플레이트는 안정성과 타건감의 균형이 뛰어납니다.")
                
            elif category == 'stabilizers':
                explanation.append(f"\n🔧 추천 스태빌라이저: {top_item['name']}")
                explanation.append(f"   • 타입: {top_item.get('type', 'N/A')}")
                explanation.append(f"   • 안정적인 대형 키 지원으로 타건 경험을 향상시킵니다.")
                
            elif category == 'keycaps':
                explanation.append(f"\n🔧 추천 키캡: {top_item['name']}")
                explanation.append(f"   • 재질: {top_item.get('material', 'N/A')}")
                explanation.append(f"   • 프로파일: {top_item.get('profile', 'N/A')}")
                explanation.append(f"   • 내구성과 타건감이 우수한 키캡입니다.")
                
            elif category == 'pcb':
                explanation.append(f"\n🔧 추천 PCB: {top_item['name']}")
                explanation.append(f"   • 고품질 PCB로 안정적인 성능을 제공합니다.")
                rgb_status = '지원' if top_item.get('rgb_support') else '미지원'
                explanation.append(f"   • RGB: {rgb_status}")
        
        explanation.append("\n💡 종합 평가:")
        explanation.append("선택된 부품들은 서로 잘 어울리며, 안정적이고 만족스러운 타건 경험을 제공할 것입니다.")
        explanation.append("각 부품의 구매 링크를 통해 쉽게 구매하실 수 있습니다.")
        
        return '\n'.join(explanation)

    def _create_preference_summary(self, preferences: Dict) -> str:
        """사용자 선호도 요약 생성"""
        summary = []
        
        # 소리 관련
        if 'sound_profile' in preferences:
            sound_level = preferences['sound_profile']
            summary.append(f"- 소리 선호도: {'조용한' if sound_level < 5 else '적당한' if sound_level < 7 else '경쾌한'} 타건음 (점수: {sound_level}/10)")
            
        # 타입 관련
        if 'switch_type' in preferences:
            summary.append(f"- 선호하는 스위치: {preferences['switch_type']} 타입")
            
        # 촉감 관련
        if 'tactile_score' in preferences:
            tactile_level = preferences['tactile_score']
            summary.append(f"- 촉각 선호도: {'낮음' if tactile_level < 4 else '보통' if tactile_level < 7 else '높음'} (점수: {tactile_level}/10)")
            
        # 속도 관련
        if 'speed_score' in preferences:
            speed_level = preferences['speed_score']
            summary.append(f"- 타건 속도감: {'부드러운' if speed_level < 4 else '보통' if speed_level < 7 else '빠른'} (점수: {speed_level}/10)")
            
        # 가격대 관련
        if 'price_tier' in preferences:
            price_levels = {1: '엔트리급', 2: '중급', 3: '고급', 4: '프리미엄급'}
            summary.append(f"- 선호 가격대: {price_levels.get(preferences['price_tier'], '미정')} 제품")

        # 품질 관련
        if 'build_quality' in preferences:
            quality_level = preferences['build_quality']
            summary.append(f"- 선호하는 품질 수준: {quality_level}/10")

        # RGB 관련
        if 'rgb_compatible' in preferences:
            summary.append(f"- RGB 선호: {'예' if preferences['rgb_compatible'] else '아니오'}")

        return '\n'.join(summary)

    def _create_recommendation_summary(self, recommendations: Dict[str, List[Dict]]) -> str:
        """추천 결과 요약 생성"""
        summary = []
        
        def safe_float(value, default=0):
            """안전하게 float로 변환"""
            try:
                return float(value) if value is not None else default
            except (ValueError, TypeError):
                return default
        
        for category, items in recommendations.items():
            if not items:
                continue
                
            top_item = items[0]  # 첫 번째 추천 아이템
            
            if category == 'switches':
                summary.append(f"[스위치] {top_item['name']}")
                summary.append(f"- 타입: {top_item.get('type', 'N/A')}")
                summary.append(f"- 특징: 소리({safe_float(top_item.get('sound_score')):.1f}/10), "
                             f"부드러움({safe_float(top_item.get('smoothness_score')):.1f}/10), "
                             f"속도({safe_float(top_item.get('speed_score')):.1f}/10)")
                summary.append(f"- 구매링크: {top_item.get('link', 'N/A')}")
                
            elif category == 'plate':
                summary.append(f"[플레이트] {top_item['name']}")
                summary.append(f"- 재질: {top_item.get('material', 'N/A')}")
                summary.append(f"- 특징: 유연성({safe_float(top_item.get('flex_level'))}/10), "
                             f"두께({safe_float(top_item.get('thickness')):.1f}mm)")
                summary.append(f"- 구매링크: {top_item.get('link', 'N/A')}")
                
            elif category == 'stabilizers':
                summary.append(f"[스태빌라이저] {top_item['name']}")
                summary.append(f"- 타입: {top_item.get('type', 'N/A')}")
                summary.append(f"- 특징: 흔들림({safe_float(top_item.get('rattle')):.1f}/10), "
                             f"부드러움({safe_float(top_item.get('smoothness')):.1f}/10)")
                summary.append(f"- 구매링크: {top_item.get('link', 'N/A')}")
                
            elif category == 'keycaps':
                summary.append(f"[키캡] {top_item['name']}")
                summary.append(f"- 재질: {top_item.get('material', 'N/A')}")
                summary.append(f"- 프로파일: {top_item.get('profile', 'N/A')}")
                summary.append(f"- 특징: 두께({safe_float(top_item.get('thickness')):.1f}mm), "
                             f"내구성({safe_float(top_item.get('durability')):.1f}/10)")
                summary.append(f"- 구매링크: {top_item.get('link', 'N/A')}")
                
            elif category == 'pcb':
                summary.append(f"[PCB] {top_item['name']}")
                summary.append(f"- 품질: {safe_float(top_item.get('build_quality')):.1f}/10")
                rgb_status = '지원' if top_item.get('rgb_support') else '미지원'
                qmk_status = '지원' if top_item.get('qmk_via') else '미지원'
                summary.append(f"- 특징: RGB({rgb_status}), QMK/VIA({qmk_status})")
                summary.append(f"- 구매링크: {top_item.get('link', 'N/A')}")
            
            summary.append("")  # 카테고리 간 구분을 위한 빈 줄
                
        return '\n'.join(summary) 

    def parse_natural_language_to_preferences(self, user_message: str) -> Dict:
        """자연어 입력을 선호도 설정으로 변환"""
        try:
            response = openai.ChatCompletion.create(
                model="gpt-4o-mini",
                messages=[
                    {
                        "role": "system",
                        "content": """당신은 키보드 전문가입니다. 사용자의 자연어 요청을 분석하여 키보드 선호도 설정으로 변환해주세요.

다음 JSON 형식으로만 응답하세요:
{
    "switch_type": "Linear|Tactile|Clicky",
    "sound_profile": 1-10 (1=매우조용, 10=매우시끄러운),
    "tactile_score": 1-10 (1=부드러운, 10=강한촉감),
    "speed_score": 1-10 (1=여유로운, 10=빠른),
    "price_tier": 1-4 (1=엔트리급, 2=중급, 3=고급, 4=프리미엄),
    "build_quality": 1-10,
    "rgb_compatible": true|false
}

키워드 해석 가이드:
- 조용한/무음: sound_profile=1-3
- 게이밍: speed_score=8-10, rgb_compatible=true
- 사무용: sound_profile=1-4, speed_score=5-7
- 타이핑: tactile_score=6-8, switch_type="Tactile"
- 고급/프리미엄: price_tier=3-4, build_quality=8-10
- 저렴한/엔트리: price_tier=1-2
- 빠른: speed_score=8-10
- 부드러운: tactile_score=1-4, switch_type="Linear"
"""
                    },
                    {
                        "role": "user",
                        "content": f"사용자 요청: '{user_message}'"
                    }
                ],
                temperature=0.3,
                max_tokens=200
            )
            
            # JSON 응답 파싱
            import json
            preferences_text = response.choices[0].message.content.strip()
            
            # JSON 부분만 추출 (```json 태그가 있을 경우 제거)
            if "```json" in preferences_text:
                preferences_text = preferences_text.split("```json")[1].split("```")[0]
            elif "```" in preferences_text:
                preferences_text = preferences_text.split("```")[1]
            
            preferences = json.loads(preferences_text)
            
            # 유효성 검사
            required_fields = ['switch_type', 'sound_profile', 'tactile_score', 'speed_score', 'price_tier']
            for field in required_fields:
                if field not in preferences:
                    return None
            
            return preferences
            
        except Exception as e:
            print(f"자연어 해석 오류: {e}")
            return None

    def generate_natural_language_explanation(self, user_request: str, recommendations: Dict[str, List[Dict]], preferences: Dict) -> str:
        """자연어 요청에 대한 맞춤형 설명 생성"""
        try:
            # 추천 결과 요약 생성
            summary = self._create_recommendation_summary(recommendations)
            preference_summary = self._create_preference_summary(preferences)
            
            response = openai.ChatCompletion.create(
                model="gpt-4o-mini",
                messages=[
                    {
                        "role": "system",
                        "content": """당신은 친근하고 재미있는 키보드 덕후입니다! 
                        사용자의 자연어 요청에 대해 추천된 키보드 부품들을 마치 친구에게 말하듯이 설명해주세요.
                        
                        말투 가이드:
                        - "~해요", "~네요", "~거든요" 같은 친근한 말투 사용
                        - "와!", "정말!", "완전" 같은 감탄사 자주 사용
                        - 전문용어는 쉽게 풀어서 설명
                        - 개인적인 경험담이나 팁도 섞어서 설명
                        - 이모지 적극 활용 (😊, 👍, 🎯, 💡 등)
                        
                        설명 구조:
                        1. 친근한 인사와 요청 이해 확인
                        2. 추천 부품들을 하나씩 재미있게 소개
                        3. 왜 이 조합이 좋은지 쉽게 설명
                        4. 실제 사용했을 때 어떨지 생생하게 묘사
                        5. 마무리 격려와 추가 도움 제안
                        
                        반드시 한국어로 답변하고, 구어체로 친근하게 작성해주세요!"""
                    },
                    {
                        "role": "user",
                        "content": f"""
                        사용자가 이렇게 요청했어요: "{user_request}"
                        
                        AI가 해석한 선호도:
                        {preference_summary}
                        
                        추천된 부품들:
                        {summary}
                        
                        이 요청에 대해 친구에게 말하듯이 재미있고 친근하게 설명해주세요!
                        각 부품이 왜 좋은지, 조합했을 때 어떨지 생생하게 설명해주세요.
                        """
                    }
                ],
                temperature=0.8,
                max_tokens=1200
            )
            
            explanation = response.choices[0].message.content.strip()
            
            # 한글 인코딩 문제 해결
            if isinstance(explanation, bytes):
                explanation = explanation.decode('utf-8')
            
            return explanation
            
        except Exception as e:
            print(f"자연어 설명 생성 오류: {e}")
            return self._generate_friendly_basic_explanation(user_request, recommendations, preferences)

    def _generate_friendly_basic_explanation(self, user_request: str, recommendations: Dict[str, List[Dict]], preferences: Dict) -> str:
        """친근한 기본 설명 생성 (AI 없이)"""
        explanation = []
        explanation.append(f"안녕하세요! '{user_request}' 요청 잘 받았어요! 😊")
        explanation.append("")
        explanation.append("제가 딱 맞는 부품들을 골라봤는데요, 하나씩 소개해드릴게요!")
        explanation.append("")
        
        # 추천된 부품별 친근한 설명
        for category, items in recommendations.items():
            if not items:
                continue
                
            top_item = items[0]
            
            if category == 'switches':
                explanation.append(f"🔧 **스위치: {top_item['name']}**")
                explanation.append(f"   와! 이 스위치 정말 좋아요! {top_item.get('type', 'N/A')} 타입이라서")
                explanation.append(f"   조용하면서도 부드러운 타건감을 느낄 수 있거든요 👍")
                explanation.append(f"   특히 소음에 민감하신 분들께 완전 추천해요!")
                
            elif category == 'plate':
                explanation.append(f"\n🔧 **플레이트: {top_item['name']}**")
                explanation.append(f"   {top_item.get('material', 'N/A')} 재질로 만들어진 이 플레이트는")
                explanation.append(f"   타건감이 정말 부드러워요! 손목도 편하고 💡")
                explanation.append(f"   스위치와 찰떡궁합이에요!")
                
            elif category == 'stabilizers':
                explanation.append(f"\n🔧 **스태빌라이저: {top_item['name']}**")
                explanation.append(f"   {top_item.get('type', 'N/A')} 타입이라서 스페이스바나 엔터키 누를 때")
                explanation.append(f"   흔들림 없이 안정적이에요! 정말 중요한 부품이거든요 🎯")
                
            elif category == 'keycaps':
                explanation.append(f"\n🔧 **키캡: {top_item['name']}**")
                explanation.append(f"   {top_item.get('material', 'N/A')} 재질에 {top_item.get('profile', 'N/A')} 프로파일이라서")
                explanation.append(f"   손가락 끝에 딱 맞아떨어져요! 타이핑할 때 기분 좋아질 거예요 😊")
                
            elif category == 'pcb':
                explanation.append(f"\n🔧 **PCB: {top_item['name']}**")
                explanation.append(f"   이 PCB는 정말 똑똑해요! 핫스왑 지원되니까")
                explanation.append(f"   나중에 스위치 바꾸고 싶으면 쉽게 교체할 수 있어요 🔄")
                rgb_status = '지원돼요!' if top_item.get('rgb_support') else '지원 안 돼요'
                explanation.append(f"   RGB는 {rgb_status}")
        
        explanation.append("\n💡 **종합 평가:**")
        explanation.append("이 조합으로 키보드 만들면 정말 만족하실 거예요!")
        explanation.append("조용하면서도 타건감 좋고, 내구성도 뛰어나거든요 👍")
        explanation.append("혹시 궁금한 거 있으면 언제든 물어보세요!")
        explanation.append("")
        explanation.append("행복한 키보드 라이프 되세요! 🎉")
        
        return '\n'.join(explanation) 