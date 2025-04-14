package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultTokenFactoryService implements TokenFactoryUseCase {

  private final TokenRepository tokenRepository;

  public DefaultTokenFactoryService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  @Transactional
  public Token createToken(UUID userId, UUID queueId) {
    return tokenRepository.findByUserId(userId)
        .orElseGet(() -> tokenRepository.save(Token.of(userId, queueId)));
  }

  @Override
  public Token getToken(UUID tokenId) {
    return tokenRepository.findById(tokenId)
        .orElseThrow(RuntimeException::new);
  }
}
