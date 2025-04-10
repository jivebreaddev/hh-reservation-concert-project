package kr.hhplus.be.server.payments.domain;

import java.util.Optional;
import java.util.UUID;

public interface PointRepository {
  Optional<Point> findByUserId(UUID uuid);

  Point save(Point point);
}
