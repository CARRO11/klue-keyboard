# KLUE 키보드 추천 시스템 완전 구축 가이드

이 가이드를 따라하면 현재 KLUE 시스템과 동일한 환경을 구축할 수 있습니다.

## 📋 시스템 요구사항

- macOS (또는 Linux/Windows with WSL)
- Node.js 18+
- Python 3.8+
- MySQL 8.0+
- Git

## 🚀 1단계: 기본 환경 설정

### 필수 도구 설치 (macOS)

```bash
# Homebrew 설치 (이미 있다면 건너뛰기)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Node.js 설치
brew install node

# Python 설치 (이미 있다면 건너뛰기)
brew install python

# MySQL 설치
brew install mysql
```

## 🗄️ 2단계: MySQL 데이터베이스 설정

### MySQL 서버 시작 및 기본 설정

```bash
# MySQL 서버 시작
brew services start mysql

# MySQL root 비밀번호 설정 (비밀번호: shin)
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'shin';"

# 데이터베이스 생성
mysql -u root -pshin -e "CREATE DATABASE klue_keyboard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 테이블 생성
mysql -u root -pshin klue_keyboard << 'EOF'
CREATE TABLE Switches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type ENUM('Linear', 'Tactile', 'Clicky') NOT NULL,
    stem_material VARCHAR(100),
    sound_score DECIMAL(3,1) DEFAULT 5.0,
    linear_score DECIMAL(3,1) DEFAULT 5.0,
    tactile_score DECIMAL(3,1) DEFAULT 5.0,
    weight_score DECIMAL(3,1) DEFAULT 5.0,
    smoothness_score DECIMAL(3,1) DEFAULT 5.0,
    speed_score DECIMAL(3,1) DEFAULT 5.0,
    stability_score DECIMAL(3,1) DEFAULT 5.0,
    durability_score DECIMAL(3,1) DEFAULT 5.0,
    price_tier INT DEFAULT 2,
    link VARCHAR(500),
    startdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    enddate DATETIME DEFAULT '2099-12-31 23:59:59'
);

CREATE TABLE Plate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    material VARCHAR(100),
    thickness DECIMAL(4,2),
    flex_level INT DEFAULT 5,
    sound_profile DECIMAL(3,1) DEFAULT 5.0,
    weight INT,
    price_tier INT DEFAULT 2,
    link VARCHAR(500),
    startdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    enddate DATETIME DEFAULT '2099-12-31 23:59:59'
);

CREATE TABLE Stabilizer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    material VARCHAR(100),
    sound_profile DECIMAL(3,1) DEFAULT 5.0,
    smoothness DECIMAL(3,1) DEFAULT 5.0,
    rattle DECIMAL(3,1) DEFAULT 5.0,
    build_quality DECIMAL(3,1) DEFAULT 5.0,
    price_tier INT DEFAULT 2,
    link VARCHAR(500),
    startdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    enddate DATETIME DEFAULT '2099-12-31 23:59:59'
);

CREATE TABLE Keycap (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    material VARCHAR(100),
    profile VARCHAR(100),
    thickness DECIMAL(4,2),
    sound_profile DECIMAL(3,1) DEFAULT 5.0,
    durability DECIMAL(3,1) DEFAULT 5.0,
    rgb_compatible BOOLEAN DEFAULT TRUE,
    price_tier INT DEFAULT 2,
    link VARCHAR(500),
    startdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    enddate DATETIME DEFAULT '2099-12-31 23:59:59'
);

CREATE TABLE PCB (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    build_quality DECIMAL(3,1) DEFAULT 5.0,
    features TEXT,
    rgb_support BOOLEAN DEFAULT TRUE,
    qmk_via BOOLEAN DEFAULT TRUE,
    flex DECIMAL(3,1) DEFAULT 5.0,
    price_tier INT DEFAULT 2,
    link VARCHAR(500),
    startdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    enddate DATETIME DEFAULT '2099-12-31 23:59:59'
);
EOF

# 샘플 데이터 삽입
mysql -u root -pshin klue_keyboard << 'EOF'
INSERT INTO Switches (name, type, stem_material, sound_score, linear_score, tactile_score, weight_score, smoothness_score, speed_score, stability_score, durability_score, price_tier, link) VALUES
('체리 MX 레드', 'Linear', 'POM', 6.0, 9.0, 2.0, 6.0, 8.0, 9.0, 8.0, 9.0, 2, 'https://kbdfans.com/collections/switches'),
('가테론 옐로우', 'Linear', 'POM', 7.0, 8.0, 2.0, 7.0, 9.0, 8.0, 8.0, 8.0, 2, 'https://kbdfans.com/collections/switches'),
('체리 MX 브라운', 'Tactile', 'POM', 5.0, 3.0, 8.0, 6.0, 7.0, 7.0, 8.0, 9.0, 2, 'https://kbdfans.com/collections/switches'),
('Holy Panda', 'Tactile', 'POM', 8.0, 2.0, 9.0, 8.0, 8.0, 7.0, 9.0, 8.0, 4, 'https://drop.com/buy/drop-invyr-holy-panda-mechanical-switches'),
('가테론 옥옥', 'Linear', 'POM', 4.0, 9.0, 1.0, 5.0, 9.0, 9.0, 8.0, 8.0, 3, 'https://kbdfans.com/collections/switches'),
('칼리 박스 제이드', 'Clicky', 'POM', 9.0, 1.0, 3.0, 7.0, 6.0, 6.0, 8.0, 9.0, 3, 'https://kbdfans.com/collections/switches'),
('Zealios V2', 'Tactile', 'POM', 6.0, 2.0, 9.0, 7.0, 9.0, 7.0, 9.0, 9.0, 4, 'https://zealpc.net/products/zealio'),
('탠저린 스위치', 'Linear', 'UHMWPE', 8.0, 9.0, 1.0, 6.0, 10.0, 9.0, 8.0, 8.0, 3, 'https://www.primekb.com/products/tangerine-switches'),
('듀록 V2', 'Linear', 'POM', 7.0, 9.0, 1.0, 7.0, 9.0, 8.0, 9.0, 8.0, 3, 'https://kbdfans.com/collections/switches'),
('바나나 스플릿', 'Linear', 'POM', 6.0, 8.0, 2.0, 6.0, 8.0, 8.0, 8.0, 8.0, 3, 'https://thekey.company/products/banana-split-switches'),
('아쿠아킹 V3', 'Linear', 'POM', 5.0, 9.0, 1.0, 5.0, 9.0, 9.0, 8.0, 8.0, 3, 'https://kbdfans.com/collections/switches'),
('지온 스위치', 'Linear', 'POM', 7.0, 8.0, 2.0, 6.0, 8.0, 8.0, 8.0, 8.0, 3, 'https://kbdfans.com/collections/switches'),
('사일런트 알파카', 'Linear', 'POM', 2.0, 9.0, 1.0, 6.0, 9.0, 8.0, 8.0, 8.0, 4, 'https://primekb.com/products/alpaca-linears'),
('옵티카 블랙', 'Linear', 'Optical', 8.0, 9.0, 1.0, 7.0, 8.0, 10.0, 8.0, 9.0, 3, 'https://kbdfans.com/collections/switches'),
('NK 크림', 'Linear', 'POM', 6.0, 8.0, 2.0, 7.0, 8.0, 8.0, 8.0, 8.0, 3, 'https://novelkeys.xyz/products/nk_-cream-switches');

INSERT INTO Keycap (name, material, profile, thickness, sound_profile, durability, rgb_compatible, price_tier, link) VALUES
('Cherry Profile PBT 키캡', 'PBT', 'Cherry', 1.4, 7.0, 9.0, TRUE, 2, 'https://kbdfans.com/collections/keycaps'),
('OEM Profile ABS 키캡', 'ABS', 'OEM', 1.2, 8.0, 6.0, TRUE, 1, 'https://kbdfans.com/collections/keycaps'),
('SA Profile PBT 키캡', 'PBT', 'SA', 1.6, 9.0, 9.0, FALSE, 3, 'https://drop.com/mechanical-keyboards/keycaps'),
('XDA Profile PBT 키캡', 'PBT', 'XDA', 1.5, 6.0, 8.0, TRUE, 2, 'https://kbdfans.com/collections/keycaps');

INSERT INTO PCB (name, build_quality, features, rgb_support, qmk_via, flex, price_tier, link) VALUES
('DZ60 RGB PCB', 8.0, 'RGB, 핫스왑', TRUE, TRUE, 6.0, 2, 'https://kbdfans.com/collections/pcb'),
('Tofu65 PCB', 9.0, '핫스왑, USB-C', TRUE, TRUE, 7.0, 3, 'https://kbdfans.com/collections/pcb'),
('NK65 Entry PCB', 7.0, '핫스왑, 무선', TRUE, FALSE, 5.0, 2, 'https://novelkeys.xyz/collections/keyboards');

INSERT INTO Plate (name, material, thickness, flex_level, sound_profile, weight, price_tier, link) VALUES
('알루미늄 플레이트', 'Aluminum', 1.5, 5, 7.0, 150, 2, 'https://kbdfans.com/collections/plate'),
('PC 플레이트', 'Polycarbonate', 1.2, 8, 5.0, 80, 3, 'https://kbdfans.com/collections/plate'),
('FR4 플레이트', 'FR4', 1.6, 7, 6.0, 120, 2, 'https://kbdfans.com/collections/plate');

INSERT INTO Stabilizer (name, type, material, sound_profile, smoothness, rattle, build_quality, price_tier, link) VALUES
('Cherry 클립인 스테빌라이저', 'Clip-in', 'PC', 6.0, 7.0, 3.0, 8.0, 2, 'https://kbdfans.com/collections/stabilizer'),
('Durock V2 스테빌라이저', 'Screw-in', 'PC', 8.0, 9.0, 2.0, 9.0, 3, 'https://kbdfans.com/collections/stabilizer'),
('Zeal 스테빌라이저', 'Screw-in', 'PC', 9.0, 9.0, 1.0, 10.0, 4, 'https://zealpc.net/collections/stabilizers');
EOF

echo "✅ MySQL 데이터베이스 설정 완료!"
```

## 📁 3단계: 프로젝트 클론 및 기본 구조

```bash
# 프로젝트 클론 (또는 새로 생성)
git clone https://github.com/CARRO11/klue-keyboard.git klue_project
cd klue_project

# 또는 새 프로젝트 생성 시:
# mkdir klue_project && cd klue_project
# git init
```

## 🐍 4단계: 백엔드 (Python Flask) 설정

### Python 의존성 설치

```bash
# 가상환경 생성 (선택사항)
python -m venv venv
source venv/bin/activate  # macOS/Linux
# venv\Scripts\activate     # Windows

# 필수 패키지 설치
pip install flask flask-cors mysql-connector-python numpy python-dotenv

# 또는 requirements.txt 생성 후 설치
cat << 'EOF' > requirements.txt
flask==2.3.3
flask-cors==4.0.0
mysql-connector-python==8.1.0
numpy==1.24.3
python-dotenv==1.0.0
EOF

pip install -r requirements.txt
```

### AI 추천 모듈 생성

```bash
cat << 'EOF' > ai_recommender.py
import random
from typing import Dict, List, Optional
import json

class AIRecommender:
    def __init__(self):
        self.preference_keywords = {
            # 용도별 키워드
            'gaming': {'게이밍', '게임', 'gaming', '빠른', '반응속도', '경쟁'},
            'office': {'사무', '사무용', 'office', '조용한', '업무', '회사'},
            'typing': {'타이핑', 'typing', '글쓰기', '작업', '장시간'},

            # 소리 관련
            'quiet': {'조용한', '무음', '사일런트', 'quiet', 'silent', '소음없는'},
            'loud': {'시끄러운', '큰소리', 'loud', '클릭키', 'clicky'},

            # 스위치 타입
            'linear': {'리니어', 'linear', '선형', '부드러운', 'smooth'},
            'tactile': {'택타일', 'tactile', '촉감', '툭툭', '단계감'},
            'clicky': {'클릭키', 'clicky', '딸깍', '소리'},

            # 가격 관련
            'budget': {'저렴한', '싼', '가성비', 'budget', '엔트리'},
            'premium': {'고급', '프리미엄', 'premium', '최고급', '비싼'}
        }

    def parse_natural_language_to_preferences(self, user_input: str) -> Dict:
        """자연어 입력을 선호도 딕셔너리로 변환"""
        user_input_lower = user_input.lower()

        preferences = {
            'sound_profile': 5,
            'tactile_score': 5,
            'speed_score': 5,
            'price_tier': 2,
            'build_quality': 7,
            'rgb_compatible': False,
            'switch_type': 'Linear'
        }

        # 용도 분석
        if any(keyword in user_input_lower for keyword in self.preference_keywords['gaming']):
            preferences.update({
                'sound_profile': 7,
                'tactile_score': 3,
                'speed_score': 9,
                'price_tier': 3,
                'rgb_compatible': True,
                'switch_type': 'Linear'
            })
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['office']):
            preferences.update({
                'sound_profile': 2,
                'tactile_score': 4,
                'speed_score': 6,
                'price_tier': 2,
                'rgb_compatible': False,
                'switch_type': 'Linear'
            })
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['typing']):
            preferences.update({
                'sound_profile': 6,
                'tactile_score': 7,
                'speed_score': 7,
                'price_tier': 3,
                'rgb_compatible': False,
                'switch_type': 'Tactile'
            })

        # 소리 분석
        if any(keyword in user_input_lower for keyword in self.preference_keywords['quiet']):
            preferences['sound_profile'] = 2
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['loud']):
            preferences['sound_profile'] = 8

        # 스위치 타입 분석
        if any(keyword in user_input_lower for keyword in self.preference_keywords['linear']):
            preferences['switch_type'] = 'Linear'
            preferences['tactile_score'] = 2
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['tactile']):
            preferences['switch_type'] = 'Tactile'
            preferences['tactile_score'] = 8
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['clicky']):
            preferences['switch_type'] = 'Clicky'
            preferences['sound_profile'] = 9

        # 가격 분석
        if any(keyword in user_input_lower for keyword in self.preference_keywords['budget']):
            preferences['price_tier'] = 1
        elif any(keyword in user_input_lower for keyword in self.preference_keywords['premium']):
            preferences['price_tier'] = 4
            preferences['build_quality'] = 9

        # RGB 분석
        if any(keyword in user_input_lower for keyword in ['rgb', '조명', '빛', '예쁜', '이쁜']):
            preferences['rgb_compatible'] = True

        return preferences

    def generate_natural_language_explanation(self, user_request: str, recommendations: Dict, preferences: Dict, system_prompt: str = None) -> str:
        """자연어 추천 설명 생성"""

        # 기본 시스템 프롬프트
        if not system_prompt:
            system_prompt = "당신은 키보드 전문가 Tony입니다. 친근하고 전문적인 톤으로 추천해주세요."

        # 간단한 템플릿 기반 설명 생성
        explanation_parts = []

        # 인사말
        explanation_parts.append(f"안녕하세요! 키보드 소믈리에 Tony입니다. 🎩")
        explanation_parts.append(f'"{user_request}"라는 요청을 분석해봤어요!')
        explanation_parts.append("")

        # 스위치 추천 설명
        if 'switches' in recommendations and recommendations['switches']:
            switch = recommendations['switches'][0]
            explanation_parts.append("🔧 **스위치 추천:**")
            explanation_parts.append(f"가장 적합한 스위치는 **{switch['name']}**입니다.")

            if preferences.get('switch_type') == 'Linear':
                explanation_parts.append("리니어 스위치로 부드럽고 일관된 타감을 제공해요.")
            elif preferences.get('switch_type') == 'Tactile':
                explanation_parts.append("택타일 스위치로 적당한 촉감과 피드백을 느낄 수 있어요.")

            explanation_parts.append("")

        # 키캡 추천 설명
        if 'keycaps' in recommendations and recommendations['keycaps']:
            keycap = recommendations['keycaps'][0]
            explanation_parts.append("🎨 **키캡 추천:**")
            explanation_parts.append(f"**{keycap['name']}**을 추천드려요.")
            explanation_parts.append("내구성과 타감을 모두 고려한 선택입니다.")
            explanation_parts.append("")

        # 가격대 설명
        price_tier = preferences.get('price_tier', 2)
        price_descriptions = {
            1: "가성비를 중시하신다면",
            2: "적당한 예산으로도",
            3: "좋은 품질을 원하신다면",
            4: "최고급 제품으로"
        }
        explanation_parts.append(f"💰 **예산 고려사항:**")
        explanation_parts.append(f"{price_descriptions.get(price_tier, '적당한 예산으로도')} 충분히 만족스러운 키보드를 만들 수 있어요!")
        explanation_parts.append("")

        # 마무리
        explanation_parts.append("각 부품의 구매 링크를 확인하시고, 즐거운 키보드 라이프 되세요! 🎹✨")

        return "\n".join(explanation_parts)

    def generate_recommendation_explanation(self, recommendations: Dict, preferences: Dict) -> str:
        """상세 선호도 기반 추천 설명 생성"""
        explanation_parts = []

        explanation_parts.append("🎯 **맞춤 추천 결과입니다!**")
        explanation_parts.append("")

        # 선호도 요약
        sound_level = preferences.get('sound_profile', 5)
        if sound_level <= 3:
            sound_desc = "조용한 환경을 선호하시는군요!"
        elif sound_level >= 7:
            sound_desc = "시원한 타격감을 좋아하시네요!"
        else:
            sound_desc = "적당한 소리를 선호하시는군요!"

        explanation_parts.append(f"🔊 {sound_desc}")

        # 추천 부품 수
        total_components = sum(len(items) for items in recommendations.values())
        explanation_parts.append(f"총 {total_components}개의 추천 부품을 선별했습니다.")
        explanation_parts.append("")
        explanation_parts.append("각 부품은 여러분의 선호도를 종합적으로 분석하여 추천되었어요! 🎪")

        return "\n".join(explanation_parts)
EOF
```

### 메인 백엔드 파일들 생성

```bash
# keyboard_recommender.py는 이미 제공된 파일 사용
# app.py는 이미 제공된 파일 사용
```

## ⚛️ 5단계: 프론트엔드 (React) 설정

### React 프로젝트 생성

```bash
# React 프로젝트 생성
npx create-react-app klue_client --template typescript
cd klue_client

# 필수 의존성 설치
npm install @emotion/react @emotion/styled react-router-dom

# 타입 정의 설치
npm install --save-dev @types/react-router-dom

cd ..
```

### 프론트엔드 파일 구조 생성

```bash
# 필요한 디렉토리 생성
mkdir -p klue_client/src/components
mkdir -p klue_client/src/pages
mkdir -p klue_client/src/services

# 컴포넌트와 페이지 파일들은 이미 제공된 파일들 사용
```

## 🔧 6단계: 환경 설정 파일

### 환경변수 파일 생성

```bash
cat << 'EOF' > .env
# 데이터베이스 설정
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=shin
DB_NAME=klue_keyboard

# 서버 설정
FLASK_HOST=127.0.0.1
FLASK_PORT=8080
FLASK_DEBUG=True

# 프론트엔드 설정
REACT_APP_API_URL=http://localhost:8080
EOF
```

## 🚀 7단계: 실행 명령어

### 백엔드 실행

```bash
# 터미널 1: 백엔드 서버 실행
cd klue_project
python app.py

# 또는 배경에서 실행
python app.py &
```

### 프론트엔드 실행

```bash
# 터미널 2: 프론트엔드 개발 서버 실행
cd klue_project/klue_client
npm start
```

### MySQL 서비스 관리

```bash
# MySQL 시작
brew services start mysql

# MySQL 중지
brew services stop mysql

# MySQL 재시작
brew services restart mysql

# MySQL 상태 확인
brew services list | grep mysql
```

## 📊 8단계: 확인 및 테스트

### 서비스 동작 확인

```bash
# 백엔드 API 테스트
curl http://localhost:8080/api/health

# 데이터베이스 연결 테스트
curl http://localhost:8080/switches

# AI 추천 테스트
curl -X POST http://localhost:8080/api/recommend/natural \
  -H "Content-Type: application/json" \
  -d '{"message": "조용한 사무용 키보드"}'
```

### 브라우저 접속

```
프론트엔드: http://localhost:3000
백엔드 API: http://localhost:8080
```

## 🛠️ 9단계: 개발 도구 명령어

### Git 관리

```bash
# 변경사항 저장
git add .
git commit -m "feat: 초기 설정 완료"
git push origin main

# 상태 확인
git status
git log --oneline
```

### 디버깅 명령어

```bash
# 프로세스 확인
ps aux | grep python
ps aux | grep node

# 포트 사용 확인
lsof -i :8080  # 백엔드 포트
lsof -i :3000  # 프론트엔드 포트

# 로그 확인
tail -f /var/log/mysql/error.log  # MySQL 로그
```

## 🔄 10단계: 일상적인 실행 스크립트

### 전체 시스템 시작 스크립트

```bash
cat << 'EOF' > start_klue.sh
#!/bin/bash
echo "🚀 KLUE 키보드 추천 시스템 시작"

# MySQL 시작
echo "📚 MySQL 시작 중..."
brew services start mysql
sleep 3

# 백엔드 시작
echo "🐍 백엔드 서버 시작 중..."
cd klue_project
python app.py &
BACKEND_PID=$!
sleep 5

# 프론트엔드 시작
echo "⚛️ 프론트엔드 시작 중..."
cd klue_client
npm start &
FRONTEND_PID=$!

echo "✅ 모든 서비스가 시작되었습니다!"
echo "프론트엔드: http://localhost:3000"
echo "백엔드: http://localhost:8080"
echo ""
echo "종료하려면 Ctrl+C를 누르세요"

# 백그라운드 프로세스 ID 저장
echo $BACKEND_PID > backend.pid
echo $FRONTEND_PID > frontend.pid

wait
EOF

chmod +x start_klue.sh
```

### 시스템 종료 스크립트

```bash
cat << 'EOF' > stop_klue.sh
#!/bin/bash
echo "🛑 KLUE 시스템 종료 중..."

# 저장된 PID로 프로세스 종료
if [ -f backend.pid ]; then
    kill $(cat backend.pid) 2>/dev/null
    rm backend.pid
fi

if [ -f frontend.pid ]; then
    kill $(cat frontend.pid) 2>/dev/null
    rm frontend.pid
fi

# 포트로 실행 중인 프로세스 종료
pkill -f "python app.py"
pkill -f "npm start"

echo "✅ KLUE 시스템이 종료되었습니다."
EOF

chmod +x stop_klue.sh
```

## 📝 완료!

이제 다음 명령어로 전체 시스템을 실행할 수 있습니다:

```bash
# 전체 시스템 시작
./start_klue.sh

# 또는 개별 실행
# 터미널 1: python app.py
# 터미널 2: cd klue_client && npm start
```

## 🎯 주요 접속 URL

- **홈페이지**: http://localhost:3000
- **AI 추천**: http://localhost:3000/ai
- **부품 관리**: http://localhost:3000/parts
- **키보드 빌드**: http://localhost:3000/build
- **쇼핑 사이트**: http://localhost:3000/site
- **백엔드 API**: http://localhost:8080

---

이 가이드를 따라하면 현재와 동일한 KLUE 키보드 추천 시스템을 구축할 수 있습니다! 🎉
