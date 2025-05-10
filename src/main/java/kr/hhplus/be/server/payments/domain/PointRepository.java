package kr.hhplus.be.server.payments.domain;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface PointRepository {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({
      @QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000")
  })
  Optional<Point> findByUserId(UUID uuid);

  Point save(Point point);
}
