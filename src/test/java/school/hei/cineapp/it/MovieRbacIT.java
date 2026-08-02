package school.hei.cineapp.it;

import static java.time.Duration.ofMinutes;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static school.hei.cineapp.conf.TestUtils.CLIENT_A_EMAIL;
import static school.hei.cineapp.conf.TestUtils.EMPLOYEE_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MANAGER_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MOVIES_URL;
import static school.hei.cineapp.conf.TestUtils.MOVIE_BY_ID_URL;
import static school.hei.cineapp.conf.TestUtils.authHeaders;
import static school.hei.cineapp.conf.TestUtils.jsonHeaders;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import school.hei.cineapp.conf.TestcontainersConfigurer;
import school.hei.cineapp.model.Genre;
import school.hei.cineapp.model.Movie;

class MovieRbacIT extends TestcontainersConfigurer {
  @Autowired TestRestTemplate testRestTemplate;

  private String managerToken;
  private String employeeToken;
  private String clientToken;

  @BeforeEach
  void setUp() {
    managerToken = utils.tokenOf(MANAGER_EMAIL);
    employeeToken = utils.tokenOf(EMPLOYEE_EMAIL);
    clientToken = utils.tokenOf(CLIENT_A_EMAIL);
  }

  @Test
  void everyone_can_list_movies_without_authentication() {
    var response =
        testRestTemplate.exchange(MOVIES_URL, GET, new HttpEntity<>(jsonHeaders()), Movie[].class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void manager_can_crupdate_a_movie() {
    var toCreate = movie(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Movie.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(toCreate.title(), response.getBody().title());

    var fetchResponse =
        testRestTemplate.exchange(
            MOVIE_BY_ID_URL, GET, new HttpEntity<>(jsonHeaders()), Movie.class, toCreate.id());
    assertEquals(OK, fetchResponse.getStatusCode());
  }

  @Test
  void employee_cannot_crupdate_a_movie() {
    var toCreate = movie(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, authHeaders(employeeToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void client_cannot_crupdate_a_movie() {
    var toCreate = movie(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, authHeaders(clientToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void anonymous_cannot_crupdate_a_movie() {
    var toCreate = movie(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, jsonHeaders()), Map.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  private static Movie movie(String id) {
    return Movie.builder()
        .id(id)
        .title("Interstellar " + id.substring(0, 6))
        .genre(of(Genre.SCI_FI, Genre.DRAMA))
        .description("A team of explorers travel through a wormhole in space.")
        .duration(ofMinutes(169))
        .build();
  }
}
