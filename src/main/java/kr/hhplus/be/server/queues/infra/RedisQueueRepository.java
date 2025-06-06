package kr.hhplus.be.server.queues.infra;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueuePosition;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RMap;
import org.redisson.api.RMapAsync;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RSetCacheAsync;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
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
  public Queue save(Queue waitingQueue) {

    try {
      // 1. 큐 맵에 저장
      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
      queueMap.put(waitingQueue.getUserId().toString(), waitingQueue);

      // 2. 대기열(RScoredSortedSet)에 추가
      RScoredSortedSet<String> waitQueue = redissonClient.getScoredSortedSet(
          RedisKey.WAIT_QUEUE.key());
      waitQueue.add(System.currentTimeMillis(), waitingQueue.getUserId().toString());


    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

      return null;
    }
    return waitingQueue;
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

      Set<String> keys = ranking.entryRange(0, enqueuedSize.intValue() - 1)
          .stream()
          .map(ScoredEntry::getValue)
          .collect(Collectors.toSet());

      Map<String, Queue> queueMapEntries = queueMap.getAll(keys);

      return queueMapEntries.values()
          .stream()
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
      RSetCache<String> activeTokens = redissonClient.getSetCache(RedisKey.PROCESS_QUEUE.key());
      return activeTokens.stream().count();
    } catch (Exception e) {
      LOGGER.error("Redis 조회 중 오류 발생", e);

      return null;
    }
  }

  @Override
  public void toActiveToken(List<Queue> waitingUsers) {
    try {
      RBatch batch = redissonClient.createBatch();
      RSetCacheAsync<String> activeTokens = batch.getSetCache(RedisKey.PROCESS_QUEUE.key());

      waitingUsers
          .forEach(
              user -> activeTokens.addAsync(String.valueOf(user.getUserId()), 300, TimeUnit.SECONDS)
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

      if (userQueue == null) {
        return Optional.empty();
      }
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

  @Override
  public void removeFromWaitQueue(Set<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return;
    }
    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet(RedisKey.WAIT_QUEUE.key());
    System.out.println(keys.toString());
    for (String key : keys) {
      System.out.println(key);
    }

    ranking.removeAll(keys);
  }

  @Override
  public void removeFromQueue(Set<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return;
    }

    try {
      RMap<String, Queue> queueMap = redissonClient.getMap(RedisKey.QUEUE_MAP.key());
      queueMap.fastRemove(keys.toArray(new String[0]));  // 성능상 fastRemove 사용 권장
    } catch (Exception e) {
      LOGGER.error("QUEUE_MAP에서 키 제거 중 오류 발생", e);
    }
  }
}
