package school.hei.cineapp.it;

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
import static school.hei.cineapp.conf.TestUtils.ROOMS_URL;
import static school.hei.cineapp.conf.TestUtils.ROOM_SEATS_URL;
import static school.hei.cineapp.conf.TestUtils.authHeaders;
import static school.hei.cineapp.conf.TestUtils.jsonHeaders;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import school.hei.cineapp.conf.TestcontainersConfigurer;
import school.hei.cineapp.model.Room;
import school.hei.cineapp.model.Seat;

class RoomAndSeatRbacIT extends TestcontainersConfigurer {
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
  void everyone_can_list_rooms_without_authentication() {
    var response =
        testRestTemplate.exchange(ROOMS_URL, GET, new HttpEntity<>(jsonHeaders()), Room[].class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void manager_can_crupdate_a_room() {
    var toCreate = room(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Room.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void employee_cannot_crupdate_a_room() {
    var toCreate = room(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(employeeToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void client_cannot_crupdate_a_room() {
    var toCreate = room(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(clientToken)), Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void anonymous_cannot_crupdate_a_room() {
    var toCreate = room(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, jsonHeaders()), Map.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void manager_can_crupdate_a_seat_in_an_existing_room() {
    var room = createRoom();
    var toCreate = Seat.builder().id(randomUUID().toString()).number("A1").build();

    var response =
        testRestTemplate.exchange(
            ROOM_SEATS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(managerToken)),
            Seat.class,
            room.id());

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(room.id(), response.getBody().roomId());

    var listResponse =
        testRestTemplate.exchange(
            ROOM_SEATS_URL, GET, new HttpEntity<>(jsonHeaders()), Seat[].class, room.id());
    assertEquals(OK, listResponse.getStatusCode());
  }

  @Test
  void client_cannot_crupdate_a_seat() {
    var room = createRoom();
    var toCreate = Seat.builder().id(randomUUID().toString()).number("A1").build();

    var response =
        testRestTemplate.exchange(
            ROOM_SEATS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(clientToken)),
            Map.class,
            room.id());

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  private Room createRoom() {
    var toCreate = room(randomUUID().toString());
    var response =
        testRestTemplate.exchange(
            ROOMS_URL, PUT, new HttpEntity<>(toCreate, authHeaders(managerToken)), Room.class);
    return response.getBody();
  }

  private static Room room(String id) {
    return Room.builder().id(id).number("R-" + id.substring(0, 6)).capacity(120).build();
  }
}
