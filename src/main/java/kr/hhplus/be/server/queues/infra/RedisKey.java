package kr.hhplus.be.server.queues.infra;

public enum RedisKey {
  QUEUE_MAP("queue-map"),
  WAIT_QUEUE("wait-queue"),
  PROCESS_QUEUE("process-queue"),
  QUEUE_SIZE("queue-size"),
  TOKEN_MAP("token-map"),
  DLQ("dead-letter-queue");
  private final String key;

  RedisKey(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }


  public String withSuffix(String suffix) {
    return key + ":" + suffix;
  }
}
