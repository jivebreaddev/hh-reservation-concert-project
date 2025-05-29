package kr.hhplus.be.server.platform.ui.listener;

import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface DataPlatformEventListener {

  void handleSeatHeld(SeatHeldStatusEvent event, Acknowledgment ack);

  void handleSeatPending(SeatPendingStatusEvent event, Acknowledgment ack);

  void handleSeatAvailable(SeatAvailableStatusEvent event, Acknowledgment ack);

  void handleReservedStatus(SeatReservedStatusEvent event, Acknowledgment ack);

}
