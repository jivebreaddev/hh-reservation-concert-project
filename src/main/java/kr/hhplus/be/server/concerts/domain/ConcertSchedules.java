package kr.hhplus.be.server.concerts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "concert_schedules")
public class ConcertSchedules {
  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "concert_id", columnDefinition = "binary(16)")
  private UUID concertId;

  @Column(name = "available_count")
  private Long availableCount;

  @Column(name = "concert_date", columnDefinition = "DATETIME")
  private LocalDateTime concertDate;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  private ConcertSchedules(UUID id, UUID concertId, Long availableCount, LocalDateTime concertDate) {
    this.id = id;
    this.concertId = concertId;
    this.availableCount = availableCount;
    this.concertDate = concertDate;
    this.createdAt = LocalDateTime.now();
  }
  protected ConcertSchedules() {
  }

  public static ConcertSchedules of(UUID id, UUID concertId, Long availableCount, LocalDateTime concertDate) {

    return new ConcertSchedules(id, concertId, availableCount, concertDate);

  }

  public UUID getConcertId() {
    return concertId;
  }

  public Long getAvailableCount() {
    return availableCount;
  }

  public LocalDateTime getConcertDate() {
    return concertDate;
  }
}
