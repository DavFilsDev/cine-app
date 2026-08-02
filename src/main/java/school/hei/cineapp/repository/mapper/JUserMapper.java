package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.User;
import school.hei.cineapp.repository.model.JUser;

@Component
public class JUserMapper {
  public User toDomain(JUser entity) {
    return new User(
        entity.getId(),
        entity.getRole(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getBirthdate(),
        entity.getEmail(),
        entity.getPassword(),
        entity.getPhone(),
        entity.getJoinedAt());
  }

  public JUser toEntity(User domain) {
    return new JUser(
        domain.getId(),
        domain.getRole(),
        domain.getFirstName(),
        domain.getLastName(),
        domain.getBirthdate(),
        domain.getEmail(),
        domain.getPassword(),
        domain.getPhone(),
        domain.getJoinedAt());
  }
}
