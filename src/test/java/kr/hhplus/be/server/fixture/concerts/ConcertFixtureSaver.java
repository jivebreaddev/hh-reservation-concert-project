package kr.hhplus.be.server.fixture.concerts;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.concerts.domain.Concert;
import kr.hhplus.be.server.concerts.domain.Seat;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConcertFixtureSaver {
  @PersistenceContext
  private final EntityManager entityManager;

  public ConcertFixtureSaver(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  @Transactional
  public UUID saveConcertData(List<LocalDateTime> scheduleDates) {
    Concert concert = ConcertFixture.createConcert();
    entityManager.persist(concert);

    scheduleDates.forEach(
        scheduleDate -> entityManager.persist(
            ConcertFixture.createConcertSchedule(concert.getId(), scheduleDate))
    );

    for (int i = 0; i < 100; i++) {
      Seat seat = ConcertFixture.createSeat(concert.getId());
      entityManager.persist(seat);
    }

    return concert.getId();
  }

  @Transactional
  public UUID saveSoldOutConcertData(List<LocalDateTime> scheduleDates) {
    Concert concert = ConcertFixture.createConcert();
    entityManager.persist(concert);

    scheduleDates.forEach(
        scheduleDate -> entityManager.persist(
            ConcertFixture.createConcertSchedule(concert.getId(), scheduleDate))
    );

    for (int i = 0; i < 100; i++) {
      Seat seat = ConcertFixture.createReservedSeat(concert.getId());
      entityManager.persist(seat);
    }

    return concert.getId();
  }

  @Transactional
  public UUID saveHalfHeldConcertData(List<LocalDateTime> scheduleDates) {
    Concert concert = ConcertFixture.createConcert();
    entityManager.persist(concert);

    scheduleDates.forEach(
        scheduleDate -> entityManager.persist(
            ConcertFixture.createConcertSchedule(concert.getId(), scheduleDate))
    );

    for (int i = 0; i < 50; i++) {
      Seat seat = ConcertFixture.createHeldSeat(concert.getId());
      entityManager.persist(seat);
    }
    for (int i = 0; i < 50; i++) {
      Seat seat = ConcertFixture.createSeat(concert.getId());
      entityManager.persist(seat);
    }

    return concert.getId();
  }

  @Transactional
  public UUID soldOutConcertDate(LocalDateTime dateTime) {
    Concert concert = ConcertFixture.createConcert();
    entityManager.persist(concert);

    entityManager.persist(
        ConcertFixture.createSoldoutConcertSchedule(concert.getId(), dateTime));

    return concert.getId();
  }
}
