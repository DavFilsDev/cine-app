package school.hei.cineapp.model;

import java.time.Instant;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import school.hei.cineapp.security.model.UserRole;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class UserWithToken extends User {
  @Getter private final String token;

  public UserWithToken(
      String id,
      UserRole role,
      String firstName,
      String lastName,
      LocalDate birthdate,
      String email,
      String password,
      String phone,
      String token,
      Instant joinedAt) {
    super(id, role, firstName, lastName, birthdate, email, password, phone, joinedAt);
    this.token = token;
  }

  public static UserWithToken from(User user, String token) {
    return new UserWithToken(
        user.id,
        user.role,
        user.firstName,
        user.lastName,
        user.birthdate,
        user.email,
        user.password,
        user.phone,
        token,
        user.joinedAt);
  }
}
