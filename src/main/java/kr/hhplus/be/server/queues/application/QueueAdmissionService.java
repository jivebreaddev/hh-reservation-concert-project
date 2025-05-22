package kr.hhplus.be.server.queues.application;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class QueueAdmissionService implements QueueAdmissionUseCase {

  private final QueueRepository queueRepository;
  private final TokenRepository tokenRepository;

  protected QueueAdmissionService(QueueRepository queueRepository, TokenRepository tokenRepository) {
    this.queueRepository = queueRepository;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public void processQueue() {
    List<UUID> queues = queueRepository.findAllByCreatedAtAsc()
        .stream()
        .map(queue -> queue.getId())
        .toList();
    List<Token> tokens = tokenRepository.findByUserIds(queues);

    queueRepository.toActiveToken(tokens);
  }
}
