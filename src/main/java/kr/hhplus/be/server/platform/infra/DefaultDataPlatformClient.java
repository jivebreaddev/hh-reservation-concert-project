package kr.hhplus.be.server.platform.infra;

import org.springframework.stereotype.Component;

@Component
public class DefaultDataPlatformClient implements DataPlatformClient {

  public void send(Object object) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
