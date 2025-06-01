# 백엔드용 Dockerfile
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 빌드 파일 복사
COPY klue_sever/gradle/ gradle/
COPY klue_sever/gradlew .
COPY klue_sever/gradlew.bat .
COPY klue_sever/build.gradle .
COPY klue_sever/settings.gradle .

# 소스 코드 복사
COPY klue_sever/src/ src/

# 실행 권한 부여
RUN chmod +x gradlew

# 애플리케이션 빌드
RUN ./gradlew build -x test

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "build/libs/klue_sever-0.0.1-SNAPSHOT.jar"] 