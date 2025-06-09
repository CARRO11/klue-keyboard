# 🚀 Railway 배포 가이드

## 1. Railway 프로젝트 생성

1. **https://railway.app** 접속
2. **"New Project"** → **"Deploy from GitHub repo"**
3. **"klue-keyboard"** 저장소 선택

## 2. MySQL 데이터베이스 추가 (먼저 해야 함!)

1. Railway 프로젝트에서 **"+"** 클릭
2. **"Database"** → **"Add MySQL"** 선택
3. MySQL 서비스 생성 대기 (1-2분)

## 3. 프로젝트 설정

### Root Directory 설정

- **Settings** → **Source** → **Root Directory**: 비워둠 (Docker 사용)

### Build 설정

- **Settings** → **Build** → **Builder**: Dockerfile

## 4. 환경 변수 설정 (중요!)

**Variables** 탭에서 다음 변수들 추가:

```bash
# 데이터베이스 연결 (MySQL 서비스 추가 후 자동 생성됨)
SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}

# 만약 위 변수가 작동하지 않으면 다음 시도:
# SPRING_DATASOURCE_URL=${{Mysql.DATABASE_URL}}
# SPRING_DATASOURCE_USERNAME=${{Mysql.MYSQL_USER}}
# SPRING_DATASOURCE_PASSWORD=${{Mysql.MYSQL_PASSWORD}}

# Spring Boot 설정
SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
PORT=8080

# Java 설정
JAVA_VERSION=17
```

## 5. 배포 실행

- **Deploy** 버튼 클릭 또는 자동 배포 기다리기

## 6. 데이터베이스 연결 확인

배포 후 다음 URL에서 헬스체크 확인:

- `https://your-app.railway.app/actuator/health`

## 트러블슈팅

### 문제 1: MySQL 연결 실패

1. MySQL 서비스가 추가되어 있는지 확인
2. 환경 변수 이름이 정확한지 확인 (${{MySQL.DATABASE_URL}} vs ${{Mysql.DATABASE_URL}})
3. MySQL 서비스가 "Running" 상태인지 확인

### 문제 2: 환경 변수 인식 안됨

- Railway 대시보드에서 MySQL 서비스 클릭 → "Variables" 탭에서 실제 변수 이름 확인
- 정확한 변수 이름을 백엔드 서비스 환경 변수에 복사

### 문제 3: 테이블 생성 실패

- `SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop`으로 설정하여 자동 테이블 생성
