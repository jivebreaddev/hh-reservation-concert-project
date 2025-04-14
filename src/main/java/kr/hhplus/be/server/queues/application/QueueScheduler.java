package kr.hhplus.be.server.queues.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class QueueScheduler {

  private final QueueAdmissionUseCase admissionUseCase;
  private final TokenExpirationUseCase tokenExpirationUseCase;

  protected QueueScheduler(QueueAdmissionUseCase admissionUseCase,
      TokenExpirationUseCase tokenExpirationUseCase) {
    this.admissionUseCase = admissionUseCase;
    this.tokenExpirationUseCase = tokenExpirationUseCase;
  }

  @Scheduled(fixedDelay = 3000)
  private void processQueue() {
    admissionUseCase.processQueue();
  }

  @Scheduled(fixedDelay = 3000)
  private void expiredTokens() {
    tokenExpirationUseCase.expireToken();
  }
}
