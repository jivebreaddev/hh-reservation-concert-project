package kr.hhplus.be.server.config.redis;

import kr.hhplus.be.server.config.redis.RedisConfig.RedisQueueProperties;
import kr.hhplus.be.server.queues.infra.RedisKey;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisInitializer.class);

  private final RedissonClient redissonClient;
  private final RedisQueueProperties redisQueueProperties;

  public RedisInitializer(RedissonClient redissonClient, RedisQueueProperties redisQueueProperties) {
    this.redissonClient = redissonClient;
    this.redisQueueProperties = redisQueueProperties;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initializeRedisKeys() {
    try {
      RAtomicLong queueCounter = redissonClient.getAtomicLong(RedisKey.QUEUE_SIZE.key());

      if (!queueCounter.isExists()) {
        queueCounter.set(redisQueueProperties.getInitialSize());
        LOGGER.info("queue-size to {}", redisQueueProperties.getInitialSize());

      } else {
        LOGGER.info("queue-size already exists: {}", queueCounter.get());
      }

    } catch (Exception e) {
      LOGGER.error("[RedisInit] Failed to initialize 'queue-size': {}", e.getMessage(), e);
    }
  }
}
