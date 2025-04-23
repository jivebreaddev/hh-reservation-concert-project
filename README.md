# hh-reservation-concert-project



<details>
  <summary>1. 요구사항 분석</summary>
<h2>요구사항 정리</h2>
<hr>

- 콘서트 예약 서비스` 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.**
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## Requirements

- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.

## API Specs

1️⃣ **`주요` 유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
*** 대기열 토큰 발급 API
* 대기번호 조회 API**
>

**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
>

3️⃣ **`주요` 좌석 예약 요청 API**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 **5분**간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 한다.
- 누군가에게 점유된 동안에는 해당 좌석은 다른 사용자가 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

<aside>
💡 **KEY POINT**

</aside>

- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.
</details>

<details>
  <summary>2. 유저스토리</summary>
<h2>유저스토리</h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/user-story.png" alt="이미지 설명">

### 주요 유즈 케이스 시나리오

#### 1. 대기열 진입
  - 고객이 조회 및 예약을 위해 대기열에 진입합니다.
  - 진입 가능해질때까지, 현재 대기열 순번을 표기합니다.
#### 2. 콘서트 예약가능한 날짜 조회/ 임시 예약
  - 발급된 토큰으로 조회 및 좌석 예약을 진행합니다.
  - 콘서트 좌석은 임시 예약되어 5분간 예약 불가 됩니다.
#### 3. 좌석 결제/포인트 결제
  - 임시 예약 성공시에 결제를 진행합니다.
  - 결제 성공 시, 좌석 예약을 확정합니다.
  


</details>

<details>
  <summary>3. 플로우 차트</summary>
<h2> 콘서트 예약가능한 날짜 조회/ 임시 예약 플로우 차트</h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/flowChart.png" alt="이미지 설명">

<h2> 좌석 결제/포인트 결제 플로우 차트 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/flowchart2.png" alt="이미지 설명">
</details>

<details>
  <summary>4. 도메인 설계(클래스 다이어그램)</summary>
</details>

<details>
  <summary>5. ERD 설계</summary>
<h2> 결제, 예약, 고객, 좌석, 콘서트, 대기열, 토큰에 대한 스키마 설계 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/erd.png" alt="이미지 설명">
</details>

<details>
  <summary>6. 대기열 시퀀스 다이어그램, 상태 다이어그램</summary>
<h2> 대기열 시퀀스 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/queue.png" alt="이미지 설명">
<h2> 대기열 상태 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/stateQueue.png" alt="이미지 설명">

</details>

<details>
  <summary>7. 조회/예약 시퀀스, 상태 다이어그램</summary>
<h2> 조회 시퀀스 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/booking.png" alt="이미지 설명">
<h2> 조회 상태 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/stateReservation.png" alt="이미지 설명">
<h2> 조회 시퀀스 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/reservation.png" alt="이미지 설명">
<h2> 조회 상태 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/stateSeat.png" alt="이미지 설명">


</details>

<details>
  <summary>8. 충전/결제 시퀀스, 상태 다이어그램</summary>
<h2> 충전/결제 시퀀스 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/payment.png" alt="이미지 설명">
<h2> 충전/결제 상태 다이어그램 </h2>
<hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-base/docs/statePayment.png" alt="이미지 설명">

</details>

<details>
  <summary>9. API SPECS</summary>
    <h2> 대기열 API</h2>
    <hr>
    <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/queue.png" alt="이미지 설명" >
  
  <h2> 예약 접근 가능 API</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationAvailable.png" alt="이미지 설명" >

  <h2> 예약 조회 API</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

  <h2> 마일스톤</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

  <h2> 마일스톤</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

  <h2> 마일스톤</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

  <h2> 마일스톤</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

  <h2> 마일스톤</h2>
    <hr>
      <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/week2-advanced/docs/swagger/reservationView.png" alt="이미지 설명" >

</details>

<details>
  <summary>10. Milestones</summary>
  <h2> 마일스톤</h2>
  <hr>
  <img src="https://raw.githubusercontent.com/jivebreaddev/hh-reservation-concert-project/main/docs/concertgaant.JPG" alt="이미지 설명" >
</details>


<details>
  <summary>11. WIL</summary>

## 1주차 WIL
### 과제 목표
- mocking 과 stubbing 을 통한 unit 테스트 작성법 숙지
- 통합 테스트 작성과 동시성 테스트 작성

#### 1) 실습 내용:
1. 포인트 결제/충전/사용에 대한 API 를 구현하고 단위테스트를 작성했다.
2. 단일 서버에서 일어날 수 있는 동시성 이슈에 대한 이슈를 해결했다.

#### 2) 학습 내용:
1. 테스트 피라미드란 무엇인가?
   - 유닛테스트, 통합테스트, E2E 테스트
2. 의존성을 제거하기 위한 방법은 무엇이 있을까?
   - mocking
   - fake
3. 동시성 테스트에 대한 정책 짜기
   - 입금, 출금이 동시에?
   - 입급이 동시에 2번?
   - 출금이 동시에 2번?
4. 테스트의 도구는 무엇이 있을까?
   - 러너, 어썰션, 모킹, 테스트 훅
5. 좋은 테스트의 기준은 무엇일까?
  - 실패하기 쉬운 테스트
6. 테스트 더블은 무엇일까?
  - mock, stub, fake, spy

#### 3) 회고:
1. 지식을 습득할 때, 왜?라는 질문들을 많이 던지고 범주를 나눠서 정리하면, '일을 잘하기 쉬워지구나'를 멘토님을 보고 느꼈다. 
2. 단일 서버내 멀티스레드의 자원 경합에 대한 이슈를 세가지 시나리오로 해결해봤다. 또, 쓰레드를 병렬로 열어서 테스트하는 법을 이해할 수 있었다.
   - synchronized
   - CAS (atomic), ReentrantLock
   - ConcurrentLinkedQueue, 동시성 자료구조
3. 마지막으로, 내가 뭘 모르는지 정의하고, 문제에 대해 공유하는 방법을 이해할 수 있었다.


## 2주차 WIL

### Swagger 구현 및 도메인 설계 및 설계 세부사항 정리
#### 1) 실습 내용:
1. 유저스토리들을 생성으로 요구사항을 명확히 분석하기 (애매모호한것을 뾰족하게한다.)
   - 유저스토리 
   - 플로우 차트로 유즈 케이스 분석
2. OpenAPI 명세로 협업
3. 도메인 모델링
   - 상태 다이어그램
   - 도메인 간 메시지 식별
   - 행위에 대한 시퀀스 다이어그램
4. ERD 설계

#### 2) 학습 내용:
1. 시퀀스 다이어그램을 언제 사용할지
2. 플로우 차트를 통해 유즈케이스 분석하기
3. 도메인 모델링
   - 도메인 간 메시지 식별
   - 도메인의 상태 다이어그램
3. AGGREGATE ROOT 식별 및 패키지 분리


#### 3) 회고:
1. 추상적인 아이디어를 뾰족하게 표현하는 법
2. Mock API 전달해서, 빠른 커뮤니케이션 진행
3. 어떤 아이디어는 어떤 툴로 표현해야 좋은지 고민할 수 있었음

## 3주차 WIL
### 애플리케이션 아키텍처 구조
#### 1) 실습 내용:
1. DIP를 통해 의존성 방향을 도메인을 향해 설정
2. 유저 시나리오들에 대한 비즈니스 로직 개발 및 단위 테스트 작성

#### 2) 학습 내용:
1. 클린아키텍처가 중요한 이유?
   - 변경의 범위를 최소한으로 만들기 위해
   - DIP, OCP 를 지키기 위해
2. 멘토님의 성과를 공유 받으면서 내가 어떤 부분들을 측정하고, 해결하고 공유하는 법을 선택할지 생각해볼 수 있었다.
   - 해결 방법을 왜하는지?
   - 목표 수치는 무엇인지?
   - 해결하는 방법은 무엇이 있는지?
   - 이해를 높이는 도표는 무엇이 있는지?
3. 외부 연동시 팁들
   - Connection Pool 이 보통 thread 당으로 물려 이슈 생김
   - 비동기로 해결하게 되면, 결제 처리는 정책으로 풀어낸다. 
   - 정책에 따라, 예약과 결제를 하나로 묶는 경우도 있다.
   - 장애시
     - 외부 서비스 에러시, 써킷 건다.
     - REDIS로 대기열 운영하다가 장애지점은 메모리 적재량 초과

#### 3) 회고:
1. DIP를 적용해서, 비즈니스 로직을 구현했다.
   - Interceptor로 token 발급 받은 고객만 예약할 수 있도록 처리
   - Scheduler로 대기열 입장 처리
     - 대기열 입장 처리에 대한 정책 고민 
       - 시간당 n명의 인원 입장
       - 입장 인원들의 상태 처리이후 n명의 인원 입장


## 4주차 WIL

### 통합테스트 작성 및 인덱스를 통한 쿼리 최적화 
#### 1) 실습 내용:

#### 2) 학습 내용:

#### 3) 회고:


## 5주차 WIL

### 통합테스트 작성 및 동시성 테스트 작성
#### 1) 실습 내용:

#### 2) 학습 내용:

#### 3) 회고:


</details>


<details>
  <summary>12. 동시성 개선안</summary>

# 1. 단일 서버내 동시성 이슈
**단일 서버에 대한 동시성 처리**

- 어려운 이유
  1. **일반적으로 동시성으로 인해 생기는 예외는 재현하기 어렵습니다.**
  2. **코드만 보고 동시성 문제의 발생 가능성을 파악하기 정말 어렵습니다.**
  3. **최악의 경우에는 동시성 문제가 발생해도 진짜 결함으로 간주되지 않고 일회성 문제로 여겨 무시될 수 있습니다.**
- 단일 서버에서 활용 방안들
  - jpa thread local

  ### ✅ **결론: 단일 서버 락이 유효한 경우**

  ✔ **요청이 항상 같은 서버로 라우팅된다면** (`Sticky Session`)

  ✔ **싱글톤 프로세스로 실행되는 배치 작업이라면** (`@Scheduled`)

  ✔ **JVM 내에서만 접근하는 캐시, 로컬 데이터라면** (`ReentrantLock`, `ReadWriteLock`)

  ✔ **로컬 파일, 로그 파일 등을 동기화할 때라면** (`FileLock`)

- 해결책
  - atomic class (락프리)
    - **AtomicLong**
    - Compare and A Swapped
      - **기존 값과 예상 값이 일치할 때만 변수의 값을 업데이트하기 때문에, 데이터의 일관성을 보장**
      - **데드락과 같은 문제를 방지하면서도 높은 성능을 유지**
  - 동시성 데이터 구조  + QUEUE
  - LOCK (REENTRANT LOCK)

## 1. 기술적인 문제 정의
 - 단일 서버내에서 멀티 스레드내 공유 자원에 대한 동시성 이슈 해결

## 2. 개선 정의 ()
  - 
### 보상 트랜잭션의 종류

- 사가 패턴 논문에서 실패 처리에 대해서 두가지 복구 유형이 기술 되있다고 합니다.

### A. 역방향 실패

### B. 정방향 실패

### 3. 정합성 검증 절차

### a. 잠재적인 문제: 애플리케이션 로직 문제

### a.1 검증되지 못한 보상 트랜잭션 문제 검증

### a.2 적절한 종료 실패로 상태값이 처리 검증

## 3. as-is, to-be 데이터 증거 측정

## 4. 장점, 단점

| 장점 | 단점                                      |
| --- |-----------------------------------------|
| 정합성에 대한 자신감 | 서비스마다 TEST 와 검증 절차는 변경 비용이 됨            |
| 서비스 안전성 | 검증절차에 대한 서비스마다 다른 구현으로 히스토리 따라가기 힘듬     |
| 복잡한 트랜잭션 구성 가능 | 통합테스트를 구성함으로써, 필요한 구성요소 구성 필요 (테스트 복잡성) |

## 5. 대안


# 2. DB 접근의 동시성 이슈

## 1. 기술적인 문제 정의

## 2. 개선 정의

### 2. 보상 트랜잭션의 종류

- 사가 패턴 논문에서 실패 처리에 대해서 두가지 복구 유형이 기술 되있다고 합니다.

### A. 역방향 실패

### B. 정방향 실패

### 3. 정합성 검증 절차

### a. 잠재적인 문제: 애플리케이션 로직 문제

### a.1 검증되지 못한 보상 트랜잭션 문제 검증

### a.2 적절한 종료 실패로 상태값이 처리 검증

## 3. as-is, to-be 데이터 증거 측정

## 4. 장점, 단점

| 장점 | 단점                                      |
| --- |-----------------------------------------|
| 정합성에 대한 자신감 | 서비스마다 TEST 와 검증 절차는 변경 비용이 됨            |
| 서비스 안전성 | 검증절차에 대한 서비스마다 다른 구현으로 히스토리 따라가기 힘듬     |
| 복잡한 트랜잭션 구성 가능 | 통합테스트를 구성함으로써, 필요한 구성요소 구성 필요 (테스트 복잡성) |

## 5. 대안


# 3. 다중 서버의 동시성 이슈

## 1. 기술적인 문제 정의

## 2. 개선 정의

### 2. 보상 트랜잭션의 종류

- 사가 패턴 논문에서 실패 처리에 대해서 두가지 복구 유형이 기술 되있다고 합니다.

### A. 역방향 실패

### B. 정방향 실패

### 3. 정합성 검증 절차

### a. 잠재적인 문제: 애플리케이션 로직 문제

### a.1 검증되지 못한 보상 트랜잭션 문제 검증

### a.2 적절한 종료 실패로 상태값이 처리 검증

## 3. as-is, to-be 데이터 증거 측정

### AS-IS
#### A. 예약 시나리오

#### B. 잔액 충전, 사용, 결제 시나리오

### TO-BE 
#### A. 예약 시나리오 

#### B. 잔액 충전, 사용, 결제 시나리오

## 4. 장점, 단점

| 장점 | 단점                                      |
| --- |-----------------------------------------|
| 정합성에 대한 자신감 | 서비스마다 TEST 와 검증 절차는 변경 비용이 됨            |
| 서비스 안전성 | 검증절차에 대한 서비스마다 다른 구현으로 히스토리 따라가기 힘듬     |
| 복잡한 트랜잭션 구성 가능 | 통합테스트를 구성함으로써, 필요한 구성요소 구성 필요 (테스트 복잡성) |

## 5. 대안

# 4. 동시성 이슈를 피해가는 메시지 큐 도입

## 1. 기술적인 문제 정의

## 2. 개선 정의

### 2. 보상 트랜잭션의 종류

- 사가 패턴 논문에서 실패 처리에 대해서 두가지 복구 유형이 기술 되있다고 합니다.

### A. 역방향 실패

### B. 정방향 실패

### 3. 정합성 검증 절차

### a. 잠재적인 문제: 애플리케이션 로직 문제

### a.1 검증되지 못한 보상 트랜잭션 문제 검증

### a.2 적절한 종료 실패로 상태값이 처리 검증

## 3. as-is, to-be 데이터 증거 측정

## 4. 장점, 단점

| 장점 | 단점                                      |
| --- |-----------------------------------------|
| 정합성에 대한 자신감 | 서비스마다 TEST 와 검증 절차는 변경 비용이 됨            |
| 서비스 안전성 | 검증절차에 대한 서비스마다 다른 구현으로 히스토리 따라가기 힘듬     |
| 복잡한 트랜잭션 구성 가능 | 통합테스트를 구성함으로써, 필요한 구성요소 구성 필요 (테스트 복잡성) |

## 5. 대안

# 5. 동시성 이슈를 피해가지 못하는 즉각적 일관성이 필요한 경우



</details>


<details>
  <summary>13. 쿼리 개선안</summary>

# 1. 콘서트 API
## 1.  API 이름 

### A. 기술적인 문제 정의 (쿼리) 

### B. 개선 정의 (인덱스 검토)

### C. as-is, to-be 데이터 증거 측정


# 2. 결제/포인트 충전 API

## 1.  API 이름

### A. 기술적인 문제 정의 (쿼리)

### B. 개선 정의 (인덱스 검토)

### C. as-is, to-be 데이터 증거 측정


</details>

<details>
  <summary>14. Redis를 통한 대기열 성능 개선안</summary>

# 1. REDIS 활용한 대기열 성능 개선

## A. 기술적인 문제 정의 (RDB 성능 제한 및 API 성능 기준 미충족)

## B. 개선 정의 (개선 방향 검토)

## C. as-is, to-be 데이터 증거 측정

</details>

<details>
  <summary>15. @Transactional과 외부 요청 분리 개선안</summary>

# 1. REDIS 활용한 대기열 성능 개선

## A. 기술적인 문제 정의 (RDB 성능 제한 및 API 성능 기준 미충족)

## B. 개선 정의 (개선 방향 검토)

## C. as-is, to-be 데이터 증거 측정

</details>

<details>
  <summary>16. 배치로 상태 처리에 대한 개선안</summary>

# 1. REDIS 활용한 대기열 성능 개선

## A. 기술적인 문제 정의 (RDB 성능 제한 및 API 성능 기준 미충족)

## B. 개선 정의 (개선 방향 검토)

## C. as-is, to-be 데이터 증거 측정

</details>
