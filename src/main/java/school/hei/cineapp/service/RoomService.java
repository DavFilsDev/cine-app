package school.hei.cineapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Room;
import school.hei.cineapp.repository.JRoomRepository;
import school.hei.cineapp.repository.mapper.JRoomMapper;
import school.hei.cineapp.service.validator.CrupdateRoomValidator;

@Service
@AllArgsConstructor
public class RoomService {
  private final JRoomRepository jRepository;
  private final JRoomMapper jMapper;
  private final CrupdateRoomValidator crupdateValidator;

  public List<Room> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public Room getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Room(id=" + id + ") not found"));
  }

  public Room save(Room toSave) {
    crupdateValidator.accept(toSave);

    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}
