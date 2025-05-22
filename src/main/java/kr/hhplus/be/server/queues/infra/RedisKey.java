package kr.hhplus.be.server.queues.infra;

public enum RedisKey {
  QUEUE_MAP("queue-map"), // QUEUE 에 대한 상태 저장소
  WAIT_QUEUE("wait-queue"), // WAIT QUEUE (대기열)
  PROCESS_QUEUE("process-queue"), // 토큰이 허용된 처리열
  QUEUE_SIZE("queue-size"), // QUEUE SIZE 가 저장된 REDIS STRING
  TOKEN_MAP("token-map"), // TOKEN 에 대한 상태 저장소
  DLQ("dead-letter-queue"); // 상태 처리 실패한 QUEUE 저장소
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
