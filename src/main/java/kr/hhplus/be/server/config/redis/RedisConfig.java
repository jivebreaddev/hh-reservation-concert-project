package kr.hhplus.be.server.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
  @Bean
  public RedissonClient redissonClient(ObjectMapper objectMapper) {
    Config config = new Config();

    config.useSentinelServers()
        .setMasterName("mymaster")
        .addSentinelAddress("redis://localhost:26379")
        .setPassword("mypass");

    // ObjectMapper 연동한 Codec
    config.setCodec(new JsonJacksonCodec(objectMapper));

    return Redisson.create(config);
  }
}
