package kr.hhplus.be.server.reservations.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

  Reservation save(Reservation reservation);

  List<Reservation> findAllByUserId(UUID userId);
  void deleteById(UUID reserationId);

  Optional<Reservation> findByIdAndReservationStatus(UUID reservationId,
      ReservationStatus reservationStatus);
  List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus);

  void deleteAllByIdsIn(List<UUID> reservationList);
}
