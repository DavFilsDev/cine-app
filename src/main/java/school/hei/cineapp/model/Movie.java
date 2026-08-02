package school.hei.cineapp.model;

import java.time.Duration;
import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
public record Movie(
    String id, String title, List<Genre> genre, String description, Duration duration) {

  public Movie {
    if (genre == null) {
      genre = List.of();
    }
  }
}
