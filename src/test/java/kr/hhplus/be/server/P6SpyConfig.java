package kr.hhplus.be.server;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class P6SpyConfig {

  @Bean
  public P6SpyEventListener p6SpyCustomEventListener() {
    return new P6SpyEventListener();
  }

  @Bean
  public P6SpyFormatter p6SpyCustomFormatter() {
    return new P6SpyFormatter();
  }
}
