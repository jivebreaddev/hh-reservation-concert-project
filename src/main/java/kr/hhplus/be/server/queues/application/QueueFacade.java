package kr.hhplus.be.server.queues.application;

import kr.hhplus.be.server.common.annotation.Facade;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.Token;
import org.springframework.transaction.annotation.Transactional;

@Facade
public class QueueFacade {

  private final QueueFactoryUseCase queueFactoryUseCase;
  private final TokenFactoryUseCase tokenFactoryUseCase;

  protected QueueFacade(QueueFactoryUseCase queueFactoryUseCase,
      TokenFactoryUseCase tokenFactoryUseCase) {
    this.queueFactoryUseCase = queueFactoryUseCase;
    this.tokenFactoryUseCase = tokenFactoryUseCase;
  }

  @Transactional
  public QueueResponse queueUser(QueueRequest queueRequest) {
    Queue queue = queueFactoryUseCase.createQueue(queueRequest.getUserId());
    Token token = tokenFactoryUseCase.createToken(queueRequest.getUserId(), queue.getId());

    return new QueueResponse(queueRequest.getUserId(), queue.getQueueStatus(), token.getId());
  }

  @Transactional(readOnly = true)
  public EnterResponse getQueueStatus(EnterRequest enterRequest) {
    Queue queue =  queueFactoryUseCase.getQueue(enterRequest.getUserId());

    return new EnterResponse(enterRequest.getUserId(), queue.getQueueStatus());
  }
}
