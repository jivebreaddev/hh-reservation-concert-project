package kr.hhplus.be.server.queues.infra;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueuePosition;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.Token;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RMap;
import org.redisson.api.RMapAsync;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSetCacheAsync;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RedisQueueRepository implements QueueRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisQueueRepository.class);

  private final RedissonClient redissonClient;

  public RedisQueueRepository(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Override
  public List<Queue> saveAll(List<Queue> processeingQueues) {
    try {
      // O(M * log(N)) M: 추가하려는 리스트 크기, N: Sorted Set에 이미 저장된 리스트 크기
      RBatch batch = redissonClient.createBatch();
      RMapAsync<String, Queue> queueMap = batch.getMap(RedisKey.QUEUE_MAP.key());

      processeingQueues.forEach(q -> queueMap.putAsync(q.getUserId().toString(), q));

      batch.execute();
      return processeingQueues;
    } catch (Exception e) {

      return Collections.EMPTY_LIST;
    }

  }

  @Override
  public Queue save(Queue processingQueue) {

    try {
      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
      queueMap.put(processingQueue.getId().toString(), processingQueue);
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

      return null;
    }
    return processingQueue;
  }

  @Override
  public List<Queue> findAllByCreatedAtAsc() {
    try {
      // ZRANGEBYSCORE O(log(N) + M)
      RAtomicLong queueCounter = redissonClient.getAtomicLong(RedisKey.QUEUE_SIZE.key());
      Long targetQueueSize = queueCounter.get();
      Long processed = countByQueueStatus();
      Long enqueuedSize = targetQueueSize - processed;

      RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet(
          RedisKey.WAIT_QUEUE.key());
      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());

      return ranking.entryRange(0, enqueuedSize.intValue() - 1)
          .stream()
          .map(rank -> queueMap.get(rank.getValue()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

      return Collections.EMPTY_LIST;
    }
  }

  @Override
  public Long countByQueueStatus() {
    // SCARD O(1)
    try {
      RSet<String> activeTokens = redissonClient.getSet(RedisKey.PROCESS_QUEUE.key());
      return activeTokens.stream().count();
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

      return null;
    }
  }

  @Override
  public void toActiveToken(List<Token> waitingToken) {
    try {
      RBatch batch = redissonClient.createBatch();
      RSetCacheAsync<String> activeTokens = batch.getSetCache(RedisKey.PROCESS_QUEUE.key());

      waitingToken
          .forEach(
              token -> activeTokens.addAsync(String.valueOf(token.getId()), 300, TimeUnit.SECONDS)
          );
      batch.execute();
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

    }

  }

  @Override
  public Optional<QueuePosition> findQueueStatusByUserId(UUID userId) {
    try {
      RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet(
          RedisKey.WAIT_QUEUE.key());
      Integer rank = ranking.rank(String.valueOf(userId));

      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
      Queue userQueue = queueMap.get(userId.toString());

      return Optional.of(QueuePosition.of(userQueue.getQueueStatus(), rank.longValue()));
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Queue> findByUserId(UUID userId) {
    try {
      String id = userId.toString();

      RSet<String> processing = redissonClient.getSet(RedisKey.PROCESS_QUEUE.key());
      if (processing.contains(id)) {
        RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
        Queue queue = queueMap.get(id);
        return Optional.ofNullable(queue);
      }

      RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet(
          RedisKey.WAIT_QUEUE.key());
      Integer rank = ranking.rank(id);
      if (rank == null) {
        return Optional.empty();
      }

      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
      Queue queue = queueMap.get(id);
      return Optional.ofNullable(queue);

    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);
      return Optional.empty();
    }
  }

  @Override
  public List<Queue> findAllByUserId(List<UUID> userIds) {
    try {

      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());

      return userIds.stream()
          .map(id -> queueMap.get(id.toString()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);
      return Collections.EMPTY_LIST;

    }
  }
}
