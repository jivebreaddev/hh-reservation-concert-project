package kr.hhplus.be.server.queues.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class QueueScheduler {

  private final QueueAdmissionUseCase admissionUseCase;

  protected QueueScheduler(QueueAdmissionUseCase admissionUseCase) {
    this.admissionUseCase = admissionUseCase;
  }

  @Scheduled(fixedDelay = 3000)
  private void processQueue() {
    admissionUseCase.processQueue();
  }

}
