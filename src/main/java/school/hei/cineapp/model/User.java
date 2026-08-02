package school.hei.cineapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import school.hei.cineapp.security.model.UserRole;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public sealed class User permits UserWithToken {
  protected final String id;
  protected final UserRole role;
  protected final String firstName;
  protected final String lastName;
  protected final LocalDate birthdate;
  protected final String email;

  @JsonIgnore @Setter protected String password;

  protected final String phone;
  protected final Instant joinedAt;
}
