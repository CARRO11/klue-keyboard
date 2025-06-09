# 더 안정적인 Spring Boot Dockerfile
FROM gradle:7.6-jdk17 AS build

WORKDIR /app

# Gradle 파일들 복사
COPY build.gradle .
COPY settings.gradle .
COPY gradle/ gradle/
COPY gradlew .
COPY gradlew.bat .

# 소스 코드 복사
COPY src/ src/

# 권한 설정 및 빌드
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# 런타임 이미지
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/klue_sever-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"] 