package school.hei.cineapp.security;

import static school.hei.cineapp.security.model.UserRole.EMPLOYEE;
import static school.hei.cineapp.security.model.UserRole.MANAGER;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import school.hei.cineapp.security.model.Principal;

@Component
public class SelfAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext context) {
    var auth = authentication.get();

    if (auth == null || !(auth.getPrincipal() instanceof Principal principal)) {
      return new AuthorizationDecision(false);
    }

    var uid = context.getVariables().get("uid");
    var role = principal.user().getRole();
    var isStaff = role == EMPLOYEE || role == MANAGER;
    var isSelf = uid != null && uid.equals(principal.user().getId());

    return new AuthorizationDecision(isStaff || isSelf);
  }
}
