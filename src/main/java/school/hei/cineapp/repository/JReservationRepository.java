package school.hei.cineapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.cineapp.repository.model.JReservation;

public interface JReservationRepository extends JpaRepository<JReservation, String> {
  List<JReservation> findByUserId(String userId);

  boolean existsByProjectionIdAndSeatId(String projectionId, String seatId);

  boolean existsByProjectionIdAndSeatIdAndIdNot(String projectionId, String seatId, String id);
}