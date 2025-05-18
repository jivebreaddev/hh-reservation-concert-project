package kr.hhplus.be.server.queues.ui.scheduler;

import kr.hhplus.be.server.common.annotation.Scheduler;
import kr.hhplus.be.server.queues.application.QueueAdmissionUseCase;
import org.springframework.scheduling.annotation.Scheduled;

@Scheduler
public class EnqueueScheduler {
  private final QueueAdmissionUseCase admissionUseCase;

  protected EnqueueScheduler(QueueAdmissionUseCase admissionUseCase) {
    this.admissionUseCase = admissionUseCase;
  }

  @Scheduled(fixedDelay = 3000)
  private void processQueue() {
    admissionUseCase.processQueue();
  }
}
