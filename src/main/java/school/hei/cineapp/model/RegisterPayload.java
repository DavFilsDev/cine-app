package school.hei.cineapp.model;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static school.hei.cineapp.security.model.UserRole.CLIENT;

import java.time.LocalDate;

public record RegisterPayload(
    String firstName,
    String lastName,
    LocalDate birthdate,
    String email,
    String password,
    String phone) {

  public User toNewUser() {
    return new User(
        randomUUID().toString(),
        CLIENT,
        firstName,
        lastName,
        birthdate,
        email,
        password,
        phone,
        now());
  }
}
