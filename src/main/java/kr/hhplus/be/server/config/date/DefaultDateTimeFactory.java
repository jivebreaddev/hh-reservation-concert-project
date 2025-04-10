package kr.hhplus.be.server.config.date;

import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;

public class DefaultDateTimeFactory implements DateTimeFactory {

  @Override
  public LocalDateTime getCurrentTime() {
    return LocalDateTime.now();
  }
}
