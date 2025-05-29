package kr.hhplus.be.server.fake;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kr.hhplus.be.server.platform.infra.DataPlatformClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class FakePlatformApiListener implements DataPlatformClient {

  private final List<Object> sentEvents = new CopyOnWriteArrayList<>();

  @Override
  public void send(Object event) {
    sentEvents.add(event);

  }

  public List<Object> getSentEvents() {
    return sentEvents;
  }

  public void clear() {
    sentEvents.clear();
  }

}
