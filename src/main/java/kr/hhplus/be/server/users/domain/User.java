package kr.hhplus.be.server.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
  private String name;

  @Column(name = "email", nullable = false, columnDefinition = "varchar(255)")
  private String email;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected User(UUID id, String name, String email, LocalDateTime updatedAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.updatedAt = updatedAt;
  }

  protected User() {

  }

  public static User of(String name, String email) {
    return new User(UUID.randomUUID(), name, email, LocalDateTime.now());
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
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
