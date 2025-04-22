package kr.hhplus.be.server;

import kr.hhplus.be.server.config.jpa.JpaConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ServerApplication.class)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, JpaConfig.class})
public class IntegrationTest {


}
