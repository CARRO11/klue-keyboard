# 🎯 KLUE 키보드 추천 시스템 - 백엔드 API 가이드

## 📋 개요

KLUE 키보드 추천 시스템의 백엔드 API를 사용하여 자연어로 키보드 부품 추천을 받을 수 있습니다.

## 🚀 서버 실행

```bash
python3 app.py
```

서버는 `http://localhost:5002`에서 실행됩니다.

## 📡 API 엔드포인트

### 1. 간편 추천 API (권장)

**POST** `/api/simple-recommend`

가장 간단하고 완전한 추천 API입니다.

#### 요청 예시:

```bash
curl -X POST http://localhost:5002/api/simple-recommend \
  -H "Content-Type: application/json" \
  -d '{"message": "조용한 커스텀키보드 부품 추천해줘"}'
```

#### 응답 예시:

```json
{
  "success": true,
  "user_request": "조용한 커스텀키보드 부품 추천해줘",
  "preference_summary": {
    "switch_type": "Linear",
    "sound_level": 2,
    "sound_description": "조용함",
    "price_tier": 2,
    "price_description": "중급",
    "rgb_compatible": false,
    "rgb_description": "선호하지 않음"
  },
  "recommendations": {
    "switches": {
      "name": "스위치",
      "items": [
        {
          "name": "Gateron Yellow",
          "type": "Linear",
          "sound_score": 7.0,
          "smoothness_score": 8.5,
          "price_tier_text": "고급",
          "link": "https://swagkeys.com/products/gateron-yellow-switches"
        }
      ]
    }
  },
  "ai_explanation": "안녕! 😊 조용한 커스텀 키보드를 만들고 싶다고 했지?...",
  "total_components": 15
}
```

### 2. 빠른 추천 API

**GET** `/api/quick-recommend/<message>`

URL에 직접 요청을 넣어서 빠르게 추천받을 수 있습니다.

#### 요청 예시:

```bash
curl "http://localhost:5002/api/quick-recommend/게이밍용%20고급%20키보드"
```

### 3. 기존 API들

#### 자연어 추천

**POST** `/api/recommend/natural`

#### 상세 선호도 추천

**POST** `/api/recommend`

#### 서버 상태 확인

**GET** `/api/health`

#### 부품 정보 조회

**GET** `/api/components`
**GET** `/api/components/<category>`

## 💻 Python에서 사용하기

### 간단한 예시:

```python
import requests

def get_keyboard_recommendation(message):
    url = "http://localhost:5002/api/simple-recommend"
    data = {"message": message}

    response = requests.post(url, json=data)

    if response.status_code == 200:
        result = response.json()
        if result['success']:
            return result
    return None

# 사용 예시
result = get_keyboard_recommendation("조용한 커스텀키보드 부품 추천해줘")
if result:
    print(f"요청: {result['user_request']}")
    print(f"총 {result['total_components']}개 부품 추천")
    print(f"AI 설명: {result['ai_explanation'][:100]}...")
```

### 고급 예시:

```python
import requests
import json

class KeyboardRecommendationAPI:
    def __init__(self, base_url="http://localhost:5002"):
        self.base_url = base_url

    def simple_recommend(self, message):
        """간편 추천"""
        url = f"{self.base_url}/api/simple-recommend"
        response = requests.post(url, json={"message": message})
        return response.json() if response.status_code == 200 else None

    def quick_recommend(self, message):
        """빠른 추천 (GET)"""
        import urllib.parse
        encoded = urllib.parse.quote(message)
        url = f"{self.base_url}/api/quick-recommend/{encoded}"
        response = requests.get(url)
        return response.json() if response.status_code == 200 else None

    def get_server_info(self):
        """서버 정보 조회"""
        response = requests.get(f"{self.base_url}/")
        return response.json() if response.status_code == 200 else None

# 사용 예시
api = KeyboardRecommendationAPI()

# 서버 정보 확인
info = api.get_server_info()
print(f"서버: {info['message']} v{info['version']}")

# 추천 받기
result = api.simple_recommend("게이밍용 고급 키보드 만들고 싶어")
if result and result['success']:
    print(f"\n추천 결과:")
    for category, data in result['recommendations'].items():
        print(f"- {data['name']}: {len(data['items'])}개")
```

## 🌐 JavaScript/Node.js에서 사용하기

### Fetch API 사용:

```javascript
async function getKeyboardRecommendation(message) {
  try {
    const response = await fetch("http://localhost:5002/api/simple-recommend", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ message: message }),
    });

    const result = await response.json();

    if (result.success) {
      return result;
    } else {
      console.error("추천 실패:", result.message);
      return null;
    }
  } catch (error) {
    console.error("API 호출 오류:", error);
    return null;
  }
}

// 사용 예시
getKeyboardRecommendation("조용한 커스텀키보드 부품 추천해줘").then(
  (result) => {
    if (result) {
      console.log("요청:", result.user_request);
      console.log("총 부품 수:", result.total_components);
      console.log("AI 설명:", result.ai_explanation.substring(0, 100) + "...");
    }
  }
);
```

### Axios 사용:

```javascript
const axios = require("axios");

class KeyboardAPI {
  constructor(baseURL = "http://localhost:5002") {
    this.api = axios.create({ baseURL });
  }

  async simpleRecommend(message) {
    try {
      const response = await this.api.post("/api/simple-recommend", {
        message,
      });
      return response.data;
    } catch (error) {
      console.error("API 오류:", error.message);
      return null;
    }
  }

  async quickRecommend(message) {
    try {
      const encodedMessage = encodeURIComponent(message);
      const response = await this.api.get(
        `/api/quick-recommend/${encodedMessage}`
      );
      return response.data;
    } catch (error) {
      console.error("API 오류:", error.message);
      return null;
    }
  }
}

// 사용 예시
const keyboardAPI = new KeyboardAPI();

keyboardAPI.simpleRecommend("게이밍용 고급 키보드").then((result) => {
  if (result && result.success) {
    console.log("추천 성공!");
    console.log("선호도:", result.preference_summary);
  }
});
```

## 🔧 테스트 도구

프로젝트에 포함된 테스트 스크립트를 사용할 수 있습니다:

```bash
python3 test_backend_api.py
```

## 📝 요청 메시지 예시

다음과 같은 자연어 요청이 가능합니다:

- `"조용한 커스텀키보드 부품 추천해줘"`
- `"게이밍용 고급 키보드 만들고 싶어"`
- `"사무실에서 쓸 조용한 키보드"`
- `"프리미엄 타이핑용 키보드"`
- `"저렴한 엔트리급 키보드"`
- `"RGB 조명이 있는 화려한 키보드"`
- `"택타일 스위치로 타건감 좋은 키보드"`

## ⚠️ 주의사항

1. **서버 실행**: API 사용 전에 `python3 app.py`로 서버를 실행해야 합니다.
2. **OpenAI API 키**: `.env` 파일에 `OPENAI_API_KEY`가 설정되어 있어야 합니다.
3. **타임아웃**: AI 처리로 인해 응답에 10-30초 정도 소요될 수 있습니다.
4. **한글 인코딩**: URL에 한글을 사용할 때는 적절히 인코딩해야 합니다.

## 🚀 프로덕션 배포

프로덕션 환경에서는 다음을 고려하세요:

1. **WSGI 서버 사용**: Gunicorn, uWSGI 등
2. **리버스 프록시**: Nginx 등
3. **환경 변수**: 민감한 정보는 환경 변수로 관리
4. **로깅**: 적절한 로깅 설정
5. **에러 핸들링**: 더 상세한 에러 처리

```bash
# Gunicorn 예시
pip install gunicorn
gunicorn -w 4 -b 0.0.0.0:5002 app:app
```

## 📞 지원

문제가 발생하면 다음을 확인하세요:

1. 서버가 실행 중인지 확인: `curl http://localhost:5002/api/health`
2. OpenAI API 키가 설정되어 있는지 확인
3. 필요한 패키지가 설치되어 있는지 확인: `pip install -r requirements.txt`

---

🎉 **이제 백엔드 API를 통해 어디서든 키보드 추천을 받을 수 있습니다!**
