package kr.hhplus.be.server.concerts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "seats")
public class Seat {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;
  @Column(name = "concert_id", nullable = false)
  private UUID concertId;
  @Column(name = "status", nullable = false, columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  private SeatStatus seatStatus;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public UUID getId() {
    return id;
  }

  public void toAvailable() {
    this.seatStatus = SeatStatus.AVAILABLE;
  }

  public void toHeld() {
    this.seatStatus = SeatStatus.HELD;
  }

  public void toReserved() {
    this.seatStatus = SeatStatus.RESERVED;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Seat seat = (Seat) o;
    return Objects.equals(id, seat.id) && Objects.equals(concertId,
        seat.concertId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, concertId);
  }
}
