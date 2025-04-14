package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;

public interface QueueFactoryUseCase {

  Queue createQueue(UUID userId);

  Queue getQueue(UUID userId);
}
