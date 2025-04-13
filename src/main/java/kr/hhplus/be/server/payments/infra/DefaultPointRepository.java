package kr.hhplus.be.server.payments.infra;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultPointRepository extends PointRepository, JpaRepository<Point, UUID> {


}
