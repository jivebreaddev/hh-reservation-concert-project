[] 테스트 관련 글쓰기
[] WIL 3주차 아키텍처 관련 글쓰기
[] FK 와 JPA 글쓰기 


초기 설정
[x] JPA 설정
[x] JPA Repository interface 오류 수정
[x] db 초기화 스크립트 생성
[x] testcontainer 설정

[x] jpa 설정 이상해서 entity manager ref 강제로 설정함
[x] jpa repository save 가 저절로 안들어가서 JpaImpl 만들어줌

[x] 이벤트 발행에 대한 테스트 생성 (transactional after commit)
    [x] 수동으로 transaction을 생성하고, transactionTemplate 사용 해 commit 시에 실패 -> debugging에는 찍히는데 조회시 같은 transaction으로 이벤트 발행이후 동일 데이터 조회됨
    [x] 이에 따라, 이벤트 발행에 대한 처리만 테스트 진행
[x] 결제 테스트 
    [x] fake controller 사용하는데 port 번호 달라서 전달 안됨
    [x] Request에 constructor 없어서 오류 
[x] 콘서트 데이터 fixture 생성
[x] 사용자 데이터 fixture 생성
[x] 대기열 데이터 fixture 생성


[x] 콘서트 조회 통합 테스트 시나리오 작성
[x] 콘서트 예약 통합 테스트 시나리오 작성
[x] 결제 통합 테스트 시나리오 작성
[x] 포인트 충전/사용 통합 테스트 시나리오 작성
[x] 대기열 통합 테스트 시나리오 작성

