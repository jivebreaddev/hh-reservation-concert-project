package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultTokenFactoryService implements TokenFactoryUseCase {

  private final TokenRepository tokenRepository;

  public DefaultTokenFactoryService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  public Token createToken(UUID userId, UUID queueId) {
    return tokenRepository.findByUserId(userId)
        .orElseGet(() -> tokenRepository.save(Token.of(userId, queueId)));
  }

}
