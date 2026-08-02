package school.hei.cineapp.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Movie;
import school.hei.cineapp.repository.model.JMovie;

@Component
public class JMovieMapper {
  public Movie toDomain(JMovie entity) {
    return new Movie(
        entity.getId(),
        entity.getTitle(),
        entity.getGenre(),
        entity.getDescription(),
        entity.getDuration());
  }

  public JMovie toEntity(Movie domain) {
    return new JMovie(
        domain.id(), domain.title(), domain.genre(), domain.description(), domain.duration());
  }
}
