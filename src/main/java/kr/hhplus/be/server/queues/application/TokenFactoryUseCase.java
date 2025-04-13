package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;

public interface TokenFactoryUseCase {

  Token createToken(UUID userId, UUID queueId);

  Token getToken(UUID tokenId);

}
