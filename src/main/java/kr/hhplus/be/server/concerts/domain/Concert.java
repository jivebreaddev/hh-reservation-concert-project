package kr.hhplus.be.server.concerts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "concerts")
public class Concert {
  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "venue", nullable = false, columnDefinition = "varchar(255)")
  private String venue;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  private Concert(UUID id, String venue) {
    this.id = id;
    this.venue = venue;
    this.createdAt = LocalDateTime.now();
  }

  protected Concert() {
  }

  public static Concert of(String venue){
    return new Concert(UUID.randomUUID(), venue);
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Concert concert = (Concert) o;
    return Objects.equals(id, concert.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
