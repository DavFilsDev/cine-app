package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.repository.model.JReservation;

@Component
public class JReservationMapper {

  public Reservation toDomain(JReservation entity) {
    if (entity == null) {
      return null;
    }
    return new Reservation(
        entity.getId(),
        entity.getProjectionId(),
        entity.getSeatId(),
        entity.getUserId(),
        entity.getCreatedAt());
  }

  public JReservation toEntity(Reservation domain) {
    if (domain == null) {
      return null;
    }
    return JReservation.builder()
        .id(domain.getId())
        .projectionId(domain.getProjectionId())
        .seatId(domain.getSeatId())
        .userId(domain.getUserId())
        .createdAt(domain.getCreatedAt())
        .build();
  }
}
