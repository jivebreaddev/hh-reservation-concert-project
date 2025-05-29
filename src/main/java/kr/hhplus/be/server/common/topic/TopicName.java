package kr.hhplus.be.server.common.topic;

public enum TopicName {

  SEAT_STATUS(TopicContants.SEAT_STATUS),
  SEAT_DEAD_LETTER(TopicContants.SEAT_DEAD_LETTER);
  private final String value;

  TopicName(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
