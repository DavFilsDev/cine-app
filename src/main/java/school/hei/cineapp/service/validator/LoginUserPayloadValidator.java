package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.LoginPayload;

@Component
public class LoginUserPayloadValidator implements Consumer<LoginPayload> {
  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(LoginPayload payload) {
    var sj = new StringJoiner(". ");

    if (isBlank(payload.email())) {
      sj.add("Email is mandatory");
    }
    if (isBlank(payload.password())) {
      sj.add("Password is mandatory");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
