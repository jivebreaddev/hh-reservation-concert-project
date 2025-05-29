package kr.hhplus.be.server.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RedisConfig {
  @Bean
  public RedissonClient redissonClient(ObjectMapper objectMapper) {
    Config config = new Config();

    config.useSentinelServers()
        .setMasterName("mymaster")
        .addSentinelAddress("redis://localhost:26379")
        .setPassword("mypass")
        .setCheckSentinelsList(false);

    config.setCodec(new JsonJacksonCodec(objectMapper));

    return Redisson.create(config);
  }

  @Component
  @ConfigurationProperties(prefix = "redis.queue")
  public class RedisQueueProperties {
    private long initialSize;

    public long getInitialSize() {
      return initialSize;
    }

    public void setInitialSize(long initialSize) {
      this.initialSize = initialSize;
    }
  }
}
