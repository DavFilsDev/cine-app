package school.hei.cineapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Seat;
import school.hei.cineapp.repository.JSeatRepository;
import school.hei.cineapp.repository.mapper.JSeatMapper;
import school.hei.cineapp.service.validator.CrupdateSeatValidator;

@Service
@AllArgsConstructor
public class SeatService {
  private final JSeatRepository jRepository;
  private final JSeatMapper jMapper;
  private final CrupdateSeatValidator crupdateValidator;

  public List<Seat> findAllByRoomId(String roomId) {
    return jRepository.findAllByRoomId(roomId).stream().map(jMapper::toDomain).toList();
  }

  public Seat getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Seat(id=" + id + ") not found"));
  }

  public Seat save(Seat toSave) {
    crupdateValidator.accept(toSave);

    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}
