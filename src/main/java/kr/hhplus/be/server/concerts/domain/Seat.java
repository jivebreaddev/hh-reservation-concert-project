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

  protected Seat(UUID id, UUID concertId, SeatStatus seatStatus, LocalDateTime createdAt) {
    this.id = id;
    this.concertId = concertId;
    this.seatStatus = seatStatus;
    this.createdAt = createdAt;
  }

  public static Seat of(UUID id, UUID concertId, SeatStatus seatStatus, LocalDateTime createdAt){
    return new Seat(id, concertId, seatStatus, createdAt);
  }

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

  public boolean isAvailable(){
    return this.seatStatus.equals(SeatStatus.AVAILABLE);
  }
  public boolean isHeld(){
    return this.seatStatus.equals(SeatStatus.HELD);
  }
  public boolean isReserved(){
    return this.seatStatus.equals(SeatStatus.RESERVED);
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
