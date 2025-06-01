# 🚀 Klue 키보드 추천 시스템 배포 가이드

## 📋 개요

이 가이드는 완성된 키보드 추천 시스템을 klue 사이트에 통합하는 방법을 설명합니다.

## 🏗️ 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Client  │───▶│   Flask API     │───▶│   MySQL DB      │
│   (Frontend)    │    │   (Python)      │    │   (Database)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
│                      │                      │
│ - 사용자 인터페이스    │ - 추천 로직          │ - 부품 데이터
│ - 선호도 입력         │ - AI 설명 생성       │ - 구매 링크
│ - 결과 표시          │ - API 엔드포인트     │ - 78개 부품
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔧 1단계: 환경 설정

### Python 환경 설정

```bash
# 의존성 설치
pip install -r requirements.txt

# 환경 변수 설정 (.env 파일 확인)
OPENAI_API_KEY=your_openai_api_key
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=shin
DB_NAME=klue_keyboard
```

### MySQL 데이터베이스 확인

```bash
# 데이터베이스 연결 테스트
mysql -u root -p
USE klue_keyboard;
SHOW TABLES;

# 부품 데이터 확인
SELECT COUNT(*) FROM Switches;   -- 15개
SELECT COUNT(*) FROM Plate;      -- 20개
SELECT COUNT(*) FROM Stabilizer; -- 18개
SELECT COUNT(*) FROM Keycap;     -- 15개
SELECT COUNT(*) FROM PCB;        -- 10개
```

## 🚀 2단계: Flask API 서버 실행

### 개발 환경에서 실행

```bash
# Flask 서버 시작
python app.py

# 서버 확인
curl http://localhost:5000/api/health
```

### API 엔드포인트

- `GET /` - API 정보
- `GET /api/health` - 서버 상태 확인
- `POST /api/recommend` - 키보드 부품 추천
- `GET /api/components` - 부품 통계
- `GET /api/components/<category>` - 카테고리별 부품 목록
- `GET /api/preferences/templates` - 선호도 템플릿

## 🎨 3단계: React 클라이언트 통합

### 기존 klue_client에 추가

```bash
cd klue_client

# 키보드 추천 페이지 추가 (이미 생성됨)
# - src/pages/KeyboardRecommendation.tsx
# - src/pages/KeyboardRecommendation.css
```

### App.tsx에 라우팅 추가

```typescript
import KeyboardRecommendation from './pages/KeyboardRecommendation';

// 라우팅에 추가
<Route path="/keyboard-recommendation" component={KeyboardRecommendation} />
```

### 네비게이션에 메뉴 추가

```typescript
// 네비게이션 컴포넌트에 추가
<Link to="/keyboard-recommendation">키보드 추천</Link>
```

## 🔄 4단계: 통합 테스트

### Flask API 테스트

```bash
# 추천 API 테스트
curl -X POST http://localhost:5000/api/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "switch_type": "Linear",
    "sound_profile": 5,
    "tactile_score": 3,
    "speed_score": 8,
    "price_tier": 2,
    "build_quality": 8,
    "rgb_compatible": true
  }'
```

### React 클라이언트 테스트

```bash
cd klue_client
npm start

# 브라우저에서 확인
# http://localhost:3000/keyboard-recommendation
```

## 🌐 5단계: 프로덕션 배포

### Flask API 배포 (옵션 1: 같은 서버)

```bash
# Gunicorn으로 프로덕션 실행
pip install gunicorn
gunicorn -w 4 -b 0.0.0.0:5000 app:app
```

### Flask API 배포 (옵션 2: 별도 서버)

```bash
# Docker 컨테이너로 배포
docker build -t klue-keyboard-api .
docker run -p 5000:5000 klue-keyboard-api
```

### React 빌드 및 배포

```bash
cd klue_client

# 프로덕션 빌드
npm run build

# 빌드된 파일을 웹서버에 배포
# build/ 폴더의 내용을 웹서버 루트에 복사
```

## ⚙️ 6단계: 환경별 설정

### 개발 환경

```typescript
// React에서 API URL 설정
const API_BASE_URL = "http://localhost:5000";
```

### 프로덕션 환경

```typescript
// React에서 API URL 설정
const API_BASE_URL = "https://your-domain.com/api";
```

## 🔒 7단계: 보안 설정

### CORS 설정

```python
# app.py에서 CORS 설정 확인
CORS(app, origins=['https://your-domain.com'])
```

### API 키 보안

```bash
# 환경 변수로 API 키 관리
export OPENAI_API_KEY=your_secret_key
```

## 📊 8단계: 모니터링

### 로그 설정

```python
import logging
logging.basicConfig(level=logging.INFO)
```

### 성능 모니터링

- API 응답 시간 측정
- 데이터베이스 쿼리 최적화
- OpenAI API 사용량 모니터링

## 🐛 9단계: 문제 해결

### 일반적인 문제들

#### 1. CORS 오류

```javascript
// 브라우저 콘솔에서 CORS 오류 발생 시
// Flask app.py에서 CORS 설정 확인
```

#### 2. 데이터베이스 연결 오류

```bash
# MySQL 서비스 상태 확인
sudo systemctl status mysql

# 연결 정보 확인
mysql -u root -p -h localhost
```

#### 3. OpenAI API 오류

```bash
# API 키 확인
echo $OPENAI_API_KEY

# 할당량 확인
# https://platform.openai.com/account/usage
```

## 📈 10단계: 성능 최적화

### 캐싱 구현

```python
from flask_caching import Cache
cache = Cache(app)

@cache.memoize(timeout=300)
def get_recommendations(preferences):
    # 캐싱된 추천 결과
```

### 데이터베이스 최적화

```sql
-- 인덱스 추가
CREATE INDEX idx_switches_type ON Switches(type);
CREATE INDEX idx_price_tier ON Switches(price_tier);
```

## 🎯 완료 체크리스트

- [ ] MySQL 데이터베이스 설정 완료
- [ ] Flask API 서버 실행 확인
- [ ] React 컴포넌트 통합 완료
- [ ] API 통신 테스트 성공
- [ ] 추천 기능 정상 작동
- [ ] AI 설명 생성 확인
- [ ] 구매 링크 연결 확인
- [ ] 반응형 디자인 확인
- [ ] 프로덕션 배포 완료

## 🆘 지원

문제가 발생하면 다음을 확인하세요:

1. 서버 로그 확인
2. 브라우저 개발자 도구 확인
3. 데이터베이스 연결 상태 확인
4. API 키 유효성 확인

---

🎉 **축하합니다!** klue 사이트에 키보드 추천 시스템이 성공적으로 통합되었습니다!
