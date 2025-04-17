초기 설정
[x] JPA 설정
[x] JPA Repository interface 오류 수정
[x] db 초기화 스크립트 생성
[x] testcontainer 설정

[x] 이벤트 발행에 대한 테스트 생성 (transactional after commit)
    [x] 수동으로 transaction을 생성하고, transactionTemplate 사용 해 commit 시에 실패 -> debugging에는 찍히는데 조회시 같은 transaction으로 이벤트 발행이후 동일 데이터 조회됨
    [x] 이에 따라, 이벤트 발행에 대한 처리만 테스트 진행
[] 콘서트 데이터 fixture 생성
[] 사용자 데이터 fixture 생성
[] 대기열 데이터 fixture 생성


[] 콘서트 조회 통합 테스트 시나리오 작성
[] 콘서트 예약 통합 테스트 시나리오 작성
[] 결제 통합 테스트 시나리오 작성
[] 포인트 충전/사용 통합 테스트 시나리오 작성
[] 대기열 통합 테스트 시나리오 작성

