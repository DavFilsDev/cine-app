package school.hei.cineapp.it;

import static java.math.BigDecimal.valueOf;
import static java.time.Duration.ofMinutes;
import static java.time.Instant.parse;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static school.hei.cineapp.conf.TestUtils.CLIENT_A_EMAIL;
import static school.hei.cineapp.conf.TestUtils.EMPLOYEE_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MANAGER_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MOVIES_URL;
import static school.hei.cineapp.conf.TestUtils.PROJECTIONS_URL;
import static school.hei.cineapp.conf.TestUtils.PROJECTION_URL;
import static school.hei.cineapp.conf.TestUtils.ROOMS_URL;
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
import school.hei.cineapp.model.Projection;
import school.hei.cineapp.model.Room;

class ProjectionRbacIT extends TestcontainersConfigurer {
  @Autowired TestRestTemplate testRestTemplate;

  private String managerToken;
  private String employeeToken;
  private String clientToken;
  private Room room;
  private Movie movie;

  @BeforeEach
  void setUp() {
    managerToken = utils.tokenOf(MANAGER_EMAIL);
    employeeToken = utils.tokenOf(EMPLOYEE_EMAIL);
    clientToken = utils.tokenOf(CLIENT_A_EMAIL);
    room = createRoom();
    movie = createMovie();
  }

  @Test
  void everyone_can_list_projections_without_authentication() {
    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL, GET, new HttpEntity<>(jsonHeaders()), Projection[].class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void manager_can_crupdate_a_projection_for_an_existing_movie_and_room() {
    var toCreate = projection(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            PROJECTION_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Projection.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(movie.id(), response.getBody().movieId());
  }

  @Test
  void employee_cannot_crupdate_a_projection() {
    var toCreate = projection(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            PROJECTION_URL, PUT, new HttpEntity<>(toCreate, authHeaders(employeeToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void client_cannot_crupdate_a_projection() {
    var toCreate = projection(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            PROJECTION_URL, PUT, new HttpEntity<>(toCreate, authHeaders(clientToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  private Projection projection(String id) {
    return Projection.builder()
        .id(id)
        .datetime(parse("2026-08-15T20:00:00Z"))
        .seatPrice(valueOf(12.5))
        .movieId(movie.id())
        .roomId(room.id())
        .build();
  }

  private Room createRoom() {
    var toCreate = Room.builder().id(randomUUID().toString()).number("R-proj").capacity(80).build();
    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Room.class);
    return response.getBody();
  }

  private Movie createMovie() {
    var toCreate =
        Movie.builder()
            .id(randomUUID().toString())
            .title("Dune")
            .genre(of(Genre.SCI_FI))
            .description("A noble family becomes embroiled in a war for control of a planet.")
            .duration(ofMinutes(155))
            .build();
    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Movie.class);
    return response.getBody();
  }
}
