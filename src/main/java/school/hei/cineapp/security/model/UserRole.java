package school.hei.cineapp.security.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
  CLIENT,
  EMPLOYEE,
  MANAGER;

  public String role() {
    return name();
  }

  @Override
  public String getAuthority() {
    return "ROLE_" + role();
  }

  @Override
  public String toString() {
    return role();
  }
}
