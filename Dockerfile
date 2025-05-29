# Java 17 기반 이미지 사용
FROM openjdk:17-jdk-alpine

# 빌드된 JAR 파일 복사
ARG JAR_FILE=build/libs/deepread-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar

# 포트 열기
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
