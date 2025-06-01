#!/bin/bash

echo "🚀 KLUE 키보드 추천 시스템 - 완전 자동 설치 및 실행"
echo "⚠️ 이 스크립트는 환경이 전혀 없는 상태에서도 KLUE를 구축합니다."
echo ""

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() { echo -e "${GREEN}✅ $1${NC}"; }
log_warn() { echo -e "${YELLOW}⚠️ $1${NC}"; }
log_error() { echo -e "${RED}❌ $1${NC}"; }
log_step() { echo -e "${BLUE}🔄 $1${NC}"; }

# 체크 필요한 경우만 설치 진행 여부 묻기
echo "이 스크립트는 다음을 자동으로 설치/설정합니다:"
echo "• Python 패키지 (flask, mysql-connector-python 등)"
echo "• Node.js 패키지 (npm install)"
echo "• MySQL 서비스 시작"
echo "• 백엔드/프론트엔드 서버 실행"
echo ""
read -p "계속 진행하시겠습니까? (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "설치가 취소되었습니다."
    exit 1
fi

# 1. 환경 점검 및 설치
log_step "1단계: 기본 환경 점검 및 설치"

# Python 확인/설치
if command -v python3 &> /dev/null; then
    log_info "Python3 이미 설치됨: $(python3 --version)"
elif command -v python &> /dev/null; then
    log_info "Python 설치됨: $(python --version)"
    alias python3=python
else
    log_error "Python이 필요합니다. 먼저 Python을 설치해주세요."
    echo "macOS: brew install python"
    exit 1
fi

# Node.js 확인
if command -v node &> /dev/null; then
    log_info "Node.js 이미 설치됨: $(node --version)"
else
    log_error "Node.js가 필요합니다. 먼저 Node.js를 설치해주세요."
    echo "macOS: brew install node"
    exit 1
fi

# MySQL 확인
if command -v mysql &> /dev/null; then
    log_info "MySQL 클라이언트 설치됨"
else
    log_error "MySQL이 필요합니다. 먼저 MySQL을 설치해주세요."
    echo "macOS: brew install mysql"
    exit 1
fi

# 2. Python 패키지 설치
log_step "2단계: Python 패키지 설치"
REQUIRED_PACKAGES=("flask" "flask-cors" "mysql-connector-python" "numpy")

for package in "${REQUIRED_PACKAGES[@]}"; do
    if python3 -c "import ${package//-/_}" &> /dev/null; then
        log_info "Python 패키지 '$package' 이미 설치됨"
    else
        log_warn "Python 패키지 '$package' 설치 중..."
        pip3 install "$package" || {
            log_error "패키지 '$package' 설치 실패"
            exit 1
        }
        log_info "패키지 '$package' 설치 완료"
    fi
done

# 3. MySQL 설정
log_step "3단계: MySQL 데이터베이스 설정"

# MySQL 서비스 시작
if command -v brew &> /dev/null; then
    log_info "MySQL 서비스 시작 중..."
    brew services start mysql &> /dev/null
    sleep 5
fi

# 데이터베이스 및 테이블 확인/생성
log_info "데이터베이스 연결 테스트..."
if mysql -u root -pshin -e "USE klue_keyboard;" &> /dev/null; then
    log_info "klue_keyboard 데이터베이스 이미 존재"
else
    log_warn "klue_keyboard 데이터베이스 생성 중..."
    mysql -u root -pshin -e "CREATE DATABASE IF NOT EXISTS klue_keyboard;" || {
        log_error "데이터베이스 생성 실패. MySQL 설정을 확인해주세요."
        exit 1
    }
    
    # 기본 테이블 생성
    log_info "기본 테이블 생성 중..."
    mysql -u root -pshin klue_keyboard << 'EOF'
CREATE TABLE IF NOT EXISTS Switches (
    switch_id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100),
    name VARCHAR(100),
    type ENUM('Linear', 'Tactile', 'Clicky'),
    actuation_force DECIMAL(4,1),
    bottom_out_force DECIMAL(4,1),
    pre_travel DECIMAL(3,1),
    total_travel DECIMAL(3,1),
    price INT,
    link VARCHAR(500)
);

INSERT IGNORE INTO Switches (switch_id, brand, name, type, actuation_force, bottom_out_force, pre_travel, total_travel, price, link) VALUES
(1, 'Cherry', 'MX Red', 'Linear', 45.0, 55.0, 2.0, 4.0, 8500, 'https://example.com'),
(2, 'Cherry', 'MX Blue', 'Clicky', 50.0, 60.0, 2.2, 4.0, 9000, 'https://example.com');
EOF
    log_info "테이블 생성 완료"
fi

# 4. 프론트엔드 설정
log_step "4단계: React 프론트엔드 설정"

if [ ! -d "klue_client" ]; then
    log_error "klue_client 디렉토리가 없습니다!"
    exit 1
fi

cd klue_client

if [ ! -f "package.json" ]; then
    log_error "package.json이 없습니다!"
    exit 1
fi

# npm 의존성 설치
if [ ! -d "node_modules" ]; then
    log_info "npm 패키지 설치 중... (시간이 좀 걸릴 수 있습니다)"
    npm install || {
        log_error "npm install 실패"
        exit 1
    }
    log_info "npm 패키지 설치 완료"
else
    log_info "npm 패키지 이미 설치됨"
fi

cd ..

# 5. 백엔드 파일 확인
log_step "5단계: 백엔드 파일 확인"

REQUIRED_FILES=("app.py" "keyboard_recommender.py" "ai_recommender.py")
for file in "${REQUIRED_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        log_error "$file 파일이 없습니다!"
        exit 1
    else
        log_info "$file 파일 확인됨"
    fi
done

# 6. 시스템 실행
log_step "6단계: KLUE 시스템 실행"

# 기존 프로세스 정리
pkill -f "python.*app.py" &> /dev/null
pkill -f "npm.*start" &> /dev/null
rm -f backend.pid frontend.pid

# 백엔드 시작
log_info "백엔드 서버 시작 중..."
python3 app.py &
BACKEND_PID=$!
echo $BACKEND_PID > backend.pid
sleep 5

# 백엔드 확인
if curl -s http://localhost:8080/api/health &> /dev/null; then
    log_info "백엔드 서버 실행 성공!"
else
    log_error "백엔드 서버 시작 실패"
    kill $BACKEND_PID 2>/dev/null
    exit 1
fi

# 프론트엔드 시작
log_info "프론트엔드 서버 시작 중..."
cd klue_client
export BROWSER=none
npm start &
FRONTEND_PID=$!
cd ..
echo $FRONTEND_PID > frontend.pid

echo ""
echo "🎉 KLUE 키보드 추천 시스템 설치 및 실행 완료!"
echo ""
echo "📱 접속 정보:"
echo "  🏠 홈페이지: http://localhost:3000"
echo "  🤖 AI 추천: http://localhost:3000/ai"
echo "  🔧 부품 관리: http://localhost:3000/parts"
echo "  🔌 백엔드 API: http://localhost:8080"
echo ""
echo "🛑 시스템 종료: ./stop_klue.sh"
echo "🔄 다음 실행: ./quick_start.sh"
echo ""

# 브라우저 자동 열기
if command -v open &> /dev/null; then
    sleep 3
    open http://localhost:3000 &> /dev/null
fi

wait 