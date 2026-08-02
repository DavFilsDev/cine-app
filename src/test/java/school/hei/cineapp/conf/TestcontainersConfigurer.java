package school.hei.cineapp.conf;

import static java.lang.Runtime.getRuntime;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class TestcontainersConfigurer {
  @ServiceConnection
  static final PostgreSQLContainer<?> PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:16");

  @Autowired TestRestTemplate testRestTemplate;

  protected TestUtils utils;

  @BeforeEach
  void setup() {
    utils = new TestUtils(testRestTemplate);
  }

  static {
    PSQL_CONTAINER.start();
    getRuntime().addShutdownHook(new Thread(PSQL_CONTAINER::stop));
  }
}
