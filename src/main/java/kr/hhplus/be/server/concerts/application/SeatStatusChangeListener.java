package kr.hhplus.be.server.concerts.application;

import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SeatStatusChangeListener {

  private final SeatQueryUseCase seatQueryUseCase;
  private final RankingUseCase rankingUseCase;

  public SeatStatusChangeListener(SeatQueryUseCase seatQueryUseCase, RankingUseCase rankingUseCase) {
    this.seatQueryUseCase = seatQueryUseCase;
    this.rankingUseCase = rankingUseCase;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleAvailable(SeatAvailableStatusEvent event) {
    seatQueryUseCase.changeToAvailable(event.getSeatId());
    // 실패시 재시도 필요
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleReserved(SeatReservedStatusEvent event) {
    seatQueryUseCase.changeToReserved(event.getSeatId());
    rankingUseCase.rankingUpdate(event.getConcertId().toString(), 1);
  }

  @EventListener
  public void handleHeld(SeatHeldStatusEvent event) {
    seatQueryUseCase.changeToHeld(event.getSeatId());
    // 실패시 재시도 필요
  }

}
