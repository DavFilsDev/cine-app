package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.RegisterPayload;

@Component
@AllArgsConstructor
public class RegisterUserPayloadValidator implements Consumer<RegisterPayload> {
  private final JUserRepository jRepository;

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(RegisterPayload payload) {
    var sj = new StringJoiner(". ");

    if (isBlank(payload.firstName())) {
      sj.add("FirstName is mandatory");
    }
    if (isBlank(payload.lastName())) {
      sj.add("LastName is mandatory");
    }
    if (isBlank(payload.email())) {
      sj.add("Email is mandatory");
    }
    if (isBlank(payload.password())) {
      sj.add("Password is mandatory");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }

    if (jRepository.existsByEmail(payload.email())) {
      sj.add("Email already taken");
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
