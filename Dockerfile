# 멀티스테이지 빌드 - 빌드 단계
FROM gradle:8.5-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle wrapper와 설정 파일들을 먼저 복사
COPY klue_sever/gradle/ gradle/
COPY klue_sever/gradlew .
COPY klue_sever/gradlew.bat .
COPY klue_sever/build.gradle .
COPY klue_sever/settings.gradle .

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# 의존성 다운로드 (캐시 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY klue_sever/src/ src/

# Gradle 빌드 (테스트 제외)
RUN ./gradlew clean bootJar -x test --no-daemon

# 런타임 단계 - 경량화된 JRE 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 시스템 패키지 업데이트 및 필요한 도구 설치
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 애플리케이션 실행을 위한 사용자 생성 (보안)
RUN groupadd -r klueapp && useradd -r -g klueapp klueapp

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/klue_sever-0.0.1-SNAPSHOT.jar app.jar

# 소유권 변경
RUN chown klueapp:klueapp app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 사용자로 전환
USER klueapp

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 메모리 최적화된 애플리케이션 실행
CMD ["java", \
     "-Xmx450m", \
     "-Xms128m", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dspring.profiles.active=production", \
     "-jar", "app.jar"] 