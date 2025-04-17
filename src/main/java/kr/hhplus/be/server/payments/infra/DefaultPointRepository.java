package kr.hhplus.be.server.payments.infra;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultPointRepository extends PointRepository, JpaRepository<Point, UUID> {


}
