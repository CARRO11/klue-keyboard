#!/bin/bash

echo "🛑 KLUE 키보드 추천 시스템 종료 중..."

# 저장된 PID로 프로세스 종료
if [ -f backend.pid ]; then
    BACKEND_PID=$(cat backend.pid)
    echo "🐍 백엔드 서버 종료 (PID: $BACKEND_PID)"
    kill $BACKEND_PID 2>/dev/null
    rm backend.pid
else
    echo "⚠️ backend.pid 파일이 없음"
fi

if [ -f frontend.pid ]; then
    FRONTEND_PID=$(cat frontend.pid)
    echo "⚛️ 프론트엔드 서버 종료 (PID: $FRONTEND_PID)"
    kill $FRONTEND_PID 2>/dev/null
    rm frontend.pid
else
    echo "⚠️ frontend.pid 파일이 없음"
fi

# 포트로 실행 중인 프로세스 강제 종료
echo "🔍 포트별 프로세스 확인 및 종료..."
pkill -f "python app.py" 2>/dev/null && echo "  - Python 프로세스 종료"
pkill -f "npm start" 2>/dev/null && echo "  - npm 프로세스 종료"

# 포트 사용 확인
if lsof -ti:8080 > /dev/null 2>&1; then
    echo "⚠️ 포트 8080이 여전히 사용 중입니다."
    echo "   수동 종료: lsof -ti:8080 | xargs kill"
fi

if lsof -ti:3000 > /dev/null 2>&1; then
    echo "⚠️ 포트 3000이 여전히 사용 중입니다."
    echo "   수동 종료: lsof -ti:3000 | xargs kill"
fi

echo ""
echo "✅ KLUE 시스템이 종료되었습니다."
echo "📝 MySQL은 계속 실행 중입니다. (필요시 brew services stop mysql로 종료)" 