package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Component;

@Component
public class QueueAccessVerifier {
  private final TokenRepository tokenRepository;

  public QueueAccessVerifier(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public boolean canAccess(String userId) {
    tokenRepository.findByUserId(UUID.fromString(userId))
        .orElseThrow(() -> new RuntimeException());
    return true;
  }
}
