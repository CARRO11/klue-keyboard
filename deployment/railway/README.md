# 🚀 Railway 배포 가이드

## 1. Railway 프로젝트 생성

1. **https://railway.app** 접속
2. **"New Project"** → **"Deploy from GitHub repo"**
3. **"klue-keyboard"** 저장소 선택

## 2. 프로젝트 설정

### Root Directory 설정 (중요!)

- **Settings** → **Source** → **Root Directory**: `klue_sever`

### Build 설정

- **Settings** → **Build**
- **Build Command**: `./gradlew clean bootJar -x test --no-daemon`
- **Start Command**: `java -jar build/libs/klue_sever-0.0.1-SNAPSHOT.jar`

## 3. nixpacks.toml 배치

다음 파일을 `klue_sever/nixpacks.toml`로 복사:

```
deployment/railway/nixpacks.toml → klue_sever/nixpacks.toml
```

## 4. 환경 변수 설정

**Variables** 탭에서 다음 변수들 추가:

```bash
# Java 환경
JAVA_VERSION=17
NIXPACKS_JDK_VERSION=17

# 데이터베이스 (MySQL 서비스 추가 후)
SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}

# Spring Boot 설정
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
PORT=8080

# Java 최적화
JAVA_TOOL_OPTIONS=-Xmx512m
GRADLE_OPTS=-Dorg.gradle.daemon=false
```

## 5. MySQL 데이터베이스 추가

1. Railway 프로젝트에서 **"+"** 클릭
2. **"Database"** → **"Add MySQL"** 선택

## 6. 배포 실행

- **Deploy** 버튼 클릭 또는 자동 배포 기다리기

## 트러블슈팅

### 문제 1: Java 환경 에러

- `JAVA_VERSION=17` 환경 변수 확인
- `nixpacks.toml` 파일이 `klue_sever/` 디렉토리에 있는지 확인

### 문제 2: 빌드 실패

- Root Directory가 `klue_sever`로 설정되어 있는지 확인
- 빌드 로그에서 구체적인 에러 메시지 확인

### 문제 3: 데이터베이스 연결 실패

- MySQL 서비스가 추가되어 있는지 확인
- 환경 변수에서 `${{MySQL.DATABASE_URL}}` 형식 확인
