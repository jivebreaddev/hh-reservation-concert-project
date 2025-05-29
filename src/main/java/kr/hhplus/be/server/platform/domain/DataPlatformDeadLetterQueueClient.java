package kr.hhplus.be.server.platform.domain;

public interface DataPlatformDeadLetterQueueClient {

  void send(Object event);
}
