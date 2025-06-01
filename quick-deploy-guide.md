# 🚀 KLUE 키보드 시스템 5분 배포 가이드

## 🎯 목표

- 프론트엔드: Vercel (무료)
- 백엔드: Railway (무료)
- 데이터베이스: Railway MySQL (무료)

## 📋 준비물

- GitHub 계정
- 현재 프로젝트 코드

## 🚀 1단계: Vercel로 프론트엔드 배포 (2분)

1. **Vercel 계정 만들기**

   - [vercel.com](https://vercel.com) 접속
   - GitHub로 로그인

2. **프로젝트 배포**
   - "Add New" → "Project" 클릭
   - GitHub에서 `klue-keyboard` 선택
   - Framework: "Create React App" 선택
   - Root Directory: `klue_client` 입력
   - Deploy 클릭!

## 🔧 2단계: Railway로 백엔드 배포 (3분)

1. **Railway 계정**

   - [railway.app](https://railway.app) 접속
   - GitHub로 로그인

2. **Spring Boot 배포**

   - "New Project" → "Deploy from GitHub repo"
   - `klue-keyboard` 선택
   - Root Directory: `klue_sever`

3. **환경변수 설정**
   ```
   DB_HOST=your_database_host
   DB_USER=your_username
   DB_PASSWORD=your_password
   DB_NAME=klue_keyboard
   YOUR_SECRET=your_secret_here
   ```

✅ **완료!** 몇 분 후 두 URL이 생성됩니다!

## 🎯 빠른 테스트

- 프론트엔드: `https://your-app.vercel.app`
- 백엔드 API: `https://your-app.railway.app/api/keyboard-cases`

## 🔧 트러블슈팅

**문제 1**: 빌드 실패

- Node 버전을 18로 설정
- `package.json` 확인

**문제 2**: 데이터베이스 연결 오류

- 환경변수 다시 확인
- DB 접근 권한 확인

---

_💡 팁: 처음에는 무료 플랜으로 충분합니다!_

## 🎉 완료!

이제 다음 URL로 접속 가능:

- **메인 사이트**: `https://your-app.vercel.app`
- **API 서버**: `https://your-app.railway.app`

### 자동 배포 설정됨

- GitHub에 코드 푸시 → 자동으로 사이트 업데이트
- 별도 서버 관리 불필요
- SSL 인증서 자동 적용
