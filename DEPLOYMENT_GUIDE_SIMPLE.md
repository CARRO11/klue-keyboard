# 🚀 KLUE 키보드 추천 시스템 배포 가이드

## 📋 배포 전 체크리스트

### ✅ 준비 사항

- [x] GitHub 계정
- [x] Vercel 계정 (프론트엔드용)
- [x] Railway 계정 (백엔드 + DB용)
- [x] 소스코드가 GitHub에 업로드되어 있어야 함

---

## 🎯 1단계: Railway 백엔드 + 데이터베이스 배포

### 1.1 Railway 프로젝트 생성

```bash
# Railway CLI 설치 (선택사항)
npm install -g @railway/cli
railway login
```

### 1.2 Railway 대시보드에서 배포

1. **Railway 대시보드** (https://railway.app) 접속
2. **"New Project"** 클릭
3. **"Deploy from GitHub repo"** 선택
4. **klue_project** 저장소 선택
5. **Root Directory 설정**: `klue_sever` 입력 ❌
   - **주의**: Root Directory를 `.` (점)으로 설정 ✅

### 1.3 MySQL 데이터베이스 추가

1. 프로젝트 내에서 **"+ New"** 클릭
2. **"Database"** → **"Add MySQL"** 선택
3. 자동으로 MySQL 인스턴스가 생성됨

### 1.4 환경 변수 설정

Railway 프로젝트 **Variables** 탭에서 다음 추가:

```env
# Database (자동 생성된 값 사용)
DATABASE_URL=mysql://root:패스워드@호스트:포트/railway
DATABASE_USERNAME=root
DATABASE_PASSWORD=자동생성된패스워드

# CORS 설정 (나중에 Vercel URL로 변경)
FRONTEND_URL=http://localhost:3000

# JVM 설정
JAVA_OPTS=-Xmx450m -Xms128m
```

---

## 🎯 2단계: Vercel 프론트엔드 배포

### 2.1 Vercel 프로젝트 생성

1. **Vercel 대시보드** (https://vercel.com) 접속
2. **"New Project"** 클릭
3. **GitHub에서 klue_project 저장소** 선택

### 2.2 빌드 설정

- **Root Directory**: `klue_client` 입력
- **Build Command**: `npm run build`
- **Output Directory**: `build`

### 2.3 환경 변수 설정

Vercel 프로젝트 **Settings** → **Environment Variables**:

```env
REACT_APP_API_URL=https://여러분의railway프로젝트.up.railway.app
```

---

## 🔄 3단계: 도메인 연결

### 3.1 Railway 환경 변수 업데이트

Vercel 배포 완료 후, Railway **Variables**에서 업데이트:

```env
FRONTEND_URL=https://여러분의vercel프로젝트.vercel.app
```

### 3.2 프론트엔드 API URL 확인

`klue_client/src` 폴더의 API 호출 부분이 올바른 Railway URL을 사용하는지 확인

---

## 🧪 4단계: 배포 테스트

### 4.1 백엔드 Health Check

```bash
curl https://여러분의railway프로젝트.up.railway.app/api/health
```

### 4.2 프론트엔드 접속

- Vercel URL로 접속하여 정상 동작 확인
- API 연결 상태 확인

---

## 🔧 트러블슈팅

### Railway 빌드 실패 시

1. **Dockerfile 경로 확인**: 프로젝트 루트에 `Dockerfile`이 있는지 확인
2. **Java 버전 확인**: `railway.toml`에서 Java 17 설정 확인
3. **메모리 설정**: Railway 무료 플랜은 512MB 제한

### CORS 에러 시

1. Railway **Variables**에서 `FRONTEND_URL` 확인
2. 프론트엔드에서 올바른 백엔드 URL 사용 확인

### 데이터베이스 연결 실패 시

1. Railway MySQL 서비스가 실행 중인지 확인
2. `DATABASE_URL` 환경 변수가 올바른지 확인

---

## 📞 지원

문제가 발생하면 다음을 확인해주세요:

1. Railway 배포 로그
2. Vercel 빌드 로그
3. 브라우저 개발자 도구의 네트워크 탭

**배포 성공!** 🎉
