package kr.hhplus.be.server.fixture.payments;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.payments.domain.Point;

public class PointFixture {
  public static Point createPoint(UUID userId) {
    return Point.of(userId, 1000L);
  }

  public static Point createPoint(UUID userId, Long balance) {
    return Point.of(userId, balance);
  }

  public static Point createPointWithCharge(UUID userId, Long balanceToAdd) {
    Point point = createPoint(userId);
    point.chargePoint(Money.of(balanceToAdd), LocalDateTime.now());
    return point;
  }

  public static Point createPointWithUse(UUID userId, Long balanceToSubtract) {
    Point point = createPoint(userId);
    point.usePoint(Money.of(balanceToSubtract), LocalDateTime.now());
    return point;
  }
}
