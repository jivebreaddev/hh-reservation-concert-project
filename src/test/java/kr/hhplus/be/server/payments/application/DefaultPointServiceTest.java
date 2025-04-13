package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.config.date.DateTimeFactory;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.application.dto.UseRequest;
import kr.hhplus.be.server.payments.application.dto.UseResponse;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultPointServiceTest {

  private static final Long ZERO_BALANCE = 0L;
  @Mock
  private PointRepository pointRepository;
  @Mock
  private DateTimeFactory dateTimeFactory;

  @InjectMocks
  private DefaultPointService defaultPointService;

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 회원의_포인트_값을_조회_하는_경우 {

    @Test
    @DisplayName("[성공] 조회한 회원의 포인트 값을 반환한다.")
    void getUserPoint() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      Long balance = 1000L;

      // Mock
      when(pointRepository.findByUserId(userId))
          .thenReturn(Optional.of(Point.of(userId, balance)));

      // WHEN
      GetBalanceResponse userPoint = defaultPointService
          .getUserPoint(new GetBalanceRequest(userId));

      // THEN
      assertThat(userPoint.getUserId()).isEqualTo(userId);
      assertThat(userPoint.getBalance()).isEqualTo(balance);
    }

    @Test
    @DisplayName("[실패] ID가 빈값일 경우 예외를 표기한다.")
    void getUserPointWithEmptyInput() {
      // GIVEN
      UUID userId = null;

      // Mock
      when(pointRepository.findByUserId(userId))
          .thenReturn(Optional.ofNullable(null));

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> defaultPointService.getUserPoint(new GetBalanceRequest(userId)));
    }

  }

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 특정_유저의_포인트를_충전하는_기능 {

    @ParameterizedTest
    @CsvSource(value = {"0", "1350", "1450", "10000", "1550"})
    @DisplayName("[성공] 특정 유저의 최소/최대, 다양한 포인트를 충전하고 포인트 잔액을 반환한다.")
    void chargeUserPoint(Long point) {
      // GIVEN
      UUID userId = UUID.randomUUID();
      Point userPoint = Point.of(userId, ZERO_BALANCE);
      Point updatedUserPoint = Point.of(userId, point);
      UUID paymentId = UUID.randomUUID();
      LocalDateTime now = LocalDateTime.now();
      // Mock
      // 의존성이 있는 모든 빈들에 대한 행동은 MOCKING 처리
      when(pointRepository.findByUserId(userId))
          .thenReturn(Optional.of(userPoint));
      when(pointRepository.save(userPoint))
          .thenReturn(updatedUserPoint);
      when(dateTimeFactory.getCurrentTime())
          .thenReturn(now);

      // WHEN
      ChargeResponse result = defaultPointService.chargePoint(new ChargeRequest(userId, point),
          paymentId);

      // THEN
      assertThat(result.getBalance()).isEqualTo(updatedUserPoint.getBalance());
      assertThat(result.getPaymentId()).isEqualTo(paymentId);
    }

    @Test
    @DisplayName("[실패] 충전량이 음수일때, 오류를 표기한다.")
    void chargeUserPointWithNegativeInput() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      Point userPoint = Point.of(userId, ZERO_BALANCE);

      when(pointRepository.findByUserId(userId))
          .thenReturn(Optional.of(userPoint));

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> defaultPointService.chargePoint(new ChargeRequest(userId, -1000L),
              UUID.randomUUID()));
    }

    @Test
    @DisplayName("[실패] ID가 빈값일 경우 예외를 표기한다.")
    void chargeUserPointWithEmptyInput() {
      // GIVEN
      UUID userId = null;

      when(pointRepository.findByUserId(userId))
          .thenReturn(Optional.ofNullable(null));

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> defaultPointService.chargePoint(new ChargeRequest(userId, -1000L),
              UUID.randomUUID()));
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 특정_유저의_포인트를_사용_하는_경우 {

      @ParameterizedTest
      @CsvSource(value = {"10000,1000", "5000,3000", "3000,3000", "10000,10000", "0,0"})
      @DisplayName("[성공] 특정 유저의 최소/최대, 다양한 포인트를 사용하고 포인트 잔액을 반환한다.")
      void useUserPointsByBorder(Long balance, Long used) {
        // GIVEN
        UUID userId = UUID.randomUUID();
        Point userPoint = Point.of(userId, balance);
        Point updatedUserPoint = Point.of(userId, balance - used);
        UUID paymentId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        // Mock
        // 의존성이 있는 모든 빈들에 대한 행동은 MOCKING 처리
        when(pointRepository.findByUserId(userId))
            .thenReturn(Optional.of(userPoint));
        when(pointRepository.save(userPoint))
            .thenReturn(updatedUserPoint);
        when(dateTimeFactory.getCurrentTime())
            .thenReturn(now);

        // WHEN
        UseResponse result = defaultPointService.useUserPoint(
            new UseRequest(userId, used, LocalDateTime.now()));

        // THEN
        assertThat(result.getAmount()).isEqualTo(updatedUserPoint.getBalance());

      }

      @Test
      @DisplayName("[실패] 사용량이 음수일때, 오류를 표기한다.")
      void useUserPointWithNegativeInput() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        Point userPoint = Point.of(userId, ZERO_BALANCE);

        when(pointRepository.findByUserId(userId))
            .thenReturn(Optional.of(userPoint));

        // THEN
        assertThrows(RuntimeException.class,
            () -> defaultPointService.useUserPoint(
                new UseRequest(userId, -1000L, LocalDateTime.now())
            ));
      }


      @Test
      @DisplayName("[실패] ID가 빈값일 경우 예외를 표기한다.")
      void useUserPointWithEmptyInput() {
        // GIVEN
        UUID userId = null;

        when(pointRepository.findByUserId(userId))
            .thenReturn(Optional.ofNullable(null));

        // THEN
        assertThrows(RuntimeException.class,
            () -> defaultPointService.useUserPoint(
                new UseRequest(userId, -1000L, LocalDateTime.now())
            ));
      }

    }
  }
}
