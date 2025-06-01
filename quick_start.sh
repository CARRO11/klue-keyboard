#!/bin/bash

echo "🚀 KLUE 키보드 추천 시스템 완전 실행 스크립트"
echo "현재 위치: $(pwd)"
echo "시간: $(date)"
echo ""

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 함수 정의
log_info() { echo -e "${GREEN}✅ $1${NC}"; }
log_warn() { echo -e "${YELLOW}⚠️ $1${NC}"; }
log_error() { echo -e "${RED}❌ $1${NC}"; }

# 1. 기본 환경 점검
echo "🔍 1단계: 기본 환경 점검"

# Python 확인
if command -v python3 &> /dev/null; then
    log_info "Python3: $(python3 --version)"
elif command -v python &> /dev/null; then
    log_info "Python: $(python --version)"
    alias python3=python
else
    log_error "Python이 설치되지 않았습니다."
    exit 1
fi

# Node.js 확인
if command -v node &> /dev/null; then
    log_info "Node.js: $(node --version)"
else
    log_error "Node.js가 설치되지 않았습니다."
    exit 1
fi

# MySQL 확인
if command -v mysql &> /dev/null; then
    log_info "MySQL 클라이언트 설치됨"
else
    log_error "MySQL이 설치되지 않았습니다."
    exit 1
fi

echo ""

# 2. MySQL 서비스 시작 및 확인
echo "🗄️ 2단계: MySQL 데이터베이스 확인"

# MySQL 서비스 시작
echo "📚 MySQL 서비스 시작..."
if command -v brew &> /dev/null; then
    brew services start mysql &> /dev/null
    sleep 3
else
    log_warn "Homebrew가 없습니다. MySQL을 수동으로 시작해주세요."
fi

# 데이터베이스 연결 및 테이블 확인
echo "🔍 데이터베이스 연결 테스트..."
if mysql -u root -pshin -e "USE klue_keyboard; SELECT COUNT(*) FROM Switches;" &> /dev/null; then
    SWITCH_COUNT=$(mysql -u root -pshin -e "USE klue_keyboard; SELECT COUNT(*) as count FROM Switches;" -N 2>/dev/null | tail -1)
    log_info "MySQL 연결 성공! 스위치 데이터: $SWITCH_COUNT 개"
else
    log_error "MySQL 연결 실패 또는 데이터베이스가 없습니다."
    echo "다음 명령어로 수동 확인:"
    echo "  mysql -u root -pshin -e \"SHOW DATABASES;\""
    exit 1
fi

echo ""

# 3. Python 의존성 확인
echo "🐍 3단계: Python 백엔드 환경 확인"

# 필수 Python 패키지 확인 및 설치
REQUIRED_PACKAGES=("flask" "flask-cors" "mysql-connector-python" "numpy")
MISSING_PACKAGES=()

for package in "${REQUIRED_PACKAGES[@]}"; do
    if python3 -c "import ${package//-/_}" &> /dev/null; then
        log_info "Python 패키지 '$package' 설치됨"
    else
        log_warn "Python 패키지 '$package' 누락됨"
        MISSING_PACKAGES+=("$package")
    fi
done

# 누락된 패키지 설치
if [ ${#MISSING_PACKAGES[@]} -gt 0 ]; then
    echo "📦 누락된 패키지 설치 중..."
    pip3 install "${MISSING_PACKAGES[@]}" || {
        log_error "패키지 설치 실패. 수동으로 설치해주세요:"
        echo "  pip3 install ${MISSING_PACKAGES[*]}"
        exit 1
    }
    log_info "패키지 설치 완료"
fi

# 백엔드 파일 확인
if [ ! -f "app.py" ]; then
    log_error "app.py 파일이 없습니다!"
    exit 1
fi

if [ ! -f "keyboard_recommender.py" ]; then
    log_error "keyboard_recommender.py 파일이 없습니다!"
    exit 1
fi

if [ ! -f "ai_recommender.py" ]; then
    log_error "ai_recommender.py 파일이 없습니다!"
    exit 1
fi

log_info "백엔드 파일들 확인 완료"
echo ""

# 4. 프론트엔드 환경 확인
echo "⚛️ 4단계: React 프론트엔드 환경 확인"

if [ ! -d "klue_client" ]; then
    log_error "klue_client 디렉토리가 없습니다!"
    exit 1
fi

cd klue_client

# package.json 확인
if [ ! -f "package.json" ]; then
    log_error "package.json이 없습니다!"
    exit 1
fi

# node_modules 확인
if [ ! -d "node_modules" ]; then
    log_warn "node_modules가 없습니다. npm install 실행 중..."
    npm install || {
        log_error "npm install 실패"
        exit 1
    }
    log_info "npm install 완료"
else
    log_info "node_modules 확인됨"
fi

cd ..
echo ""

# 5. 이전 프로세스 정리
echo "🧹 5단계: 이전 프로세스 정리"

# 기존 프로세스 종료
pkill -f "python.*app.py" &> /dev/null && log_info "이전 백엔드 프로세스 종료"
pkill -f "npm.*start" &> /dev/null && log_info "이전 프론트엔드 프로세스 종료"

# PID 파일 정리
[ -f backend.pid ] && rm backend.pid && log_info "이전 backend.pid 파일 제거"
[ -f frontend.pid ] && rm frontend.pid && log_info "이전 frontend.pid 파일 제거"

echo ""

# 6. 백엔드 시작
echo "🐍 6단계: 백엔드 서버 시작"

python3 app.py &
BACKEND_PID=$!
echo "백엔드 PID: $BACKEND_PID"
echo $BACKEND_PID > backend.pid

# 백엔드 시작 대기
echo "⏳ 백엔드 서버 시작 대기 중..."
for i in {1..15}; do
    if curl -s http://localhost:8080/api/health &> /dev/null; then
        log_info "백엔드 서버 실행 성공! (${i}초 후)"
        break
    fi
    if [ $i -eq 15 ]; then
        log_error "백엔드 서버 시작 시간 초과"
        kill $BACKEND_PID 2>/dev/null
        exit 1
    fi
    sleep 1
done

# API 테스트
SWITCH_API_TEST=$(curl -s http://localhost:8080/switches | jq -r '.success' 2>/dev/null)
if [ "$SWITCH_API_TEST" = "true" ]; then
    log_info "스위치 API 테스트 성공"
else
    log_warn "스위치 API 테스트 실패 (하지만 계속 진행)"
fi

echo ""

# 7. 프론트엔드 시작
echo "⚛️ 7단계: 프론트엔드 서버 시작"

cd klue_client

# 환경 변수 설정
export BROWSER=none
export CI=true

npm start &
FRONTEND_PID=$!
cd ..

echo "프론트엔드 PID: $FRONTEND_PID"
echo $FRONTEND_PID > frontend.pid

# 프론트엔드 시작 대기
echo "⏳ 프론트엔드 서버 시작 대기 중..."
for i in {1..30}; do
    if curl -s http://localhost:3000 &> /dev/null; then
        log_info "프론트엔드 서버 실행 성공! (${i}초 후)"
        break
    fi
    if [ $i -eq 30 ]; then
        log_warn "프론트엔드 서버 확인 시간 초과 (하지만 백그라운드에서 실행 중)"
        break
    fi
    sleep 1
done

echo ""

# 8. 최종 상태 확인 및 안내
echo "🎉 KLUE 키보드 추천 시스템이 실행되었습니다!"
echo ""
echo "📊 시스템 상태:"
echo "  🗄️ MySQL: $(brew services list | grep mysql | awk '{print $2}' 2>/dev/null || echo '실행 중')"
echo "  🐍 백엔드: PID $BACKEND_PID (포트 8080)"
echo "  ⚛️ 프론트엔드: PID $FRONTEND_PID (포트 3000)"
echo ""
echo "📱 접속 정보:"
echo "  🏠 홈페이지: http://localhost:3000"
echo "  🤖 AI 추천: http://localhost:3000/ai"
echo "  🔧 부품 관리: http://localhost:3000/parts"
echo "  🛠️ 키보드 빌드: http://localhost:3000/build"
echo "  🌐 쇼핑 사이트: http://localhost:3000/site"
echo "  🔌 백엔드 API: http://localhost:8080"
echo ""
echo "🛑 시스템 종료: ./stop_klue.sh"
echo "🔄 재시작: ./stop_klue.sh && ./quick_start.sh"
echo ""
echo "⏰ 시작 완료 시간: $(date)"
echo ""

# 자동으로 브라우저 열기 (선택사항)
read -p "🌐 브라우저를 자동으로 열까요? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    open http://localhost:3000 2>/dev/null || {
        echo "브라우저를 수동으로 열어주세요: http://localhost:3000"
    }
fi

# 백그라운드에서 실행 유지
echo "💻 시스템이 백그라운드에서 실행 중입니다..."
echo "   터미널을 닫아도 계속 실행됩니다."
echo "   Ctrl+C로 이 스크립트만 종료하거나, ./stop_klue.sh로 전체 종료"
echo ""

wait 