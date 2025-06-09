#!/bin/bash

# Java 환경 확인
echo "Checking Java environment..."
java -version

# JAR 파일 확인
echo "Checking JAR file..."
ls -la build/libs/

# 애플리케이션 시작
echo "Starting application..."
exec java -jar build/libs/klue_sever-0.0.1-SNAPSHOT.jar 