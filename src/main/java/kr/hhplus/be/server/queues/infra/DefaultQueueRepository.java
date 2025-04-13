package kr.hhplus.be.server.queues.infra;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultQueueRepository extends QueueRepository, JpaRepository<UUID, Queue> {

}
