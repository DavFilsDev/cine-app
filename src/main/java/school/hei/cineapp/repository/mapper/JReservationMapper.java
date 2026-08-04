package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.repository.model.JReservation;

@Component
public class JReservationMapper {
  public Reservation toDomain(JReservation entity) {
    return new Reservation(
        entity.getId(),
        entity.getCreatedAt(),
        entity.getProjectionId(),
        entity.getSeatId(),
        entity.getUserId());
  }

  public JReservation toEntity(Reservation domain) {
    return new JReservation(
        domain.id(), domain.createdAt(), domain.projectionId(), domain.seatId(), domain.userId());
  }
}
