package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueuePosition;

public interface QueueFactoryUseCase {

  Queue createQueue(UUID userId);

  QueuePosition getQueueStatus(UUID userId);
}
