package school.hei.cineapp.it;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.now;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static school.hei.cineapp.conf.TestUtils.CLIENT_A_EMAIL;
import static school.hei.cineapp.conf.TestUtils.EMPLOYEE_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MANAGER_EMAIL;
import static school.hei.cineapp.conf.TestUtils.MOVIES_URL;
import static school.hei.cineapp.conf.TestUtils.PROJECTION_URL;
import static school.hei.cineapp.conf.TestUtils.REGISTER_URL;
import static school.hei.cineapp.conf.TestUtils.RESERVATIONS_URL;
import static school.hei.cineapp.conf.TestUtils.RESERVATION_BY_ID_URL;
import static school.hei.cineapp.conf.TestUtils.RESERVATION_TICKET_URL;
import static school.hei.cineapp.conf.TestUtils.RESERVATION_URL;
import static school.hei.cineapp.conf.TestUtils.ROOMS_URL;
import static school.hei.cineapp.conf.TestUtils.ROOM_SEATS_URL;
import static school.hei.cineapp.conf.TestUtils.USER_RESERVATIONS_URL;
import static school.hei.cineapp.conf.TestUtils.authHeaders;

import java.time.Duration;
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
import school.hei.cineapp.model.RegisterPayload;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.model.Room;
import school.hei.cineapp.model.Seat;
import school.hei.cineapp.model.UserWithToken;

class ReservationRbacIT extends TestcontainersConfigurer {
  @Autowired TestRestTemplate testRestTemplate;

  private String managerToken;
  private String employeeToken;
  private UserWithToken clientA;
  private UserWithToken clientB;

  @BeforeEach
  void setUp() {
    managerToken = utils.tokenOf(MANAGER_EMAIL);
    employeeToken = utils.tokenOf(EMPLOYEE_EMAIL);
    clientA = utils.login(CLIENT_A_EMAIL);
    clientB = registerNewClient();
  }

  @Test
  void staff_only_can_list_all_reservations() {
    var managerResponse =
        testRestTemplate.exchange(
            RESERVATIONS_URL,
            GET,
            new HttpEntity<>(authHeaders(managerToken)),
            Reservation[].class);
    assertEquals(OK, managerResponse.getStatusCode());

    var clientResponse =
        testRestTemplate.exchange(
            RESERVATIONS_URL, GET, new HttpEntity<>(authHeaders(clientA.getToken())), Map.class);
    assertEquals(FORBIDDEN, clientResponse.getStatusCode());
  }

  @Test
  void staff_can_crupdate_a_reservation_but_client_cannot() {
    var projection = createProjection();
    var seat = createSeat(projection.roomId());

    var toCreate = reservation(projection.id(), seat.id(), clientA.getId());
    var staffResponse =
        testRestTemplate.exchange(
            RESERVATION_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Reservation.class);
    assertEquals(OK, staffResponse.getStatusCode());
    assertNotNull(staffResponse.getBody());

    var anotherSeat = createSeat(projection.roomId());
    var clientAttempt = reservation(projection.id(), anotherSeat.id(), clientA.getId());
    var clientResponse =
        testRestTemplate.exchange(
            RESERVATION_URL,
            PUT,
            new HttpEntity<>(clientAttempt, authHeaders(clientA.getToken())),
            Map.class);
    assertEquals(FORBIDDEN, clientResponse.getStatusCode());
  }

  @Test
  void owner_client_can_read_their_own_reservation() {
    var reservation = createReservationForClientA();

    var response =
        testRestTemplate.exchange(
            RESERVATION_BY_ID_URL,
            GET,
            new HttpEntity<>(authHeaders(clientA.getToken())),
            Reservation.class,
            reservation.id());

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void non_owner_client_cannot_read_someone_elses_reservation() {
    var reservation = createReservationForClientA();

    var response =
        testRestTemplate.exchange(
            RESERVATION_BY_ID_URL,
            GET,
            new HttpEntity<>(authHeaders(clientB.getToken())),
            Map.class,
            reservation.id());

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void staff_can_read_any_reservation() {
    var reservation = createReservationForClientA();

    var response =
        testRestTemplate.exchange(
            RESERVATION_BY_ID_URL,
            GET,
            new HttpEntity<>(authHeaders(employeeToken)),
            Reservation.class,
            reservation.id());

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void client_can_book_a_seat_for_themselves() {
    var projection = createProjection();
    var seat = createSeat(projection.roomId());
    var toCreate = reservation(projection.id(), seat.id(), "irrelevant-should-be-overridden");

    var response =
        testRestTemplate.exchange(
            USER_RESERVATIONS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(clientA.getToken())),
            Reservation.class,
            clientA.getId());

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(clientA.getId(), response.getBody().userId());
  }

  @Test
  void client_cannot_book_a_seat_on_behalf_of_another_client() {
    var projection = createProjection();
    var seat = createSeat(projection.roomId());
    var toCreate = reservation(projection.id(), seat.id(), clientB.getId());

    var response =
        testRestTemplate.exchange(
            USER_RESERVATIONS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(clientA.getToken())),
            Map.class,
            clientB.getId());

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void cannot_reserve_the_same_seat_twice_for_the_same_projection() {
    var projection = createProjection();
    var seat = createSeat(projection.roomId());

    var first = reservation(projection.id(), seat.id(), clientA.getId());
    var firstResponse =
        testRestTemplate.exchange(
            RESERVATION_URL,
            PUT,
            new HttpEntity<>(first, authHeaders(managerToken)),
            Reservation.class);
    assertEquals(OK, firstResponse.getStatusCode());

    var second = reservation(projection.id(), seat.id(), clientB.getId());
    var secondResponse =
        testRestTemplate.exchange(
            RESERVATION_URL, PUT, new HttpEntity<>(second, authHeaders(managerToken)), Map.class);
    assertEquals(BAD_REQUEST, secondResponse.getStatusCode());
  }

  @Test
  void owner_client_can_download_their_own_ticket() {
    var reservation = createReservationForClientA();

    var response =
        testRestTemplate.exchange(
            RESERVATION_TICKET_URL,
            GET,
            new HttpEntity<>(authHeaders(clientA.getToken())),
            byte[].class,
            reservation.id());

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().length > 0);

    var contentDisposition = response.getHeaders().getFirst("Content-Disposition");
    assertNotNull(contentDisposition);
    assertTrue(contentDisposition.contains("ticket-reservation-" + reservation.id()));
  }

  @Test
  void non_owner_client_cannot_download_someone_elses_ticket() {
    var reservation = createReservationForClientA();

    var response =
        testRestTemplate.exchange(
            RESERVATION_TICKET_URL,
            GET,
            new HttpEntity<>(authHeaders(clientB.getToken())),
            Map.class,
            reservation.id());

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  private Reservation createReservationForClientA() {
    var projection = createProjection();
    var seat = createSeat(projection.roomId());
    var toCreate = reservation(projection.id(), seat.id(), clientA.getId());

    var response =
        testRestTemplate.exchange(
            RESERVATION_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Reservation.class);
    return response.getBody();
  }

  private static Reservation reservation(String projectionId, String seatId, String userId) {
    return Reservation.builder()
        .id(randomUUID().toString())
        .createdAt(now())
        .projectionId(projectionId)
        .seatId(seatId)
        .userId(userId)
        .build();
  }

  private Projection createProjection() {
    var room = createRoom();
    var movie = createMovie();
    var toCreate =
        Projection.builder()
            .id(randomUUID().toString())
            .datetime(now())
            .seatPrice(TEN)
            .movieId(movie.id())
            .roomId(room.id())
            .build();

    var response =
        testRestTemplate.exchange(
            PROJECTION_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Projection.class);
    return response.getBody();
  }

  private Seat createSeat(String roomId) {
    var toCreate =
        Seat.builder()
            .id(randomUUID().toString())
            .number("S-" + randomUUID().toString().substring(0, 6))
            .build();

    var response =
        testRestTemplate.exchange(
            ROOM_SEATS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Seat.class,
            roomId);
    return response.getBody();
  }

  private Room createRoom() {
    var toCreate =
        Room.builder()
            .id(randomUUID().toString())
            .number("R-" + randomUUID().toString().substring(0, 6))
            .capacity(80)
            .build();

    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Room.class);
    return response.getBody();
  }

  private Movie createMovie() {
    var toCreate =
        Movie.builder()
            .id(randomUUID().toString())
            .title("Movie-" + randomUUID())
            .genre(of(Genre.ACTION))
            .description("Integration test fixture")
            .duration(Duration.ofMinutes(120))
            .build();

    var response =
        testRestTemplate.exchange(
            MOVIES_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Movie.class);
    return response.getBody();
  }

  private UserWithToken registerNewClient() {
    var payload =
        new RegisterPayload(
            "Client",
            "B-" + randomUUID(),
            null,
            "client-b-" + randomUUID() + "@cine-app.test",
            "AdminPass123!",
            null);

    return testRestTemplate.postForObject(REGISTER_URL, payload, UserWithToken.class);
  }
}
