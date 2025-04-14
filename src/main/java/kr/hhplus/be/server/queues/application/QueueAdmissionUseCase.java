package kr.hhplus.be.server.queues.application;

import org.springframework.transaction.annotation.Transactional;

public interface QueueAdmissionUseCase {

  @Transactional
  void processQueue();
}
