package kr.hhplus.be.server.config.jpa;

import jakarta.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Properties;
import javax.sql.DataSource;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.infra.DefaultConcertRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "kr.hhplus.be.server",
    entityManagerFactoryRef = "customEntityManagerFactory"
)
public class JpaConfig {
    @Bean("customEntityManagerFactory")
    @Profile("default")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(final DataSource datasource){
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("kr.hhplus.be.server");
        factory.setDataSource(datasource);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        System.setProperty("hibernate.timezone.default_storage", "NORMALIZE_UTC");

        jpaProperties.put("hibernate.jdbc.time_zone", "UTC");

        factory.setJpaProperties(jpaProperties);
        return factory;
    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
