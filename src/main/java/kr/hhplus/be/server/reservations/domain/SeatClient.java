package kr.hhplus.be.server.reservations.domain;

import java.util.UUID;

public interface SeatClient {

  boolean seatAvailable(UUID seatId);

}
