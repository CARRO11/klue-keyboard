# 🎯 KLUE 키보드 부품 추천 시스템

## 📋 프로젝트 개요

KLUE 키보드 부품 추천 시스템은 AI 기반 자연어 처리를 통해 사용자의 요구사항을 분석하고, 최적의 키보드 부품 조합을 추천하는 종합 시스템입니다.

### ✨ 주요 기능

- 🤖 **AI 자연어 처리**: "조용한 커스텀키보드 부품 추천해줘" 같은 자연어 입력 지원
- 🎯 **개인 맞춤형 추천**: 사용자 선호도 기반 정확한 부품 매칭
- 💬 **구어체 친근한 설명**: AI가 친구처럼 재미있게 설명
- 🔗 **구매 링크 제공**: 추천 부품의 직접 구매 링크 포함
- 📊 **상세한 부품 정보**: 78개 부품의 체계적인 데이터베이스
- 🌐 **다양한 인터페이스**: CLI, 웹 UI, REST API 지원

## 🏗️ 시스템 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Client  │    │   Flask API     │    │   MySQL DB      │
│                 │◄──►│                 │◄──►│                 │
│ - 웹 UI         │    │ - 자연어 처리   │    │ - 부품 데이터   │
│ - 자연어 입력   │    │ - AI 추천       │    │ - 선호도 매칭   │
│ - 결과 표시     │    │ - 구어체 설명   │    │ - 구매 링크     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │
         │              ┌─────────────────┐
         │              │   CLI Tool      │
         │              │                 │
         └──────────────│ - 터미널 인터페이스│
                        │ - 직접 실행     │
                        │ - 테스트 도구   │
                        └─────────────────┘
```

## 📦 데이터베이스 구성

### 부품 카테고리별 데이터 수:

- **스위치 (Switches)**: 15개 (Linear, Tactile, Clicky, Magnetic)
- **플레이트 (Plate)**: 20개 (Aluminum, FR4, POM, PC, PETG 등)
- **스태빌라이저 (Stabilizer)**: 18개 (Screw-in, Clip-in, Plate-mount)
- **키캡 (Keycap)**: 15개 (Cherry, OEM, SA, XDA, KAT, MT3 프로파일)
- **PCB**: 10개 (60%, 65%, 75% 레이아웃, 핫스왑 지원)

**총 78개 부품**의 상세 정보와 구매 링크 포함

## 🚀 설치 및 실행

## ⚡ 빠른 시작

### 1. 프로젝트 클론

```bash
git clone https://github.com/CARRO11/klue-keyboard.git
cd klue-keyboard
```

### 2. OpenAI API 키 설정 (필수!)

**⚠️ 중요: AI 추천 기능(Tony)을 사용하려면 OpenAI API 키가 필요합니다.**

#### 방법 1: 템플릿 파일 사용 (권장)

```bash
# 템플릿 파일을 복사
cp klueai.env.template klueai.env

# 텍스트 에디터로 파일 열기
nano klueai.env
# 또는
code klueai.env
```

#### 방법 2: 직접 생성

```bash
# 새 파일 생성
echo "OPENAI_API_KEY=sk-proj-여기에-실제-키-입력" > klueai.env
echo "DB_HOST=localhost" >> klueai.env
echo "DB_USER=root" >> klueai.env
echo "DB_PASSWORD=shin" >> klueai.env
echo "DB_NAME=klue_keyboard" >> klueai.env
```

#### API 키 발급 방법:

1. [OpenAI 플랫폼](https://platform.openai.com/api-keys)에서 계정 생성
2. API 키 생성 (유료 계정 필요)
3. `klueai.env` 파일에서 다음 라인 수정:

   ```
   # 수정 전
   OPENAI_API_KEY=sk-proj-여기에-실제-키-입력

   # 수정 후 (실제 키로 교체)
   OPENAI_API_KEY=sk-proj-실제발급받은키여기에입력
   ```

### 3. 데이터베이스 설정

```bash
mysql -u root -p
CREATE DATABASE klue_keyboard;
# 제공된 SQL 파일들로 테이블 생성
```

### 4. 백엔드 실행

```bash
# Spring Boot 서버
cd klue_sever
./gradlew bootRun

# Python AI 서버 (포트 5002) - 새 터미널에서
python keyboard_recommender.py
```

### 5. 프론트엔드 실행

```bash
cd klue_client
npm install
npm start
```

## 🚨 문제 해결

### "AI 추천 기능이 작동하지 않아요!"

- `klueai.env` 파일이 있는지 확인
- OpenAI API 키가 올바르게 설정되었는지 확인
- API 키에 잔액이 있는지 확인 (유료 서비스)

### "Tony가 응답하지 않아요!"

- Python 서버가 실행 중인지 확인 (`python keyboard_recommender.py`)
- 포트 5002가 사용 중인지 확인

## 🎯 기능별 사용 가이드

### 🍷 AI 추천 (Tony)

- **URL**: `http://localhost:3000/ai`
- **요구사항**: OpenAI API 키 필수
- **사용법**: "조용한 사무용 키보드" 같은 자연어로 요청

### 🔧 Build 페이지

- **URL**: `http://localhost:3000/build`
- **요구사항**: 데이터베이스만 필요
- **사용법**: 드롭다운에서 부품 선택

### 📊 일반 기능 (List, Site 등)

- **요구사항**: API 키 불필요
- **사용법**: 바로 사용 가능

## 💻 사용 방법

### 1. CLI 도구 (터미널)

```bash
# 대화형 CLI 실행
python3 keyboard_cli.py

# 입력 예시:
💬 원하는 키보드를 설명해주세요: 조용한 커스텀키보드 부품 추천해줘
```

### 2. 웹 인터페이스

브라우저에서 `http://localhost:3000` 접속

- 자연어 입력 또는 상세 설정 선택
- 실시간 추천 결과 확인
- AI 설명 및 구매 링크 제공

### 3. REST API

#### 간편 추천 API (권장)

```bash
curl -X POST http://localhost:5002/api/simple-recommend \
  -H "Content-Type: application/json" \
  -d '{"message": "조용한 커스텀키보드 부품 추천해줘"}'
```

#### 빠른 추천 API (GET)

```bash
curl "http://localhost:5002/api/quick-recommend/게이밍용%20고급%20키보드"
```

## 🔧 API 엔드포인트

### 추천 API

- `POST /api/simple-recommend` - 간편 텍스트 추천 (권장)
- `GET /api/quick-recommend/<message>` - URL 파라미터 추천
- `POST /api/recommend/natural` - 자연어 추천
- `POST /api/recommend` - 상세 선호도 추천

### 정보 조회 API

- `GET /api/health` - 서버 상태 확인
- `GET /api/components` - 부품 통계 조회
- `GET /api/components/<category>` - 카테고리별 부품 목록
- `GET /api/preferences/templates` - 선호도 템플릿

## 🧪 테스트

### API 테스트

```bash
# 백엔드 API 테스트 도구
python3 test_backend_api.py

# 추천 시스템 테스트
python3 test_recommender.py
```

### 테스트 케이스 예시

- "조용한 커스텀키보드 부품 추천해줘"
- "게이밍용 고급 키보드 만들고 싶어"
- "사무실에서 쓸 조용한 키보드"
- "프리미엄 타이핑용 키보드"

## 📁 프로젝트 구조

```
klue_project/
├── 🎯 핵심 시스템
│   ├── app.py                     # Flask API 서버
│   ├── keyboard_recommender.py    # 추천 엔진
│   ├── ai_recommender.py          # AI 자연어 처리
│   └── keyboard_cli.py            # CLI 도구
│
├── 🌐 웹 클라이언트
│   └── klue_client/               # React 웹 애플리케이션
│       ├── src/pages/KeyboardRecommendation.tsx
│       └── src/pages/KeyboardRecommendation.css
│
├── 🗄️ 데이터베이스
│   ├── migrate_to_mysql.sql       # 데이터베이스 스키마
│   ├── update_missing_data.sql    # 부품 데이터
│   ├── update_exact_links.sql     # 구매 링크
│   └── insert_*_data.py          # 데이터 삽입 스크립트
│
├── 🔧 테스트 도구
│   ├── test_backend_api.py        # API 테스트
│   ├── test_recommender.py        # 추천 시스템 테스트
│   └── crawl_*.py                # 데이터 수집 도구
│
├── 📚 문서
│   ├── README.md                  # 프로젝트 개요 (이 파일)
│   ├── BACKEND_API_GUIDE.md       # API 사용 가이드
│   └── DEPLOYMENT_GUIDE.md        # 배포 가이드
│
└── ⚙️ 설정
    ├── requirements.txt           # Python 패키지
    ├── klueai.env                # 환경 변수 템플릿
    └── .env                      # 실제 환경 변수 (생성 필요)
```

## 🤖 AI 기능

### 자연어 처리

- **OpenAI GPT-4o-mini** 모델 사용
- 한국어 자연어 → 구조화된 선호도 변환
- 키워드 해석 (조용한→sound_profile=1-3, 게이밍→speed_score=8-10)

### 구어체 설명 생성

- 친근한 말투 ("~해요", "~거든요", "완전", "정말")
- 이모지 활용 (😊, 👍, 🎯, 💡)
- 개인적 경험담 포함
- 쉬운 전문용어 설명

## 📊 추천 알고리즘

### 1. 선호도 분석

- 스위치 타입 (Linear/Tactile/Clicky)
- 소음 수준 (1-10)
- 촉감 강도 (1-10)
- 속도감 (1-10)
- 가격대 (1-4)
- RGB 선호도

### 2. 부품 매칭

- 가중치 기반 점수 계산
- 카테고리별 최적 조합 선택
- 호환성 검증
- 가격대 균형 조정

### 3. 결과 최적화

- 상위 3-5개 부품 추천
- 시너지 효과 고려
- 구매 가능성 확인
- 사용자 피드백 반영

## 🌟 특별 기능

### 템플릿 시스템

- **게이밍**: 빠른 반응속도, RGB 지원
- **사무용**: 조용한 타건음, 편안한 사용감
- **타이핑**: 장시간 사용 최적화
- **프리미엄**: 최고급 부품 조합

### 구매 연동

- 실제 구매 가능한 링크 제공
- 가격 정보 포함
- 재고 상태 고려
- 다양한 쇼핑몰 연결

## 🚀 배포 및 운영

### 개발 환경

```bash
# 로컬 개발 서버
python3 app.py  # API 서버
npm start       # React 클라이언트
```

### 프로덕션 배포

```bash
# Gunicorn으로 API 서버 실행
pip install gunicorn
gunicorn -w 4 -b 0.0.0.0:5002 app:app

# React 빌드 및 배포
npm run build
# 빌드된 파일을 웹 서버에 배포
```

## 📈 성능 및 확장성

### 현재 성능

- **응답 시간**: 10-30초 (AI 처리 포함)
- **동시 사용자**: 10-50명 (개발 환경)
- **데이터베이스**: 78개 부품, 확장 가능
- **API 처리량**: 초당 2-5 요청

### 확장 계획

- 부품 데이터 지속 확장
- 사용자 피드백 학습
- 추천 정확도 개선
- 다국어 지원

## 🔒 보안 및 개인정보

- OpenAI API 키 환경 변수 관리
- 사용자 입력 검증 및 필터링
- SQL 인젝션 방지
- CORS 설정으로 안전한 API 접근

## 🤝 기여 방법

1. **부품 데이터 추가**: 새로운 키보드 부품 정보 기여
2. **번역**: 다국어 지원을 위한 번역 작업
3. **UI/UX 개선**: 사용자 인터페이스 개선 제안
4. **알고리즘 최적화**: 추천 정확도 향상 방안

## 📞 지원 및 문의

### 문제 해결

1. **서버 연결 실패**: `curl http://localhost:5002/api/health`로 상태 확인
2. **AI 응답 없음**: OpenAI API 키 설정 확인
3. **데이터베이스 오류**: MySQL 연결 및 권한 확인

### 추가 문서

- [백엔드 API 가이드](BACKEND_API_GUIDE.md)
- [배포 가이드](DEPLOYMENT_GUIDE.md)

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

---

## 🎉 완성된 기능 요약

✅ **자연어 처리**: "조용한 키보드" → 구조화된 선호도  
✅ **AI 추천 엔진**: 78개 부품에서 최적 조합 선택  
✅ **구어체 설명**: 친근하고 재미있는 한글 설명  
✅ **웹 인터페이스**: React 기반 모던 UI  
✅ **REST API**: 다양한 플랫폼 연동 가능  
✅ **CLI 도구**: 터미널에서 직접 사용  
✅ **구매 연동**: 실제 구매 링크 제공  
✅ **테스트 도구**: 완전한 테스트 환경

**🚀 이제 어디서든 "조용한 커스텀키보드 부품 추천해줘"라고 말하면 AI가 친근하게 답해드립니다!**
