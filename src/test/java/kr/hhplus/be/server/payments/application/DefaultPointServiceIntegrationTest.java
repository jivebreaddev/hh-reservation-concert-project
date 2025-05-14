package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.config.date.DateTimeFactory;
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
    AtomicInteger chargeSuccessCount = new AtomicInteger();
    AtomicInteger useSuccessCount = new AtomicInteger();

    // 10개의 쓰레드 중 절반은 충전(+1000), 절반은 사용(-500)을 시도함.
    // 실제 실행 순서는 충전 전에 사용 요청이 들어가면 실패할 수도 있음.
    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          int n = counter.getAndIncrement();
          if (n % 2 == 0) {
            pointService.chargePoint(new ChargeRequest(userId, 1000L), UUID.randomUUID());
            chargeSuccessCount.incrementAndGet();

          } else {
            pointService.useUserPoint(new UseRequest(userId, 500L, LocalDateTime.now()));
            useSuccessCount.incrementAndGet();
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    GetBalanceResponse finalBalance = pointService.getUserPoint(new GetBalanceRequest(userId));

    // 예: 포인트가 부족할 경우 실패 이후 롤백 되고, 성공된 케이스들로 잔액을 추정
    assertThat(finalBalance.getBalance()).isEqualTo(2500L);
    long expectedBalance = (1000L * chargeSuccessCount.get()) - (500L * useSuccessCount.get());
    assertThat(finalBalance.getBalance()).isEqualTo(expectedBalance);
  }


//  충전이 먼저 수행되고 사용이 나중에 수행된다면 총 5000 → 7500 → 5000 → 2500으로 내려감.
//
//  그런데 실제 실행 순서는 비결정적이며, 충전 전에 사용 요청이 들어가면 실패할 수도 있음.
//
//  따라서, 현재 구조라면 사용 요청이 실패할 수도 있다
}
