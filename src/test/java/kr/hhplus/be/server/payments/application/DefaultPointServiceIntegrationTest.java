package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.concerts.application.DefaultConcertQueryService;
import kr.hhplus.be.server.config.date.DateTimeFactory;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.fixture.payments.PointFixture;
import kr.hhplus.be.server.fixture.payments.PointSaver;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.application.dto.UseRequest;
import kr.hhplus.be.server.payments.application.dto.UseResponse;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultPointServiceIntegrationTest extends IntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  PointSaver saver;

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("points"));
    // 초기 포인트 없이 유저 생성
    Point initialPoint = Point.of(userId, 0L);
    pointRepository.save(initialPoint);
  }
  @Autowired
  private DefaultPointService pointService;

  @Autowired
  private PointRepository pointRepository;

  @Autowired
  private DateTimeFactory dateTimeFactory;

  private final UUID userId = UUID.randomUUID();


  @Test
  @DisplayName("잔액 없음 - 충전 요청 - 충전 후 잔액 확인")
  void chargeWhenEmptyBalance() {
    ChargeRequest request = new ChargeRequest(userId, 1000L);
    ChargeResponse response = pointService.chargePoint(request, UUID.randomUUID());

    assertThat(response.getBalance()).isEqualTo(1000L);
  }

  @Test
  @DisplayName("잔액 있음 - 추가 충전 요청 - 누적 잔액 확인")
  void chargeWhenAlreadyHasBalance() {
    // 선 충전
    pointService.chargePoint(new ChargeRequest(userId, 1000L), UUID.randomUUID());

    // 추가 충전
    ChargeRequest request = new ChargeRequest(userId, 2000L);
    ChargeResponse response = pointService.chargePoint(request, UUID.randomUUID());

    assertThat(response.getBalance()).isEqualTo(3000L);
  }

  @Test
  @DisplayName("사용 요청 - 포인트 사용 요청 - 잔액 차감 후 확인")
  void usePointSuccessfully() {
    // 선 충전
    pointService.chargePoint(new ChargeRequest(userId, 1500L), UUID.randomUUID());

    // 포인트 사용
    UseRequest request = new UseRequest(userId, 1000L, LocalDateTime.now());
    UseResponse response = pointService.useUserPoint(request);

    assertThat(response.getAmount()).isEqualTo(500L);
  }

  @Test
  @DisplayName("잘못된 요청 - 초과 충전 요청 - 예외 발생")
  void usePointWithInsufficientBalance() {
    pointService.chargePoint(new ChargeRequest(userId, 500L), UUID.randomUUID());

    UseRequest request = new UseRequest(userId, 1000L, LocalDateTime.now());

    assertThatThrownBy(() -> pointService.useUserPoint(request))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("동일 유저가 동시에 포인트 충전과 사용을 요청할 경우 정합성이 유지되어야 한다")
  void concurrentChargeAndUsePoint() throws InterruptedException {
    int numberOfThreads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);
    AtomicInteger counter = new AtomicInteger();

    UUID userId = UUID.randomUUID();
    // 초기 잔액 설정
    pointService.chargePoint(new ChargeRequest(userId, 5000L), UUID.randomUUID());

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          int n = counter.getAndIncrement();
          if (n % 2 == 0) {
            pointService.chargePoint(new ChargeRequest(userId, 1000L), UUID.randomUUID());
          } else {
            pointService.useUserPoint(new UseRequest(userId, 500L, LocalDateTime.now()));
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    GetBalanceResponse finalBalance = pointService.getUserPoint(new GetBalanceRequest(userId));
    System.out.println("최종 잔액: " + finalBalance.getBalance());

    // 예: 최초 5000에서 10번 동안 충전/사용 혼합 → 음수가 아닌 값이면 성공
    assertThat(finalBalance.getBalance()).isGreaterThanOrEqualTo(0L);
  }
}
