package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Seat;
import school.hei.cineapp.repository.model.JSeat;

@Component
public class JSeatMapper {
  public Seat toDomain(JSeat entity) {
    return new Seat(entity.getId(), entity.getNumber(), entity.getRoomId());
  }

  public JSeat toEntity(Seat domain) {
    return new JSeat(domain.id(), domain.number(), domain.roomId());
  }
}
