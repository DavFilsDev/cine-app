package school.hei.cineapp.conf;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import school.hei.cineapp.model.LoginPayload;
import school.hei.cineapp.model.UserWithToken;

@AllArgsConstructor
public class TestUtils {
  public static final String MANAGER_EMAIL = "manager@cine-app.test";
  public static final String EMPLOYEE_EMAIL = "employee@cine-app.test";
  public static final String CLIENT_A_EMAIL = "client-a@cine-app.test";
  public static final String SEEDED_PASSWORD = "AdminPass123!";

  public static final String LOGIN_URL = "/login";
  public static final String REGISTER_URL = "/register";

  public static final String MOVIES_URL = "/movies";
  public static final String MOVIE_BY_ID_URL = "/movies/{movieId}";

  public static final String ROOMS_URL = "/rooms";
  public static final String ROOM_BY_ID_URL = "/rooms/{roomId}";
  public static final String ROOM_SEATS_URL = "/rooms/{roomId}/seats";

  public static final String PROJECTIONS_URL = "/projections";
  public static final String PROJECTION_URL = "/projection";

  public static final String RESERVATIONS_URL = "/reservations";
  public static final String RESERVATION_URL = "/reservation";
  public static final String RESERVATION_BY_ID_URL = "/reservations/{reservationId}";
  public static final String USER_RESERVATIONS_URL = "/users/{uid}/reservations";

  public static final String RESERVATION_TICKET_URL = "/reservations/{reservationId}/ticket";

  private final TestRestTemplate testRestTemplate;

  public UserWithToken login(String email) {
    return testRestTemplate.postForObject(
        LOGIN_URL, new LoginPayload(email, SEEDED_PASSWORD), UserWithToken.class);
  }

  public String tokenOf(String email) {
    return login(email).getToken();
  }

  public static HttpHeaders authHeaders(String token) {
    var headers = jsonHeaders();
    headers.setBearerAuth(token);
    return headers;
  }

  public static HttpHeaders jsonHeaders() {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    return headers;
  }
}
