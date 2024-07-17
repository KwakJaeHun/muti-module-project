# 선착순 구매

## 개요
- Redis를 활용한 캐싱 전략, 대규모 주문 처리 기술
  - API-GATEWAY를 활용하여 JWT 검증 및 Load Balancing 진행


- 모노리스 서비스의 마이크로 서비스화
  - 각 서비스별 Micro-service룰 구축하여 특정 서비스에 트래픽이 집중 될 경우 확장 가능 고려


- Docker를 활용한 로컬 개발 환경 구축
 

## Skills

- Framework : Spring Boot
- Infrastructure & Databases : Docker, MySQL (JPA), Redis, AWS
- Tools : Git, GitHub, Jenkins

<hr>

### Eureka Server
- 기술: Netflix Eureka
- 목적: 서비스 등록 및 발견을 관리하여 마이크로서비스 간 통신
   
<hr>

### API GATEWAY
- Spring Cloud Gateway
   - 클라이언트 요청을 적절한 서비스로 라우팅하는 단일 진입점 역할
   - Load Balancing 통해 트래픽을 분산
  

- Spring Security
   - WebFlux 기반 JWT 검증

<hr>

### User Service
- Spring Security
    - JWT accessToken, RefreshToken 발급


- Mail
  - Mail Sender를 활용한 회원가입 이메일 인증 로직 구현
  - Async로 비동기 발송


- Feign
  - Rest API 호출을 위한 Interface 작성
  - AOP : Header Token Setting 자동화
  - myPage : Cart, Wish, OrderList


- Redis
  - BlackList RefreshToken 등록을 통한 모든 기기 로그아웃 구현
  - User Info Caching


<hr>

### Product Service

- Redis
    - 상품 재고 관리
    - Redisson 분산락을 활용한 동시성 제어


- Scheduler
  - Redis로 관리되는 상품 재고 DB 동기화 스케줄러 구현


<hr>

### Order Service

- Feign
  - Rest API 호출을 위한 Interface 작성
  - AOP : Header Token Setting 자동화
  - UserInfo, ProductInfo 

<hr>

## Trouble Shooting

1.
