# 🚀 Vercel로 KLUE 키보드 사이트 배포하기

## 1단계: GitHub에 코드 업로드

```bash
# Git 초기화 (아직 안했다면)
git init
git add .
git commit -m "Initial commit"

# GitHub 저장소 생성 후 연결
git remote add origin https://github.com/your-username/klue-keyboard.git
git push -u origin main
```

## 2단계: Vercel 계정 생성 및 연결

1. https://vercel.com 접속
2. GitHub 계정으로 로그인
3. "New Project" 클릭
4. GitHub 저장소 선택
5. 프로젝트 설정:
   - Framework Preset: Create React App
   - Root Directory: klue_client
   - Build Command: npm run build
   - Output Directory: build

## 3단계: 환경 변수 설정

Vercel 대시보드에서:

- Settings → Environment Variables
- API_URL: https://your-backend.herokuapp.com (백엔드 URL)

## 4단계: 자동 배포 완료!

- 코드 푸시할 때마다 자동 배포
- 도메인: https://klue-keyboard.vercel.app
- SSL 인증서 자동 적용
