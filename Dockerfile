# 멀티스테이지 빌드 - 빌드 단계
FROM gradle:8.5-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# klue_sever 디렉토리의 내용을 복사
COPY klue_sever/ .

# Gradle 빌드 (테스트 제외)
RUN gradle clean bootJar -x test --no-daemon

# 런타임 단계
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/klue_sever-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"] 