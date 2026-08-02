package school.hei.cineapp.security.model;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import school.hei.cineapp.model.User;

public record Principal(User user) implements UserDetails {

  @Override
  public List<UserRole> getAuthorities() {
    return List.of(user.getRole());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
