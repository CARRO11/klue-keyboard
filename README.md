# 🎹 KLUE - 커스텀 키보드 추천 시스템

> **스위치, 키캡, 하우징, 조립까지! 초보자도 쉽게 접근할 수 있는 커스텀 키보드 플랫폼**

![KLUE System](https://img.shields.io/badge/KLUE-키보드추천시스템-blue)
![Python](https://img.shields.io/badge/Python-3.8+-green)
![React](https://img.shields.io/badge/React-18+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange)

## 📋 목차

- [시스템 개요](#-시스템-개요)
- [빠른 시작](#-빠른-시작)
- [기능 소개](#-기능-소개)
- [완전 설치](#-완전-설치)
- [문제 해결](#-문제-해결)
- [개발자 가이드](#-개발자-가이드)

## 🎯 시스템 개요

**KLUE**는 커스텀 키보드 입문자와 전문가 모두를 위한 종합 추천 플랫폼입니다.

### 주요 구성

- **🤖 AI 추천 엔진**: 사용자 취향 분석 후 맞춤형 키보드 구성 추천
- **🔧 부품 데이터베이스**: 스위치, 키캡, PCB, 플레이트 등 15,000+ 부품 정보
- **🛠️ 빌드 시뮬레이터**: 가상으로 키보드를 조립해보는 기능
- **🌐 쇼핑 가이드**: 국내외 주요 커스텀 키보드 쇼핑몰 정보

### 기술 스택

- **Frontend**: React 18 + TypeScript + Emotion
- **Backend**: Python Flask + MySQL
- **AI**: Custom Recommendation Algorithm
- **Database**: MySQL 8.0+ (15,000+ 부품 데이터)

## 🚀 빠른 시작

### 현재 환경에서 재실행 (권장)

```bash
# 1. KLUE 시스템 실행
./quick_start.sh

# 2. 브라우저에서 접속
# http://localhost:3000

# 3. 시스템 종료
./stop_klue.sh
```

### 처음 설치하는 경우

```bash
# 1. 완전 자동 설치 및 실행
./complete_setup.sh

# 2. 또는 수동 설치 (SETUP_GUIDE.md 참조)
```

## 🎮 기능 소개

### 1. 🏠 홈 대시보드

- 실시간 부품 통계 확인
- 카테고리별 부품 개수 표시
- 빠른 네비게이션

### 2. 🤖 AI 키보드 추천

- **용도별 추천**: 게이밍, 타이핑, 프로그래밍
- **예산별 추천**: 10만원 ~ 100만원+
- **취향별 추천**: 스위치 타입, 소음 레벨, 디자인
- **완성품 추천**: 모든 부품이 포함된 완전한 키보드 구성

### 3. 🔧 부품 관리

- **스위치**: 2,000+ 종류 (Linear, Tactile, Clicky)
- **키캡**: 다양한 프로파일과 재질
- **PCB/플레이트**: 레이아웃별 호환성 정보
- **케이스**: 재질, 마운트 방식별 분류

### 4. 🛠️ 키보드 빌드

- 가상 키보드 조립 시뮬레이션
- 부품 호환성 자동 체크
- 총 비용 계산
- 조립 가이드 제공

### 5. 🌐 쇼핑 사이트 가이드

- **해외 브랜드**: KBDfans, Drop, NovelKeys, ZealPC, Keychron
- **국내 브랜드**: 레오폴드, 아이락스, 큐센, 스웨그키
- 각 사이트별 특징, 배송 정보, 가격대 안내

## 🔧 완전 설치

### 전제 조건

```bash
# macOS 기준
brew install python node mysql

# 또는 개별 설치
# Python 3.8+
# Node.js 16+
# MySQL 8.0+
```

### 자동 설치

```bash
# 환경이 전혀 없는 상태에서도 설치 가능
./complete_setup.sh
```

### 수동 설치

자세한 수동 설치 가이드는 [`SETUP_GUIDE.md`](./SETUP_GUIDE.md)를 참조하세요.

## 🚨 문제 해결

### 일반적인 문제들

#### 1. MySQL 연결 오류

```bash
# MySQL 서비스 확인
brew services list | grep mysql

# MySQL 수동 시작
brew services start mysql

# 연결 테스트
mysql -u root -pshin -e "SHOW DATABASES;"
```

#### 2. 포트 충돌

```bash
# 포트 사용 확인
lsof -ti:8080  # 백엔드
lsof -ti:3000  # 프론트엔드

# 프로세스 강제 종료
./stop_klue.sh
```

#### 3. 패키지 오류

```bash
# Python 패키지 재설치
pip3 install flask flask-cors mysql-connector-python numpy

# npm 패키지 재설치
cd klue_client && npm install
```

#### 4. 스크립트 권한 오류

```bash
chmod +x quick_start.sh complete_setup.sh stop_klue.sh
```

### 로그 확인

```bash
# 백엔드 로그
python3 app.py  # 직접 실행하여 오류 확인

# 프론트엔드 로그
cd klue_client && npm start
```

## 🛠️ 개발자 가이드

### 프로젝트 구조

```
klue_project/
├── app.py                 # Flask 백엔드 메인
├── keyboard_recommender.py # MySQL 연결 및 데이터 관리
├── ai_recommender.py      # AI 추천 엔진
├── klue_client/           # React 프론트엔드
│   ├── src/
│   │   ├── components/    # 재사용 컴포넌트
│   │   ├── pages/         # 페이지 컴포넌트
│   │   └── services/      # API 서비스
│   └── package.json
├── quick_start.sh         # 빠른 실행 스크립트
├── complete_setup.sh      # 완전 설치 스크립트
├── stop_klue.sh          # 종료 스크립트
└── SETUP_GUIDE.md        # 상세 설치 가이드
```

### API 엔드포인트

```
GET  /api/health           # 헬스체크
GET  /switches             # 스위치 목록
GET  /keycaps              # 키캡 목록
GET  /pcbs                 # PCB 목록
GET  /plates               # 플레이트 목록
POST /recommend            # AI 추천
POST /recommend_complete_set # 완성품 추천
```

### 개발 환경 실행

```bash
# 백엔드 개발 모드
python3 app.py

# 프론트엔드 개발 모드
cd klue_client && npm start
```

### 데이터베이스 스키마

- **Switches**: 스위치 정보 (브랜드, 타입, 힘, 가격 등)
- **Keycaps**: 키캡 정보 (프로파일, 재질, 색상 등)
- **PCBs**: PCB 정보 (레이아웃, 연결 방식 등)
- **Plates**: 플레이트 정보 (재질, 호환성 등)

## 📞 지원

### 실행 스크립트 요약

| 명령어                | 용도        | 상황                  |
| --------------------- | ----------- | --------------------- |
| `./quick_start.sh`    | 빠른 재실행 | 이미 설치된 환경      |
| `./complete_setup.sh` | 완전 설치   | 처음 설치하는 경우    |
| `./stop_klue.sh`      | 시스템 종료 | 실행 중인 서버들 종료 |

### 접속 주소

- **홈페이지**: http://localhost:3000
- **AI 추천**: http://localhost:3000/ai
- **부품 관리**: http://localhost:3000/parts
- **키보드 빌드**: http://localhost:3000/build
- **쇼핑 가이드**: http://localhost:3000/site
- **백엔드 API**: http://localhost:8080

---

**💡 팁**: 문제가 발생하면 `./stop_klue.sh && ./quick_start.sh`로 재시작해보세요!
