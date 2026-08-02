package school.hei.cineapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Projection;
import school.hei.cineapp.repository.JProjectionRepository;
import school.hei.cineapp.repository.mapper.JProjectionMapper;
import school.hei.cineapp.service.validator.CrupdateProjectionValidator;

@Service
@AllArgsConstructor
public class ProjectionService {
  private final JProjectionRepository jRepository;
  private final JProjectionMapper jMapper;
  private final CrupdateProjectionValidator crupdateValidator;

  public List<Projection> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public Projection getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Projection(id=" + id + ") not found"));
  }

  public Projection save(Projection toSave) {
    crupdateValidator.accept(toSave);

    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}
