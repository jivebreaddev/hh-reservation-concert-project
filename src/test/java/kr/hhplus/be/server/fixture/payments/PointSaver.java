package kr.hhplus.be.server.fixture.payments;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PointSaver {
  private final PointRepository pointRepository;

  @Autowired
  public PointSaver(PointRepository pointRepository) {
    this.pointRepository = pointRepository;
  }

  @Transactional
  public Point savePoint(Point point) {
    return pointRepository.save(point);
  }

  @Transactional
  public Point saveDefaultPoint(UUID userId) {
    return pointRepository.save(PointFixture.createPoint(userId));
  }
}
