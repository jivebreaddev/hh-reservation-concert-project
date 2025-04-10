package kr.hhplus.be.server.concerts.application;

import kr.hhplus.be.server.reservations.application.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.application.event.SeatPendingStatusEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SeatStatusChangeListener {

  private final SeatQueryUseCase seatQueryUseCase;

  public SeatStatusChangeListener(SeatQueryUseCase seatQueryUseCase) {
    this.seatQueryUseCase = seatQueryUseCase;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleAvailable(SeatAvailableStatusEvent event) {
    seatQueryUseCase.changeToAvailable(event.getSeatId());
    // 실패시 재시도 필요
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleReserved(SeatPendingStatusEvent event) {
    seatQueryUseCase.changeToReserved(event.getSeatId());
    // 실패시 재시도 필요
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleHeld(SeatPendingStatusEvent event) {
    seatQueryUseCase.changeToHeld(event.getSeatId());
    // 실패시 재시도 필요
  }
}
