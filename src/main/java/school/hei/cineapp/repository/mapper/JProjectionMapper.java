package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Projection;
import school.hei.cineapp.repository.model.JProjection;

@Component
public class JProjectionMapper {
  public Projection toDomain(JProjection entity) {
    return new Projection(
        entity.getId(),
        entity.getDatetime(),
        entity.getSeatPrice(),
        entity.getMovieId(),
        entity.getRoomId());
  }

  public JProjection toEntity(Projection domain) {
    return new JProjection(
        domain.id(), domain.datetime(), domain.seatPrice(), domain.movieId(), domain.roomId());
  }
}
