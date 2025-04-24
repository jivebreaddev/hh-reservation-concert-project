package kr.hhplus.be.server;

import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainersConfiguration {

  public static final MySQLContainer<?> MYSQL_CONTAINER;
  public static Network sharedNetwork = Network.newNetwork();
  static {
    MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
        .withDatabaseName("hhplus")
        .withUsername("test")
        .withPassword("test")
        .withExposedPorts(3306)
        .withNetwork(sharedNetwork)
        .withNetworkAliases("mysql")
        .waitingFor(Wait.forListeningPort());

    MYSQL_CONTAINER.start();
    String host = MYSQL_CONTAINER.getHost();
    int mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
    System.setProperty("spring.datasource.url",
        "jdbc:p6spy:mysql://" + host + ":" + mappedPort + "/hhplus" + "?characterEncoding=UTF-8&serverTimezone=UTC");
    System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
    System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
  }
  @DynamicPropertySource
  static void dynamicProperties(ApplicationContext applicationContext) {
    MYSQL_CONTAINER.start();

    System.setProperty("spring.datasource.url", "jdbc:p6spy:mysql://" + MYSQL_CONTAINER.getHost() + ":" + MYSQL_CONTAINER.getMappedPort(3306) + "/hhplus?characterEncoding=UTF-8&serverTimezone=UTC");
    System.setProperty("spring.datasource.username", "test");
    System.setProperty("spring.datasource.password", "test");

    System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
    System.setProperty("spring.jpa.properties.hibernate.timezone.default_storage", "NORMALIZE_UTC");
    System.setProperty("spring.jpa.properties.hibernate.jdbc.time_zone", "UTC");

    System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
  }
  @PreDestroy
  public void preDestroy() {
    if (MYSQL_CONTAINER.isRunning()) {
      MYSQL_CONTAINER.stop();
    }
  }
}
