package kr.hhplus.be.server.queues.infra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RedisTokenRepository implements TokenRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisQueueRepository.class);
  private final RedissonClient redissonClient;

  public RedisTokenRepository(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Override
  public Token save(Token token) {
    try {
      String key = RedisKey.TOKEN_MAP.withSuffix(token.getId().toString());
      RBucket<Token> bucket = redissonClient.getBucket(key);
      bucket.set(token);
      return token;
    } catch (Exception e) {
      LOGGER.error("Redis save error for token: {}", token, e);
      throw e;
    }
  }

  @Override
  public Optional<Token> findById(UUID tokenId) {
    try {
      RMap<String, Token> tokenMap = redissonClient.getMap(RedisKey.TOKEN_MAP.key());
      return Optional.ofNullable(tokenMap.get(tokenId.toString()));
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Token> findByUserId(UUID userId) {
    try {
      String key = RedisKey.TOKEN_MAP.key() + ":" + userId.toString();
      RBucket<Token> bucket = redissonClient.getBucket(key);
      return Optional.ofNullable(bucket.get());
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);
      return Optional.empty();
    }
  }

  @Override
  public List<Token> findByUserIds(List<UUID> userIds) {
    List<Token> tokens = new ArrayList<>();
    try {
      for (UUID userId : userIds) {
        String key = RedisKey.TOKEN_MAP.withSuffix(userId.toString());
        try {
          RBucket<Token> bucket = redissonClient.getBucket(key);
          Token token = bucket.get();
          if (token != null) {
            tokens.add(token);
          }
        } catch (Exception ex) {
          // 개별 키 조회 실패 시 DLQ에 userId 적재
          String DlqKey = RedisKey.DLQ.withSuffix(userId.toString());
          RBucket<String> bucket = redissonClient.getBucket(DlqKey);
          bucket.set(userId.toString());
          LOGGER.error("토큰 조회 실패 - userId: {}. DLQ에 적재", userId, ex);
        }
      }
    } catch (Exception ex) {
      // Redis 연결 실패 등 치명적 예외 처리
      LOGGER.error("Redis 연결 실패", ex);
      return Collections.emptyList();
    }

    return tokens;
  }

}
