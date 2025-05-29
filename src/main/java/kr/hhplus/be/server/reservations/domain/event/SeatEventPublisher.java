package kr.hhplus.be.server.reservations.domain.event;

public interface SeatEventPublisher {

  void publishSeatReservedEvent(SeatReservedStatusEvent event);

  void publishSeatPendingEvent(SeatPendingStatusEvent event);

  void publishSeatAvailableEvent(SeatAvailableStatusEvent event);

  void publishSeatHeldEvent(SeatHeldStatusEvent event);
}
