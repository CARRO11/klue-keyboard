# 🚀 KLUE 프로젝트 배포 가이드

## 📋 배포 준비 완료 상태

모든 설정 파일이 준비되어 있으니 언제든지 배포하실 수 있습니다!

## 🛠️ **1단계: Railway 백엔드 배포**

### CLI 설치 및 로그인

```bash
# Railway CLI 설치
npm install -g @railway/cli

# 로그인 (브라우저 열림)
railway login
```

### 프로젝트 생성 및 배포

```bash
# 새 Railway 프로젝트 생성
railway init

# 환경 변수 설정 (중요!)
railway variables set OPENAI_API_KEY=sk-proj-실제키입력
railway variables set DB_HOST=localhost
railway variables set DB_USER=root
railway variables set DB_PASSWORD=shin
railway variables set DB_NAME=klue_keyboard

# MySQL 데이터베이스 추가
railway add mysql

# 배포
railway up
```

### Railway에서 확인할 것:

- ✅ 백엔드 서비스 생성됨
- ✅ MySQL 데이터베이스 추가됨
- ✅ 환경 변수 설정됨
- ✅ 공개 URL 생성됨 (예: `https://abc-def.up.railway.app`)

## 🌐 **2단계: Vercel 프론트엔드 배포**

### CLI 설치 및 로그인

```bash
# Vercel CLI 설치
npm install -g vercel

# 로그인
vercel login
```

### 프론트엔드 배포

```bash
# klue_client 폴더로 이동
cd klue_client

# 배포
vercel --prod

# 환경 변수 설정 (Vercel 대시보드에서 또는 CLI로)
vercel env add REACT_APP_API_URL production
# 값: https://당신의railway백엔드.up.railway.app
```

### Vercel에서 확인할 것:

- ✅ 프론트엔드 배포됨
- ✅ 환경 변수 설정됨
- ✅ 공개 URL 생성됨 (예: `https://klue-keyboard.vercel.app`)

## 🔧 **3단계: 데이터베이스 설정**

Railway MySQL에 테이블과 데이터를 설정해야 합니다:

```bash
# Railway MySQL에 접속
railway connect mysql

# 또는 MySQL 클라이언트로 직접 접속
mysql -h containers-us-west-xxx.railway.app -u root -p

# 테이블 생성
source migrate_to_mysql.sql;
source update_missing_data.sql;
source update_exact_links.sql;
```

## 🎯 **배포 후 테스트**

### 1. 백엔드 API 테스트

```bash
curl https://당신의railway주소.up.railway.app/api/health
```

### 2. 프론트엔드 접속

```
https://당신의vercel주소.vercel.app
```

### 3. AI 기능 테스트

- `/ai` 페이지에서 Tony에게 키보드 추천 요청
- "조용한 사무용 키보드" 등으로 테스트

## 💰 **비용 안내**

### 🆓 무료 티어 한도:

- **Railway**: 월 $5 크레딧 (충분함)
- **Vercel**: 월 100GB 대역폭 (충분함)
- **OpenAI**: 유료 (사용량에 따라)

### 💡 절약 팁:

- Railway에서 사용하지 않을 때 서비스 일시정지
- OpenAI API 사용량 모니터링

## 🚨 **환경 변수 체크리스트**

### Railway (백엔드)

- [ ] `OPENAI_API_KEY` - 실제 OpenAI 키
- [ ] `DB_HOST` - Railway MySQL 호스트
- [ ] `DB_USER` - Railway MySQL 사용자
- [ ] `DB_PASSWORD` - Railway MySQL 비밀번호
- [ ] `DB_NAME` - 데이터베이스 이름

### Vercel (프론트엔드)

- [ ] `REACT_APP_API_URL` - Railway 백엔드 URL

## 📞 **문제 해결**

### "500 Internal Server Error"

- Railway 환경 변수 확인
- 데이터베이스 연결 상태 확인
- Railway 로그 확인: `railway logs`

### "AI가 응답하지 않음"

- OpenAI API 키 유효성 확인
- API 키 잔액 확인
- Railway 환경 변수에 키가 올바르게 설정되었는지 확인

### "CORS 에러"

- 백엔드에서 프론트엔드 도메인을 허용하도록 설정
- `@CrossOrigin` 애노테이션 확인

---

## 🎉 **배포 완료 후**

배포가 완료되면:

1. **프론트엔드**: `https://klue-keyboard.vercel.app`
2. **백엔드**: `https://klue-keyboard.up.railway.app`
3. **GitHub**: `https://github.com/CARRO11/klue-keyboard`

### 🔗 링크 공유:

"🍷 키보드 소믈리에 Tony와 함께 완벽한 키보드를 찾아보세요!"

---

**준비 완료! 언제든지 `railway up`과 `vercel --prod`로 배포하실 수 있습니다! 🚀**
