package kr.hhplus.be.server.util;

import java.util.List;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RedisCleanup {

  private final RedissonClient redissonClient;

  public RedisCleanup(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  public void cleanUp(List<String> keyPatterns) {
    RKeys rKeys = redissonClient.getKeys();

    for (String pattern : keyPatterns) {
      Iterable<String> keys = rKeys.getKeysByPattern(pattern + "*");
      keys.forEach(rKeys::delete);
    }
  }

}
