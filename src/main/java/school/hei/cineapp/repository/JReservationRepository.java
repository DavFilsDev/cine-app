package school.hei.cineapp.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.cineapp.repository.model.JReservation;

public interface JReservationRepository extends JpaRepository<JReservation, UUID> {
  List<JReservation> findByUserId(String userId);

  boolean existsByProjectionIdAndSeatId(String projectionId, String seatId);
}
