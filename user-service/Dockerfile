# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# curl 패키지 설치
RUN apk add --no-cache curl

# 작업 디렉토리 설정
WORKDIR  /app

# JAR 파일을 이미지로 복사
COPY build/libs/user-service.jar user-service.jar

# 컨테이너 시작 시 실행할 명령
ENTRYPOINT ["java","-jar","user-service.jar"]