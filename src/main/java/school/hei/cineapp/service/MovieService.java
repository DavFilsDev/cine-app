package school.hei.cineapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Movie;
import school.hei.cineapp.repository.JMovieRepository;
import school.hei.cineapp.repository.mapper.JMovieMapper;
import school.hei.cineapp.service.validator.CrupdateMovieValidator;

@Service
@AllArgsConstructor
public class MovieService {
  private final JMovieRepository jRepository;
  private final JMovieMapper jMapper;
  private final CrupdateMovieValidator crupdateValidator;

  public List<Movie> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public Movie getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Movie(id=" + id + ") not found"));
  }

  public Movie save(Movie toSave) {
    crupdateValidator.accept(toSave);

    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}
