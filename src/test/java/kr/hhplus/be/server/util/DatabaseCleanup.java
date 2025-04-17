package kr.hhplus.be.server.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanup {

  @PersistenceContext
  private final EntityManager entityManager;


  public DatabaseCleanup(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional
  public void cleanUp(List<String> tables){
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
    tables.forEach(tableName ->
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
  }
}
