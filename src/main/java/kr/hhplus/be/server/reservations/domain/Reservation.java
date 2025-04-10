package kr.hhplus.be.server.reservations.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

  @Id
  @Column(name = "id", columnDefinition = "binary(16)")
  private UUID id;
  @Column(name = "user_id", columnDefinition = "binary(16)")
  private UUID userId;

  @Column(name = "seat_id", columnDefinition = "binary(16)")
  private UUID seatId;

  @Column(name = "reservation_status", columnDefinition = "varchar(255)")
  private ReservationStatus reservationStatus;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "reserved_at", nullable = true)
  private LocalDateTime reservedAt;

  protected Reservation(UUID id, UUID userId, UUID seatId, ReservationStatus reservationStatus,
      LocalDateTime expiresAt) {
    this.id = id;
    this.userId = userId;
    this.seatId = seatId;
    this.reservationStatus = reservationStatus;
    this.expiresAt = expiresAt;
  }

  protected Reservation() {

  }

  public static Reservation createTemporaryReservation(UUID id, UUID userId, UUID seat_id,
      Long minutes) {
    return new Reservation(id, userId, seat_id, ReservationStatus.PENDING,
        LocalDateTime.now().plusMinutes(minutes));
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getSeatId() {
    return seatId;
  }

  public ReservationStatus getReservationStatus() {
    return reservationStatus;
  }

  public LocalDateTime getReservedAt() {
    return reservedAt;
  }

  public void createReservation() {
    this.reservationStatus = ReservationStatus.CONFIRMED;
    this.reservedAt = LocalDateTime.now();
  }
}
