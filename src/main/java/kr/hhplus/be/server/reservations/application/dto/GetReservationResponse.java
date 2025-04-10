package kr.hhplus.be.server.reservations.application.dto;

import java.util.List;
import kr.hhplus.be.server.reservations.domain.Reservation;

public class GetReservationResponse {

  private List<GetUserReservation> reservations;

  public GetReservationResponse(List<GetUserReservation> reservations) {
    this.reservations = reservations;
  }

  public List<GetUserReservation> getReservations() {
    return reservations;
  }

  public static GetReservationResponse of(List<Reservation> reservations) {
    return new GetReservationResponse(reservations.stream().map(
        reservation -> new GetUserReservation(reservation.getReservedAt(), reservation.getSeatId(),
            reservation.getReservationStatus())).toList());
  }
}
