package kr.hhplus.be.server.queues.application;

import kr.hhplus.be.server.common.annotation.Facade;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueuePosition;
import kr.hhplus.be.server.queues.domain.Token;

@Facade
public class QueueFacade {

  private final QueueFactoryUseCase queueFactoryUseCase;
  private final TokenFactoryUseCase tokenFactoryUseCase;

  protected QueueFacade(QueueFactoryUseCase queueFactoryUseCase,
      TokenFactoryUseCase tokenFactoryUseCase) {
    this.queueFactoryUseCase = queueFactoryUseCase;
    this.tokenFactoryUseCase = tokenFactoryUseCase;
  }

  public QueueResponse queueUser(QueueRequest queueRequest) {
    Queue queue = queueFactoryUseCase.createQueue(queueRequest.getUserId());
    Token token = tokenFactoryUseCase.createToken(queueRequest.getUserId(), queue.getId());

    return new QueueResponse(queueRequest.getUserId(), token.getId());
  }

  public EnterResponse getQueueStatus(EnterRequest enterRequest) {
    QueuePosition queuePosition =  queueFactoryUseCase.getQueueStatus(enterRequest.getUserId());

    return new EnterResponse(enterRequest.getUserId(), queuePosition.getQueueStatus(), queuePosition.getOrder());
  }
}
