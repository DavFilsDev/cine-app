package school.hei.cineapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.cineapp.repository.model.JProjection;

public interface JProjectionRepository extends JpaRepository<JProjection, String> {}
