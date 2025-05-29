package kr.hhplus.be.server;

import jakarta.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration
public class TestcontainersConfiguration {
  private static final DockerComposeContainer<?> COMPOSE_CONTAINER;

  static {
    COMPOSE_CONTAINER = new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
        .withExposedService("mysql", 3306, Wait.forListeningPort())
        .withExposedService("redis-sentinel", 26379, Wait.forListeningPort())
        .withLocalCompose(true)
        .withTailChildContainers(true)
        .withRemoveImages(DockerComposeContainer.RemoveImages.LOCAL)
        .withStartupTimeout(Duration.ofMinutes(2))
        .withOptions("--compatibility")
        .withEnv(Map.of(
            "MYSQL_ROOT_PASSWORD", "application",
            "MYSQL_DATABASE", "hhplus",
            "MYSQL_USER", "application",
            "MYSQL_PASSWORD", "application"
        ));

    COMPOSE_CONTAINER.start();

  }
  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    // MySQL 연결 정보 설정
    String mysqlHost = COMPOSE_CONTAINER.getServiceHost("mysql", 3306);
    Integer mysqlPort = COMPOSE_CONTAINER.getServicePort("mysql", 3306);

    System.setProperty("spring.datasource.url",
        "jdbc:p6spy:mysql://" + mysqlHost + ":" +
            mysqlPort + "/hhplus?characterEncoding=UTF-8&serverTimezone=UTC"
    );
    System.setProperty("spring.datasource.username", "application");
    System.setProperty("spring.datasource.password", "application");

    String redisHost = COMPOSE_CONTAINER.getServiceHost("redis-sentinel", 26379);
    Integer redisPort = COMPOSE_CONTAINER.getServicePort("redis-sentinel", 26379);
    System.setProperty("spring.redis.sentinel.master", "mymaster");
    System.setProperty("spring.redis.sentinel.nodes", redisHost + ":" + redisPort);
    System.setProperty("spring.redis.password", "mypass");

    String kafkaHost = COMPOSE_CONTAINER.getServiceHost("kafka", 9094);
    Integer kafkaPort = COMPOSE_CONTAINER.getServicePort("kafka", 9094);
    registry.add("spring.kafka.bootstrap-servers", () -> kafkaHost + ":" + kafkaPort);
  }
  @PreDestroy
  public void stop() {
    COMPOSE_CONTAINER.stop();
  }

  @Primary
  @Profile("test")
  @Bean("customEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(final DataSource datasource){
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("kr.hhplus.be.server");
    factory.setDataSource(datasource);
    // Hibernate 관련 설정
    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    jpaProperties.put("hibernate.hbm2ddl.auto", "update");
    System.setProperty("hibernate.timezone.default_storage", "NORMALIZE_UTC");

    jpaProperties.put("hibernate.format_sql", "true");
    jpaProperties.put("hibernate.show_sql", "true");
    jpaProperties.put("hibernate.jdbc.time_zone", "UTC");

    factory.setJpaProperties(jpaProperties);
    return factory;
  }

  public static String getKafkaBootstrapServers() {
    return COMPOSE_CONTAINER.getServiceHost("kafka", 9094) + ":" + COMPOSE_CONTAINER.getServicePort("kafka", 9094);
  }

  public AdminClient getAdminClient() {
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaBootstrapServers());
    return AdminClient.create(props);
  }
}
