package kr.hhplus.be.server.config.date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfiguration {

  @Bean
  DateTimeFactory dateTimeFactory(){
    return new DefaultDateTimeFactory();
  }
}
