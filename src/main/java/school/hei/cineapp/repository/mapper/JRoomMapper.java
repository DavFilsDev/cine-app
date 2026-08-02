package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Room;
import school.hei.cineapp.repository.model.JRoom;

@Component
public class JRoomMapper {
  public Room toDomain(JRoom entity) {
    return new Room(entity.getId(), entity.getNumber(), entity.getCapacity());
  }

  public JRoom toEntity(Room domain) {
    return new JRoom(domain.id(), domain.number(), domain.capacity());
  }
}
