# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR  /app

# JAR 파일을 이미지로 복사
COPY build/libs/order-service.jar order-service.jar

# 컨테이너 시작 시 실행할 명령
ENTRYPOINT ["java","-jar","order-service.jar"]