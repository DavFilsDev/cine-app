package school.hei.cineapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JUserRepository extends JpaRepository<JUser, String> {
  Optional<JUser> findByEmail(String email);

  boolean existsByEmail(String email);
}
