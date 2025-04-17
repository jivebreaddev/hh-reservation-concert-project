package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.concerts.application.DefaultConcertQueryService;
import kr.hhplus.be.server.config.date.DateTimeFactory;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.fixture.payments.PointFixture;
import kr.hhplus.be.server.fixture.payments.PointSaver;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
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
}
